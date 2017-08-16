package greencar77.jump.builder.webapp;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import greencar77.jump.builder.Predefined;
import greencar77.jump.builder.java.ArtifactSolver;
import greencar77.jump.builder.js.PredefinedAngularAppBuilder;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.classfile.RestMethod;
import greencar77.jump.model.java.classfile.TemplateClass;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.model.webapp.Container;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.spec.webapp.WebAppSpec;

public class PredefinedWebAppBuilder extends WebAppBuilder<WebAppSpec, WebAppModel> implements Predefined<WebAppModel> {

    private PredefinedAngularAppBuilder angularAppBuilder = new PredefinedAngularAppBuilder();
    private ArtifactSolver artifactSolver = new ArtifactSolver();

    public PredefinedWebAppBuilder() {}

    public PredefinedWebAppBuilder(WebAppSpec spec) {
        super(spec);
    }
    
    @Override
    public WebAppModel build(String specId) {        
        return (WebAppModel) generateModel(specId);
    }

    public WebAppModel specWebappSimple() {
        String warFilename = "webappSimple"; //will be used in url
        
        //config project
        Pom pom = new Pom("x.y", "webx");
        model.setPom(pom);
        model.setTargetContainer(Container.TOMCAT);
        
        model.getPom().addDependency("com.sun.jersey/jersey-server/1.18.1");
        model.getPom().addDependency("com.sun.jersey/jersey-bundle/1.18.1");
        /*
otherwise
aug. 08, 2017 4:10:02 PM org.apache.catalina.core.StandardWrapperValve invoke
SEVERE: Allocate exception for servlet jersey-servlet
java.lang.NoSuchMethodError: com.sun.jersey.core.reflection.ReflectionHelper.getContextClassLoaderPA()Ljava/security/PrivilegedAction;
         */

        buildAppSimple();

        setupWar();
        
        return model;
    }
    
    private void buildAppSimple() {
        
        Map<String, String> props = new HashMap<>();
        props.put("package", getSpec().getRootPackage());
        ClassFile userClass = new TemplateClass(getSpec().getRootPackage(), "User", "java/webapp/UserPojo.java", props);
        model.getClassFiles().add(userClass);
        
        RestClassFile restClass = new RestClassFile(getSpec().getRootPackage(), "Alpha");
        restClass.setPath("/alphanode");
        RestMethod method = new RestMethod("oho");
        method.classAnnotations.add("@GET");
        restClass.imports.add("javax.ws.rs.GET");
        method.classAnnotations.add("@Produces(MediaType.APPLICATION_JSON)");
        model.getPom().getDependencies().add("com.sun.jersey/jersey-json/1.8");
        /*
         * otherwise
aug. 08, 2017 4:05:39 PM com.sun.jersey.spi.container.ContainerResponse logException
SEVERE: Mapped exception to response: 500 (Internal Server Error)
javax.ws.rs.WebApplicationException: com.sun.jersey.api.MessageException: A message body writer for Java class com.x.y.User, and Java type class com.x.y.User, and MIME media type application/json was not found.
         */
        restClass.imports.add("javax.ws.rs.Produces");
        restClass.imports.add("javax.ws.rs.core.MediaType");
        method.getContent().append(code(indent(TAB + TAB,
                "User user = new User();",
                "user.setFirstName(\"JonFromREST\");",
                "user.setLastName(\"DoeFromREST\");",
                "return user;"
                )));
        method.setReturnType("User");
        restClass.imports.add(userClass.getFullName());

        restClass.getMethods().add(method);
        model.getRestClasses().add(restClass);
        model.getClassFiles().add(restClass);
        
        addDirectDependencies();
    }
    
    private void buildAppSimpleSpring() {
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
                TAB + "User user = new User();",
                TAB + "user.setFirstName(\"Jon\");",
                TAB + "user.setLastName(\"Doe\");",
                TAB + "return user;",
                "}"
                )));
        controller.imports.add("org.springframework.web.bind.annotation.GetMapping");
        controller.imports.add("org.springframework.http.MediaType"); //TODO
        model.getPom().addDependency("com.fasterxml.jackson.core/jackson-databind/2.7.5"); //without this response will be generated in XML
        model.getLocalEndpoints().add("/user");
        model.getClassFiles().add(controller);
        
        addDirectDependencies();
    }
    
    protected void addDirectDependencies() {
        Set<String> absoluteClasses = new HashSet<>();
        
        for (ClassFile clazz: model.getClassFiles()) {
            absoluteClasses.addAll(clazz.imports);
        }
        
        for (String absoluteClass: absoluteClasses) {
            String artifact = artifactSolver.getArtifact(absoluteClass);
            if (artifact != null) {
                if (!model.getPom().getDependencies().contains(artifact)) {
                    System.out.println(absoluteClass + ":" + artifact);
                    model.getPom().getDependencies().add(artifact);
                }
            }
        }
    }

    public WebAppModel specWebappFirst() {
        String rootPackage = "x.y";
        
        //setup business files
        Map<String, String> props = new HashMap<>();
        props.put("package", rootPackage);
        ClassFile clazz = new TemplateClass(rootPackage, "User", "java/webapp/UserPojo.java", props);
        model.getClassFiles().add(clazz);

        clazz = new TemplateClass(rootPackage, "UserResource", "java/webapp/UserResource.java", props);
        model.getClassFiles().add(clazz);

        //config project
        Pom pom = new Pom("x.y", "webx");
        model.setPom(pom);
        model.setTargetContainer(Container.TOMCAT);
        
        pom.setPackaging("war"); //TODO enum
        pom.getBuild().finalName = rootPackage; //this name will be used as build (war) file name
        
        setupWar();
        
        return model;
    }
    
    public WebAppModel specWebappAngular() {
        String rootPackage = "x.y";
        
        //setup business files
        Map<String, String> props = new HashMap<>();
        props.put("package", rootPackage);
        ClassFile clazz = new TemplateClass(rootPackage, "User", "java/webapp/UserPojo.java", props);
        model.getClassFiles().add(clazz);

        clazz = new TemplateClass(rootPackage, "UserResource", "java/webapp/UserResource.java", props);
        model.getClassFiles().add(clazz);
        
        //Angular app
        model.setAngularApp(angularAppBuilder.buildTutti());

        //config project
        Pom pom = new Pom("x.y", "webx");
        model.setPom(pom);        
        
        pom.setPackaging("war"); //TODO enum
        pom.getBuild().finalName = rootPackage; //this name will be used as build (war) file name
        
        setupWar();
        
        return model;
    }

    public WebAppModel specSpring4RestTomcat() {
        //http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/
        WebAppSpec spec = new WebAppSpec();
        spec.setGroupId("x.y");
        spec.setArtifactId("spring4RestTomcat");
        spec.setRootPackage(spec.getGroupId());
        
        spec.setTargetContainer(Container.TOMCAT);
        spec.setServlet3Support(true);

        setSpec(spec);

        build();

        buildAppSimpleSpring();

        return model;
    }

    public WebAppModel specSpring4RestWildfly() {
        //http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/
        WebAppSpec spec = new WebAppSpec();
        spec.setGroupId("x.y");
        spec.setArtifactId("spring4RestWildfly");
        spec.setRootPackage(spec.getGroupId());
        
        spec.setTargetContainer(Container.WILDFLY);
        spec.setServlet3Support(true);

        setSpec(spec);

        build();

        buildAppSimpleSpring();

        return model;
    }
}
