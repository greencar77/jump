package greencar77.jump.generator.webapp;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;

import greencar77.jump.generator.java.MavenProjGenerator;
import greencar77.jump.generator.js.StandaloneAngularGenerator;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.maven.ServletMappingWebDescriptor;
import greencar77.jump.model.java.maven.ServletWebDescriptor;
import greencar77.jump.model.java.maven.WebDescriptor;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.model.webapp.WebFramework;
import greencar77.jump.model.webapp.auth.Role;
import greencar77.jump.model.webapp.auth.User;

public class WebAppGenerator extends MavenProjGenerator<WebAppModel> {
    private static final String WEBAPP_FOLDER = "/src/main/webapp";
    
    private StandaloneAngularGenerator<AngularAppModel> angularAppGenerator;
    
    private WebAppModel model;

    public WebAppGenerator(String projectFolder, WebAppModel model) {
        super(projectFolder, model);
        this.model = model;
    }

    @Override
    protected void generateContent() {
        if (model.getAngularApp() != null) {
            angularAppGenerator = new StandaloneAngularGenerator<>(projectFolder + WEBAPP_FOLDER, model.getAngularApp());
            angularAppGenerator.generate();
        }

        generateClassFiles();        

        for (RawFile rawFile: model.getRawFiles()) {
            saveResource(rawFile.getPath(), rawFile.getContent());
        }

        byte[] pom = generatePom();        
        saveResource("pom.xml", pom);
        
        if (model.getWebDescriptor() != null) {
            generateWebDescriptor();
        }
        
        if (model.getAuthRealm() != null) {
            generateUsers();
        }

        mavenBuild();
    }
    
    @Override
    protected void generateInstructions() {
        StringBuilder sb = new StringBuilder();

        sb.append("Preparation instructions:" + LF);

        switch (model.getTargetContainer()) {
            case TOMCAT:
                sb.append("xcopy /s /Y target\\*.war <TOMCAT_HOME>\\webapps" + LF);
                sb.append("xcopy /s /Y tomcat\\conf\\" + projectFolder + "-" + "tomcat-users.xml <TOMCAT_HOME>\\conf" + LF);
                sb.append("assert: " + "<TOMCAT_HOME>\\conf\\server.xml" + " is updated to contain data of " + "tomcat\\conf\\" + "server.xml" + LF);
                break;
            case WILDFLY:
                sb.append("xcopy /s /Y target\\*.war <WILDFLY_HOME>\\standalone\\deployments" + LF);
                break;
            default: throw new IllegalArgumentException(model.getTargetContainer().name());
        }

        sb.append("assert: " + model.getTargetContainer() + " instance is running" + LF);

        String servletMappingPrefix = model.getServletMappingPrefix() == null? "" : model.getServletMappingPrefix();
        if (model.getLocalEndpoints().size() > 0) {
            sb.append(LF);
            sb.append("Some endpoints:" + LF);
            for (String endpoint: model.getLocalEndpoints()) {
                sb.append("http://localhost:" + model.getTargetContainer().getDefaultPort() + "/" + model.getPom().getBuild().finalName + servletMappingPrefix + endpoint + LF);
            }
        }
        
        Validate.notNull(model.getWebFramework());
        sb.append(LF);
        sb.append("Web framework: " + model.getWebFramework().getTitle());
        if (model.getWebFramework() == WebFramework.JERSEY) {
            sb.append(" " + model.getJerseyVersion());
        }
        sb.append(LF);

        sb.append(LF);
        sb.append("Security: " + (model.getAuthRealm() == null? "Application is not secured": "Authentication is needed") + LF);

        sb.append(LF);
        sb.append("==Restlet==" + LF);
        sb.append("Create project: " + projectFolder + LF);

        saveResource(INSTRUCTIONS_FILENAME, sb.toString().getBytes());
    }

