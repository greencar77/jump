package greencar77.jump.model.java.maven;

import greencar77.jump.generator.Generator;

public class PluginPom {
    private String groupId;
    private String artifactId;
    private String version;
    public StringBuilder configuration = new StringBuilder();

    public PluginPom(String groupId, String artifactId, String version) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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

    public void setVersion(String version) {
        this.version = version;
    }

    public String getShortName() {
        return groupId + "/" + artifactId;
    }

    //@Override
    public StringBuilder generateContent(String offset) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(offset + "<plugin>" + Generator.LF);
        sb.append(offset + Generator.TAB + "<groupId>" + groupId + "</groupId>" + Generator.LF);
        sb.append(offset + Generator.TAB + "<artifactId>" + artifactId + "</artifactId>" + Generator.LF);
        if (version != null) {
            sb.append(offset + Generator.TAB + "<version>" + version + "</version>" + Generator.LF);
        }
        if (configuration.length() > 0) {
            sb.append(offset + Generator.TAB + "<configuration>" + Generator.LF);
            sb.append(getConfiguration());
            sb.append(offset + Generator.TAB + "</configuration>" + Generator.LF);
        }
        sb.append(getExecutions(offset + Generator.TAB));
        sb.append(offset + "</plugin>" + Generator.LF);
        
        return sb;
    }
    
    protected StringBuilder getExecutions(String offset) {
        return new StringBuilder();
    }
    
    protected StringBuilder getConfiguration() {
        return configuration;
    }

}
