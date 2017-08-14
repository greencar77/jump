package greencar77.jump.generator.webapp;

import java.util.Map;

import greencar77.jump.generator.java.MavenProjGenerator;
import greencar77.jump.generator.js.StandaloneAngularGenerator;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.maven.ServletMappingWebDescriptor;
import greencar77.jump.model.java.maven.ServletWebDescriptor;
import greencar77.jump.model.java.maven.WebDescriptor;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.spec.webapp.WebAppSpec;

public class WebAppGenerator extends MavenProjGenerator<WebAppModel> {
    private static final String WEBAPP_FOLDER = "/src/main/webapp";
    
    private StandaloneAngularGenerator<AngularAppModel> angularAppGenerator;
    
    private WebAppModel model;

    public WebAppGenerator(String projectFolder, WebAppModel model) {
        super(projectFolder, model);
        this.model = model;
    }

    @Override
    public void generate() {
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

        mavenBuild();
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

        sb.append("</web-app>" + LF);

        saveResource("src/main/webapp/WEB-INF/" + model.getWebDescriptor().getFilename(), sb.toString().getBytes());
    }
}
