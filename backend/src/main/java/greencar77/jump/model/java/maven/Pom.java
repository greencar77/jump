package greencar77.jump.model.java.maven;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Pom {
    private String groupId;
    private String artifactId;
    private String packaging = "jar";
    private Dependency parent;
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
    
    public void addDependencyImported(String nameTriplet, String className) {
        Dependency dependency = getDependency(nameTriplet);
        if (dependency == null) {
            dependency = new Dependency(nameTriplet, DependencyScope.PROVIDED); //the minimum required
            dependencies.put(dependency.getNameTriplet(), dependency);
        } else {
            if (!dependency.getScope().isCompileAvailable()) {
                dependency.setScope(DependencyScope.ANY);
            } //else we already have what do we want
        }

        if (className != null) {
            dependency.getUsedClasses().add(className);
        }
    }
    
    public void addDependencyRuntime(String nameTriplet, String className) {
        Dependency dependency = getDependency(nameTriplet);
        if (dependency == null) {
            dependency = new Dependency(nameTriplet, DependencyScope.RUNTIME); //the minimum required
            dependencies.put(dependency.getNameTriplet(), dependency);
        } else {
            if (!dependency.getScope().isRuntimeAvailable()) {
                dependency.setScope(DependencyScope.ANY);
            } //else we already have what do we want
        }

        if (className != null) {
            dependency.getUsedClasses().add(className);
        }
    }
    
    public void addDependencyTesting(String nameTriplet, String className) {
        Dependency dependency = getDependency(nameTriplet);
        if (dependency == null) {
            dependency = new Dependency(nameTriplet, DependencyScope.TEST); //the minimum required
            dependencies.put(dependency.getNameTriplet(), dependency);
        } else {
            if (!dependency.getScope().isTestAvailable()) {
                dependency.setScope(DependencyScope.ANY);
            } //else we already have what do we want
        }

        if (className != null) {
            dependency.getUsedClasses().add(className);
        }
    }

    public Dependency getDependency(String name) {
        if (dependencies.containsKey(name)) {
            return dependencies.get(name);
        } else {
            return null;
        }
    }

    public Dependency getParent() {
        return parent;
    }

    public void setParent(Dependency parent) {
        this.parent = parent;
    }
}
