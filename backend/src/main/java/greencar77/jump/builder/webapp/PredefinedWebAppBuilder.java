package greencar77.jump.builder.webapp;

import java.util.HashMap;
import java.util.Map;

import greencar77.jump.builder.Predefined;
import greencar77.jump.builder.js.PredefinedAngularAppBuilder;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.TemplateClass;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.model.webapp.Container;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.model.webapp.WebFramework;
import greencar77.jump.spec.webapp.Jersey;
import greencar77.jump.spec.webapp.JerseyMajorVersion;
import greencar77.jump.spec.webapp.WebAppSpec;

public class PredefinedWebAppBuilder extends WebAppBuilder<WebAppSpec, WebAppModel> implements Predefined<WebAppModel> {

    private PredefinedAngularAppBuilder angularAppBuilder = new PredefinedAngularAppBuilder();

    public PredefinedWebAppBuilder() {}

    public PredefinedWebAppBuilder(WebAppSpec spec) {
        super(spec);
    }
    
    @Override
    public WebAppModel build(String specId) {        
        return (WebAppModel) generateModel(specId);
    }

    public WebAppModel specWebappSimpleTomcat() {
        WebAppSpec spec = new WebAppSpec();
        spec.setProjectName("webappSimpleTomcat");
        spec.setGroupId("x.y");
        spec.setArtifactId("webappSimpleTomcat");
        spec.setRootPackage(spec.getGroupId());
        
        spec.setTargetContainer(Container.TOMCAT);
        spec.setServlet3Support(false);
        spec.setWebFramework(WebFramework.JERSEY);
        spec.setAppGenerator("buildAppSimple");

        setSpec(spec);

        build();
        
        return model;
    }

    public WebAppModel specWebappSimpleWildfly() {
        WebAppSpec spec = new WebAppSpec();
        spec.setProjectName("webappSimpleWildfly");
        spec.setGroupId("x.y");
        spec.setArtifactId("webappSimpleWildfly");
        spec.setRootPackage(spec.getGroupId());

        spec.setTargetContainer(Container.WILDFLY);
        spec.setServlet3Support(true);
        spec.setWebFramework(WebFramework.SPRING_MVC);
        spec.setAppGenerator("buildAppSimpleSpring");

        setSpec(spec);

        build();

        return model;
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
        model.setAngularApp(angularAppBuilder.specTutti());

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
        spec.setProjectName("spring4RestTomcat");
        spec.setGroupId("x.y");
        spec.setArtifactId("spring4RestTomcat");
        spec.setRootPackage(spec.getGroupId());
        
        spec.setTargetContainer(Container.TOMCAT);
        spec.setServlet3Support(true);
        spec.setWebFramework(WebFramework.SPRING_MVC);
        spec.setAppGenerator("buildAppSimpleSpring");

        setSpec(spec);

        build();

        return model;
    }

    public WebAppModel specSpring4RestWildfly() {
        WebAppSpec spec = new WebAppSpec();
        spec.setProjectName("spring4RestWildfly");
        spec.setGroupId("x.y");
        spec.setArtifactId("spring4RestWildfly");
        spec.setRootPackage(spec.getGroupId());
        
        spec.setTargetContainer(Container.WILDFLY);
        spec.setServlet3Support(true);
        spec.setWebFramework(WebFramework.SPRING_MVC);
        spec.setAppGenerator("buildAppSimpleSpring");

        setSpec(spec);

        build();

        return model;
    }

    public WebAppModel specWebappTomcatAuth() {
        WebAppSpec spec = new WebAppSpec();
        spec.setProjectName("webappTomcatAuth");
        spec.setGroupId("x.y");
        spec.setArtifactId("webappTomcatAuth");
        spec.setRootPackage(spec.getGroupId());

        spec.setTargetContainer(Container.TOMCAT);
        spec.setServlet3Support(false);
        spec.setWebFramework(WebFramework.JERSEY);
        Jersey jersey = new Jersey();
        jersey.setJerseyMajorVersion(JerseyMajorVersion.V1);
        jersey.setJerseyVersion("1.19.4");
        spec.setJersey(jersey);
        spec.setAppGenerator("buildAppSimple");
        spec.setAuthenticate(true);

        setSpec(spec);

        build();

        return model;
    }

    public WebAppModel specWebappTomcatAuthJersey2() {
        WebAppSpec spec = new WebAppSpec();
        spec.setProjectName("webappTomcatAuthJersey2");
        spec.setGroupId("x.y");
        spec.setArtifactId("webappTomcatAuthJersey2");
        spec.setRootPackage(spec.getGroupId());

        spec.setTargetContainer(Container.TOMCAT);
        spec.setServlet3Support(false);
        spec.setWebFramework(WebFramework.JERSEY);
        Jersey jersey = new Jersey();
        jersey.setJerseyMajorVersion(JerseyMajorVersion.V2);
        jersey.setJerseyVersion("2.25.1");
        spec.setJersey(jersey);
        spec.setSslRestricted(true);
        spec.setAppGenerator("buildAppSimple");
        spec.setAuthenticate(true);

        setSpec(spec);

        build();

        return model;
    }
}
