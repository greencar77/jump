package greencar77.jump.spec.webapp;

import greencar77.jump.model.webapp.Container;
import greencar77.jump.model.webapp.WebFramework;
import greencar77.jump.spec.java.MavenProjSpec;

public class WebAppSpec extends MavenProjSpec {
    
    private Container targetContainer = Container.WILDFLY;    
    private boolean servlet3Support;
    private WebFramework webFramework;
    private Jersey jersey;
    private boolean authenticate;
    private Boolean sslRestricted;

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

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    public Jersey getJersey() {
        return jersey;
    }

    public void setJersey(Jersey jersey) {
        this.jersey = jersey;
    }

    public Boolean getSslRestricted() {
        return sslRestricted;
    }

    public void setSslRestricted(Boolean sslRestricted) {
        this.sslRestricted = sslRestricted;
    }
}
