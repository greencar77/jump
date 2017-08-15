package greencar77.jump.builder.webapp;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import greencar77.jump.builder.java.MavenProjBuilder;
import greencar77.jump.model.java.maven.PluginPom;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.model.java.maven.WebDescriptor;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.webapp.WebAppSpec;

public class WebAppBuilder<S extends MavenProjSpec, M> extends MavenProjBuilder<WebAppSpec, WebAppModel> {
    
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

        return model;
    }

    protected void setupWarRestEasy() {
        Pom pom = model.getPom();
        
        //maven-war-plugin requires web.xml
        WebDescriptor webDescriptor = new WebDescriptor();
        webDescriptor.contextParams.put("resteasy.scan", "true");
        webDescriptor.contextParams.put("resteasy.servlet.mapping.prefix", "/rest");

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

    protected void setupWarTomcat() {
        Pom pom = model.getPom();
        
        //maven-war-plugin requires web.xml
        WebDescriptor webDescriptor = new WebDescriptor();
        //TODO jersey servlets
        webDescriptor.registerServletThirdParty("com.sun.jersey.spi.container.servlet.ServletContainer", "jersey-servlet", "/rest/*", null);
        model.getPom().getDependencies().add("com.sun.jersey/jersey-servlet/1.18.1");
        
        model.setWebDescriptor(webDescriptor);
        
        //pom.addDependency("javax.servlet/servlet-api/2.5/provided");
    }
    
    protected void setupWar() {
        switch (model.getTargetContainer()) {
            case WILDFLY:
                setupWarRestEasy();
                break;
            case TOMCAT:
                setupWarTomcat();
                break;
            default:
                throw new RuntimeException(model.getTargetContainer().name());
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
}
