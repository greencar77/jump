package greencar77.jump.model.java.maven;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Pom {
    private String groupId;
    private String artifactId;
    private String packaging = "jar";
    protected List<String> dependencies = new ArrayList<String>();
    private BuildPom build = new BuildPom();
    public Properties properties = new Properties();

    public Pom(String groupId, String artifactId) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
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

    public List<String> getDependencies() {
        return dependencies;
    }

    public void addDependency(String dependency) {
        dependencies.add(dependency);
    }

    public BuildPom getBuild() {
        return build;
    }

    public void setBuild(BuildPom build) {
        this.build = build;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

}
