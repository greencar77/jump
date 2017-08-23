package greencar77.jump.builder.webapp;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.helper.Validate;

import greencar77.jump.builder.ValidationException;
import greencar77.jump.builder.java.ArtifactSolver;
import greencar77.jump.builder.java.JaxRs;
import greencar77.jump.builder.java.MavenProjBuilder;
import greencar77.jump.builder.java.PreferenceConfig;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.classfile.RestMethod;
import greencar77.jump.model.java.classfile.TemplateClass;
import greencar77.jump.model.java.maven.PluginPom;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.model.java.maven.ServletWebDescriptor;
import greencar77.jump.model.java.maven.WebDescriptor;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.model.webapp.WebFramework;
import greencar77.jump.model.webapp.auth.AuthRealm;
import greencar77.jump.model.webapp.auth.Role;
import greencar77.jump.model.webapp.auth.User;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.webapp.JerseyMajorVersion;
import greencar77.jump.spec.webapp.WebAppSpec;

public class WebAppBuilder<S extends MavenProjSpec, M> extends MavenProjBuilder<WebAppSpec, WebAppModel> {
    
    private static final String ROLE = "app_edit";
    
    //protected WebAppSpec spec;

    //Having result object as instance variable relieves us from passing it around in method signatures.
    //This is the most heavily used variable in this builder.
    protected WebAppModel model = new WebAppModel();

    public WebAppBuilder() {
        super.model = model;
    }

    public WebAppBuilder(WebAppSpec spec) {
        super(spec);
        super.model = model;
        //this.spec = spec;
    }

    @Override
    public WebAppModel build() {
        super.build();
        
        model.setTargetContainer(getSpec().getTargetContainer());
        model.setJerseyVersion(getSpec().getJersey().getJerseyVersion());
        model.setWebFramework(getSpec().getWebFramework());
        
        if (getSpec().isAuthenticate()) {
            setupAuthRealm();
        }
        
        if (getSpec().getAppGenerator() != null) {
            invoke(getSpec().getAppGenerator());
        }

        //http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/
        if (getSpec().isServlet3Support()) {
            //web.xml is not required
            PluginPom warPlugin = new PluginPom("org.apache.maven.plugins", "maven-war-plugin", "3.1.0");
            warPlugin.configuration.append(code(indent(TAB + TAB + TAB + TAB + TAB,
                    "<failOnMissingWebXml>false</failOnMissingWebXml>")));
            model.getPom().getBuild().addPlugin(warPlugin);
        } else {
            setupWar();
        }

        return model;
    }

    @Override
    protected void validate() {
        super.validate();
        
        if (getSpec().getWebFramework() == WebFramework.JERSEY && getSpec().getJersey().getJerseyVersion() == null) {
            throw new ValidationException("missing jerseyVersion");
        }
    }

    protected void setupWarRestEasy() {
        Pom pom = model.getPom();
        
        //maven-war-plugin requires web.xml
        WebDescriptor webDescriptor = new WebDescriptor();
        webDescriptor.contextParams.put("resteasy.scan", "true");
        webDescriptor.contextParams.put("resteasy.servlet.mapping.prefix", "/rest");
        model.setServletMappingPrefix("/rest");

        webDescriptor.listeners.add("org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap");
        webDescriptor.registerServletThirdParty("org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher", "resteasy-servlet", "/rest/*", null);
        model.setWebDescriptor(webDescriptor);

        /*
Avoid warning 
[WARNING] Warning: selected war files include a WEB-INF/web.xml which will be ignored
(webxml attribute is missing from war task, or ignoreWebxml attribute is specified as 'true')
for plugin maven-war-plugin:2.1.1
http://stackoverflow.com/questions/4342245/disable-maven-warning-message-selected-war-files-include-a-web-inf-web-xml-wh
http://stackoverflow.com/questions/5351948/webxml-attribute-is-required-error-in-maven 
         */
        pom.getBuild().addPlugin("org.apache.maven.plugins", "maven-war-plugin", "2.6");

        pom.addDependency("org.jboss.resteasy/jaxrs-api/3.0.12.Final/provided");
    }

