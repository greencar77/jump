package greencar77.jump.model.webapp;

import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.webapp.auth.AuthRealm;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.maven.WebDescriptor;

public class WebAppModel extends MavenProjModel {
    private Container targetContainer = Container.WILDFLY;    
    private WebDescriptor webDescriptor;
    private Set<RestClassFile> restClasses = new HashSet<>();
    private Set<String> localEndpoints = new TreeSet<>();
    private String servletMappingPrefix;
    private WebFramework webFramework;
    private String jerseyVersion;
    private AuthRealm authRealm;
    
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

    public Set<String> getLocalEndpoints() {
        return localEndpoints;
    }

    public void setLocalEndpoints(Set<String> localEndpoints) {
        this.localEndpoints = localEndpoints;
    }

    public String getServletMappingPrefix() {
        return servletMappingPrefix;
    }

    public void setServletMappingPrefix(String servletMappingPrefix) {
        this.servletMappingPrefix = servletMappingPrefix;
    }

    public WebFramework getWebFramework() {
        return webFramework;
    }

    public void setWebFramework(WebFramework webFramework) {
        this.webFramework = webFramework;
    }

    public AuthRealm getAuthRealm() {
        return authRealm;
    }

    public void setAuthRealm(AuthRealm authRealm) {
        this.authRealm = authRealm;
    }

    public String getJerseyVersion() {
        return jerseyVersion;
    }

    public void setJerseyVersion(String jerseyVersion) {
        this.jerseyVersion = jerseyVersion;
    }
}
