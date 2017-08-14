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
import greencar77.jump.model.java.maven.PluginPom;
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

    public WebAppModel buildWebappSimple() {
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

//        clazz = new TemplateClass(spec.getRootPackage(), "UserResource", "java/webapp/UserResource.java", props);
//        model.getClassFiles().add(clazz);
        
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

    public WebAppModel buildWebappFirst() {
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
    
    public WebAppModel buildWebappAngular() {
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

    public WebAppModel buildSpring4RestTomcat() {
        WebAppSpec spec = new WebAppSpec();
        spec.setGroupId("x.y");
        spec.setArtifactId("spring4RestTomcat");
        spec.setRootPackage(spec.getGroupId());
        
        spec.setTargetContainer(Container.TOMCAT);
        spec.setServlet3Support(true);

        setSpec(spec);

        build();

        //http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/
        if (getSpec().isServlet3Support()) {
            //web.xml is not required
            PluginPom warPlugin = new PluginPom("org.apache.maven.plugins", "maven-war-plugin", "3.1.0");
            warPlugin.configuration.append(code(indent(TAB + TAB + TAB + TAB + TAB,
                    "<failOnMissingWebXml>false</failOnMissingWebXml>")));
            model.getPom().getBuild().addPlugin(warPlugin);
        } else {
            //TODO generate web.xml
        }

        buildAppSimple();

        return model;        
    }
}
