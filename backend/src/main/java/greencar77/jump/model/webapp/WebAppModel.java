package greencar77.jump.model.webapp;

import greencar77.jump.model.js.AngularAppModel;

import java.util.HashSet;
import java.util.Set;

import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.maven.WebDescriptor;

public class WebAppModel extends MavenProjModel {
    private Container targetContainer = Container.WILDFLY;    
    private WebDescriptor webDescriptor;
    private Set<RestClassFile> restClasses = new HashSet<>();
    
    private AngularAppModel angularApp;

    public WebDescriptor getWebDescriptor() {
        return webDescriptor;
    }

    public void setWebDescriptor(WebDescriptor webDescriptor) {
        this.webDescriptor = webDescriptor;
    }

    public AngularAppModel getAngularApp() {
        return angularApp;
    }

    public void setAngularApp(AngularAppModel angularApp) {
        this.angularApp = angularApp;
    }

    public Container getTargetContainer() {
        return targetContainer;
    }

    public void setTargetContainer(Container targetContainer) {
        this.targetContainer = targetContainer;
    }

    public Set<RestClassFile> getRestClasses() {
        return restClasses;
    }
}