    protected void setupWarJersey() {
       
        //maven-war-plugin requires web.xml
        WebDescriptor webDescriptor = new WebDescriptor();

        Validate.notNull(getSpec().getJersey().getJerseyVersion());
        switch (getSpec().getJersey().getJerseyMajorVersion()) {
        case V1:
            webDescriptor.registerServletThirdParty("com.sun.jersey.spi.container.servlet.ServletContainer", "jersey-servlet", "/rest/*", null);
            model.setServletMappingPrefix("/rest");
            model.getPom().getDependencies().add("com.sun.jersey/jersey-servlet/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
            break;
        case V2:
            ServletWebDescriptor servlet = webDescriptor.registerServletThirdParty("org.glassfish.jersey.servlet.ServletContainer", "jersey-servlet", "/rest/*", null);
            model.setServletMappingPrefix("/rest");
            model.getPom().getDependencies().add("org.glassfish.jersey.containers/jersey-container-servlet/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
            servlet.initParams.put("jersey.config.server.provider.packages", "x.y");
            break;
        default:
            throw new RuntimeException(getSpec().getJersey().getJerseyMajorVersion().name());
        }


        if (getSpec().isAuthenticate()) {
            StringBuilder sb = new StringBuilder();

            sb.append(code(indent(TAB,
                    "<security-constraint>",
                    TAB + "<web-resource-collection>",
                    TAB + TAB + "<web-resource-name>Wildcard means whole app requires authentication</web-resource-name>",
                    TAB + TAB + "<url-pattern>/*</url-pattern>",
                    TAB + TAB + "<http-method>GET</http-method>",
                    TAB + TAB + "<http-method>POST</http-method>",
                    TAB + "</web-resource-collection>",
                    TAB + "<auth-constraint>",
                    TAB + TAB + "<role-name>" + ROLE + "</role-name>",
                    TAB + "</auth-constraint>",
                    TAB + "<user-data-constraint>",
                    TAB + TAB + "<transport-guarantee>NONE</transport-guarantee>",
                    TAB + "</user-data-constraint>",
                    "</security-constraint>",
                    "<login-config>",
                    TAB + "<auth-method>BASIC</auth-method>",
                    "</login-config>",
                    "<security-role>",
                    TAB + "<role-name>" + ROLE + "</role-name>",
                    "</security-role>"
                    )));
            webDescriptor.setSecuritySection(sb);
        }

        model.setWebDescriptor(webDescriptor);
        
        //pom.addDependency("javax.servlet/servlet-api/2.5/provided");
    }
    
    protected void setupWar() {

        if (getSpec().getWebFramework() == WebFramework.JERSEY) {
            switch (getSpec().getJersey().getJerseyMajorVersion()) {
            case V1:
                model.getPom().addDependency("com.sun.jersey/jersey-server/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
                model.getPom().addDependency("com.sun.jersey/jersey-bundle/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
                break;
            case V2:
                model.getPom().addDependency("org.glassfish.jersey.core/jersey-server/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
                break;
            default:
                throw new RuntimeException(getSpec().getJersey().getJerseyMajorVersion().name());
            }
            /*
    otherwise
    aug. 08, 2017 4:10:02 PM org.apache.catalina.core.StandardWrapperValve invoke
    SEVERE: Allocate exception for servlet jersey-servlet
    java.lang.NoSuchMethodError: com.sun.jersey.core.reflection.ReflectionHelper.getContextClassLoaderPA()Ljava/security/PrivilegedAction;
             */
        }

        switch (getSpec().getWebFramework()) {
            case RESTEASY:
                setupWarRestEasy();
                break;
            case JERSEY:
                setupWarJersey();
                break;
            default:
                throw new RuntimeException(model.getWebFramework().name());
        }
    }
    
    @Override
    protected WebAppSpec getSpec() {
        return (WebAppSpec) super.getSpec();
    }
    
    @Override
    protected String getPackagingType() {
        return "war";
    }
    
    protected void buildAppSimple() {
        Validate.isFalse(getSpec().isServlet3Support());
        
        Map<String, String> props = new HashMap<>();
        props.put("package", getSpec().getRootPackage());
        ClassFile userClass = new TemplateClass(getSpec().getRootPackage(), "User", "java/webapp/UserPojo.java", props);
        model.getClassFiles().add(userClass);
        
        RestClassFile restClass = new RestClassFile(getSpec().getRootPackage(), "Alpha");
        restClass.setPath("/user");
        model.getLocalEndpoints().add("/user");
        RestMethod method = new RestMethod("getUser");
        method.classAnnotations.add("@GET");
        restClass.imports.add("javax.ws.rs.GET");
        method.classAnnotations.add("@Produces(MediaType.APPLICATION_JSON)");
        switch (getSpec().getJersey().getJerseyMajorVersion()) {
        case V1:
            model.getPom().getDependencies().add("com.sun.jersey/jersey-json/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
            /*
             * otherwise
    aug. 08, 2017 4:05:39 PM com.sun.jersey.spi.container.ContainerResponse logException
    SEVERE: Mapped exception to response: 500 (Internal Server Error)
    javax.ws.rs.WebApplicationException: com.sun.jersey.api.MessageException: A message body writer for Java class com.x.y.User, and Java type class com.x.y.User, and MIME media type application/json was not found.
             */
            break;
        case V2:
            //https://stackoverflow.com/questions/26207252/messagebodywriter-not-found-for-media-type-application-json
            model.getPom().getDependencies().add("org.glassfish.jersey.media/jersey-media-json-jackson/" + getSpec().getJersey().getJerseyVersion() + "/runtime");
            break;
        default:
            throw new RuntimeException(getSpec().getJersey().getJerseyMajorVersion().name());
        }
        restClass.imports.add("javax.ws.rs.Produces");
        restClass.imports.add("javax.ws.rs.core.MediaType");
        method.getContent().append(code(indent(TAB + TAB,
                "System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));",
                "User user = new User();",
                "user.setFirstName(\"John\");",
                "user.setLastName(\"Doe\");",
                "return user;"
                )));
        method.setReturnType("User");
        restClass.imports.add(userClass.getFullName());
        restClass.imports.add("java.util.Arrays");

        restClass.getMethods().add(method);
        model.getRestClasses().add(restClass);
        model.getClassFiles().add(restClass);
        
        addDirectDependencies();
    }
    
    protected void buildAppSimpleSpring() {
        Validate.isTrue(getSpec().isServlet3Support());

        ClassFile configClass = new ClassFile(getSpec().getRootPackage(), "AppConfig");
        configClass.classAnnotations.add("@Configuration");
        configClass.imports.add("org.springframework.context.annotation.Configuration");
        configClass.classAnnotations.add("@EnableWebMvc");
        configClass.imports.add("org.springframework.web.servlet.config.annotation.EnableWebMvc");
        configClass.classAnnotations.add("@ComponentScan(basePackages = \"" + getSpec().getRootPackage() + "\")");
        configClass.imports.add("org.springframework.context.annotation.ComponentScan");
        model.getClassFiles().add(configClass);
        
        ClassFile appInitializer = new ClassFile(getSpec().getRootPackage(), "AppInitializer");
        appInitializer.classNameTail = "extends AbstractAnnotationConfigDispatcherServletInitializer";
        appInitializer.imports.add("org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer");
        //avoid compile error "cannot access ServletException"
        //https://stackoverflow.com/questions/25220468/spring-security-cannot-access-servletexception
        model.getPom().getDependencies().add("javax.servlet/javax.servlet-api/3.0.1/provided");
        appInitializer.getBody().append(code(indent(TAB + TAB,
                "@Override",
                "protected Class[] getRootConfigClasses() {",
                TAB + "return new Class[] { " + configClass.className + ".class };",
                "}",
                "",
                "@Override",
                "protected Class[] getServletConfigClasses() {",
                TAB + "return null;",
                "}",
                "",
                "@Override",
                "protected String[] getServletMappings() {",
                TAB + "return new String[] { \"/\" };",
                "}"
                )));
        model.getClassFiles().add(appInitializer);
        
        Map<String, String> props = new HashMap<>();
        props.put("package", getSpec().getRootPackage());
        ClassFile userClass = new TemplateClass(getSpec().getRootPackage(), "User", "java/webapp/UserPojo.java", props);
        model.getClassFiles().add(userClass);
        
        ClassFile controller = new ClassFile(getSpec().getRootPackage(), "UserRestController");
        controller.classAnnotations.add("@RestController");
        controller.imports.add("org.springframework.web.bind.annotation.RestController");
        controller.getBody().append(code(indent(TAB,
                //"@GetMapping(\"/user\")",
                "@GetMapping(name = \"/user\", produces = {MediaType.APPLICATION_JSON_VALUE})",
                "public User getUser() {",
                TAB + "System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));",
                TAB + "User user = new User();",
                TAB + "user.setFirstName(\"John\");",
                TAB + "user.setLastName(\"Doe\");",
                TAB + "return user;",
                "}"
                )));
        controller.imports.add("org.springframework.web.bind.annotation.GetMapping");
        controller.imports.add("org.springframework.http.MediaType"); //TODO
        controller.imports.add("java.util.Arrays");
        model.getPom().addDependency("com.fasterxml.jackson.core/jackson-databind/2.7.5"); //without this response will be generated in XML
        model.getLocalEndpoints().add("/user");
        model.getClassFiles().add(controller);
        
        addDirectDependencies();
    }
    
    protected void addDirectDependencies() {
        PreferenceConfig preferenceConfig = new PreferenceConfig();
        if (getSpec().getJersey() != null && getSpec().getJersey().getJerseyMajorVersion() == JerseyMajorVersion.V2) {
            preferenceConfig.setJaxRs(JaxRs.V2); //TODO
        }

        ArtifactSolver artifactSolver = new ArtifactSolver(preferenceConfig);
        Set<String> consolidatedImportedClassList = new HashSet<>();
        
        for (ClassFile clazz: model.getClassFiles()) {
            consolidatedImportedClassList.addAll(clazz.imports);
        }
        
        for (String absoluteClass: consolidatedImportedClassList) {
            if (absoluteClass.startsWith(getSpec().getRootPackage())) {
                continue; //resolve only third party classes
            }
            String artifact = artifactSolver.getArtifact(absoluteClass);
            if (artifact != null) {
                System.out.println(absoluteClass + ":" + artifact);
                if (!model.getPom().getDependencies().contains(artifact + "/provided")) {
                    model.getPom().getDependencies().add(artifact + "/provided");
                }
            }
        }
    }
    
    private void setupAuthRealm() {
        AuthRealm realm = new AuthRealm();

        Role readRole = new Role("app_read");
        Role editRole = new Role(ROLE);
        realm.getRoles().add(readRole);
        realm.getRoles().add(editRole);

        User admin = new User("admin", "adminpwd")
                .addRoles(readRole, editRole);
        User guest = new User("guest", "guestpwd")
                .addRoles(readRole);
        realm.getUsers().add(guest);
        realm.getUsers().add(admin);

        model.setAuthRealm(realm);
    }
}
