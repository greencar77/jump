package greencar77.jump.spec.java;

import greencar77.jump.spec.Spec;

public class MavenProjSpec extends Spec {
    private String groupId;
    private String artifactId;
    private String rootPackage = "x.y";
    private String appGenerator;

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getAppGenerator() {
        return appGenerator;
    }

    public void setAppGenerator(String appGenerator) {
        this.appGenerator = appGenerator;
    }
}