    public void generateWebDescriptor() {
        StringBuilder sb = new StringBuilder();
        
        WebDescriptor webDescriptor = model.getWebDescriptor();
        
        sb.append("<!DOCTYPE web-app PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\" \"http://java.sun.com/dtd/web-app_2_3.dtd\" >" + LF);
        sb.append("<web-app>" + LF);

        if (webDescriptor.contextParams.size() > 0) {
            for (Map.Entry<String, String> entry: webDescriptor.contextParams.entrySet()) {
                sb.append(TAB + "<context-param>" + LF);
                sb.append(TAB + TAB + "<param-name>" + entry.getKey() + "</param-name>" + LF);
                sb.append(TAB + TAB + "<param-value>" + entry.getValue() + "</param-value>" + LF);
                sb.append(TAB + "</context-param>" + LF);
            }
        }

        if (webDescriptor.listeners.size() > 0) {
            sb.append(TAB + "<listener>" + LF);
            for (String listenerClass: webDescriptor.listeners) {
                sb.append(TAB + TAB + "<listener-class>" + listenerClass + "</listener-class>" + LF);
            }
            sb.append(TAB + "</listener>" + LF);
        }
        
        if (webDescriptor.servlets.size() > 0) {
            sb.append(TAB + "<!--servlets-->" + LF);
            for (ServletWebDescriptor servlet: webDescriptor.servlets) {
                sb.append(servlet.generateContent(TAB));
            }
        }
        
        if (webDescriptor.servletMappings.size() > 0) {
            sb.append(TAB + "<!--servlet mappings-->" + LF);
            for (ServletMappingWebDescriptor servletMapping: webDescriptor.servletMappings) {
                sb.append(servletMapping.generateContent(TAB));
            }
        }
        
        sb.append(webDescriptor.getSecuritySection());

        sb.append("</web-app>" + LF);

        saveResource("src/main/webapp/WEB-INF/" + model.getWebDescriptor().getFilename(), sb.toString().getBytes());
    }

    private void generateUsers() {
        switch (model.getTargetContainer()) {
        case TOMCAT:
            generateUsersTomcat();
            generateTomcatServerConf();
            break;
        default: throw new IllegalArgumentException(model.getTargetContainer().name());
        }
    }

    private void generateUsersTomcat() {
        //http://www.avajava.com/tutorials/lessons/how-do-i-use-basic-authentication-with-tomcat.html
        StringBuilder sb = new StringBuilder();
        //TODO persistent order

        sb.append("<?xml version='1.0' encoding='utf-8'?>" + LF);
        sb.append("<tomcat-users>" + LF);

        for (Role role: model.getAuthRealm().getRoles()) {
            sb.append(TAB + "<role rolename=\"" + role.getName() + "\"/>" + LF);
        }

        for (User user: model.getAuthRealm().getUsers()) {
            sb.append(TAB + "<user username=\"" + user.getName() + "\" password=\"" + user.getPassword() + "\"");

            String roleString = user.getRoles()
            .stream()
            .map(r -> r.getName())
            .sorted()
            .collect(Collectors.toList())
                .stream()
                .collect(Collectors.joining(","));

            if (user.getRoles().size() > 0) {
                sb.append(" roles=\""
                        + roleString
                        + "\"");
            }
            sb.append("/>" + LF);
        }

        sb.append("</tomcat-users>" + LF);
        saveResource("tomcat/conf/" + projectFolder + "-" + "tomcat-users.xml", sb.toString().getBytes());
    }

    private void generateTomcatServerConf() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<?xml version='1.0' encoding='utf-8'?>" + LF);
        sb.append("<Server port=\"8005\" shutdown=\"SHUTDOWN\">" + LF);
        sb.append(TAB + "<GlobalNamingResources>" + LF);
        sb.append(TAB + TAB + "<Resource name=\"UserDatabase\" auth=\"Container\"" + LF);
        
        sb.append(TAB + TAB + TAB + "type=\"org.apache.catalina.UserDatabase\"" + LF);
        sb.append(TAB + TAB + TAB + "description=\"User database that can be updated and saved\"" + LF);
        sb.append(TAB + TAB + TAB + "factory=\"org.apache.catalina.users.MemoryUserDatabaseFactory\"" + LF);
        sb.append(TAB + TAB + TAB + "pathname=\"conf/" + projectFolder + "-" + "tomcat-users.xml" + "\" />" + LF);
        sb.append(TAB + "</GlobalNamingResources>" + LF);
        sb.append("</Server>" + LF);
        saveResource("tomcat/conf/" + "server.xml", sb.toString().getBytes());
       
    }
}
