package greencar77.jump.model.java.maven;

import java.util.Set;
import java.util.TreeSet;

import org.jsoup.helper.Validate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Dependency {
    public static final String SEPARATOR = "/";
    
    private String groupId;
    private String artifactId;
    private String version;
    private DependencyScope scope;

    private Set<String> usedClasses = new TreeSet<>();

    public Dependency(String name) {
        this(name, DependencyScope.COMPILE);
    }

    public Dependency(String name, DependencyScope scope) {
        Validate.notNull(name);
        Validate.notNull(scope);

        String parts[] = name.split("/");
        groupId = parts[0];
        artifactId = parts[1];
        if (parts.length > 2) {
            version = parts[2];
        }
        this.scope = scope;
    }
    
    @JsonIgnore
    public String getNameTriplet() {
        return groupId + SEPARATOR + artifactId + (version == null? "" : SEPARATOR + version);
    }
    public DependencyScope getScope() {
        return scope;
    }
    public void setScope(DependencyScope scope) {
        this.scope = scope;
    }
    
    @JsonIgnore
    public String getFullName() {
        return getNameTriplet() + SEPARATOR + scope.getXmlTitle();
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

    public Set<String> getUsedClasses() {
        return usedClasses;
    }
}
