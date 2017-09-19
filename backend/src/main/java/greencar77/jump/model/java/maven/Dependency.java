package greencar77.jump.model.java.maven;

import org.jsoup.helper.Validate;

public class Dependency {
    
    private String name;
    private DependencyScope scope;
    
    private String groupId;
    private String artifactId;
    private String version;

    public Dependency(String name) {
        this(name, DependencyScope.COMPILE);
    }

    public Dependency(String name, DependencyScope scope) {
        super();
        Validate.notNull(name);
        this.name = name;
        this.scope = scope;

        String parts[] = name.split("/");
        groupId = parts[0];
        artifactId = parts[1];
        if (parts.length > 2) {
            version = parts[2];
        }
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public DependencyScope getScope() {
        return scope;
    }
    public void setScope(DependencyScope scope) {
        this.scope = scope;
    }
    
    public String getFullName() {
        return name + "/" + scope.getXmlTitle();
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    /*
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Dependency)) {
            return false;
        }

        Dependency other = (Dependency) object;        
        return this.name.equals(other.name);
    }*/
}
