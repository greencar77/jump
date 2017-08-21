package greencar77.jump.spec.webapp;

import greencar77.jump.model.webapp.Container;
import greencar77.jump.model.webapp.WebFramework;
import greencar77.jump.spec.java.MavenProjSpec;

public class WebAppSpec extends MavenProjSpec {
    
    private Container targetContainer = Container.WILDFLY;    
    private boolean servlet3Support;
    private WebFramework webFramework;

    public boolean isServlet3Support() {
        return servlet3Support;
    }

    public void setServlet3Support(boolean servlet3Support) {
        this.servlet3Support = servlet3Support;
    }

    public Container getTargetContainer() {
        return targetContainer;
    }

    public void setTargetContainer(Container targetContainer) {
        this.targetContainer = targetContainer;
    }

    public WebFramework getWebFramework() {
        return webFramework;
    }

    public void setWebFramework(WebFramework webFramework) {
        this.webFramework = webFramework;
    }
}
