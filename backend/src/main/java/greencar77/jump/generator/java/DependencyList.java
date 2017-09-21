package greencar77.jump.generator.java;

import java.util.Collection;

import greencar77.jump.model.java.maven.Dependency;

public class DependencyList {
    private Collection<Dependency> dependencies;

    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Collection<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
}
