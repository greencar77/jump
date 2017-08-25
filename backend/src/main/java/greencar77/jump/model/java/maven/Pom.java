package greencar77.jump.model.java.maven;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Pom {
    private String groupId;
    private String artifactId;
    private String packaging = "jar";
    protected Map<String, Dependency> dependencies = new HashMap<>();
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

    public Collection<Dependency> getDependencies() {
        return dependencies.values();
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
    
    public void addDependencyImported(String name) {
        Dependency dependency = getDependency(name);
        if (dependency == null) {
            Dependency newDependency = new Dependency(name, DependencyScope.PROVIDED); //the minimum required
            dependencies.put(newDependency.getName(), newDependency);
        } else {
            if (!dependency.getScope().isCompileAvailable()) {
                dependency.setScope(DependencyScope.ANY);
            } //else we already have what do we want
        }
    }
    
    public void addDependencyRuntime(String name) {
        Dependency dependency = getDependency(name);
        if (dependency == null) {
            Dependency newDependency = new Dependency(name, DependencyScope.RUNTIME); //the minimum required
            dependencies.put(newDependency.getName(), newDependency);
        } else {
            if (!dependency.getScope().isRuntimeAvailable()) {
                dependency.setScope(DependencyScope.ANY);
            } //else we already have what do we want
        }
    }

    public Dependency getDependency(String name) {
        if (dependencies.containsKey(name)) {
            return dependencies.get(name);
        } else {
            return null;
        }
    }
}
