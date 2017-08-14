package greencar77.jump.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //TODO remove
public class Spec {
    private String projectName;
    private Platform platform;
    private Javascript javascript;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Javascript getJavascript() {
        return javascript;
    }

    public void setJavascript(Javascript javascript) {
        this.javascript = javascript;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Spec [projectName=");
        builder.append(projectName);
        builder.append(", platform=");
        builder.append(platform);
        builder.append(", javascript=");
        builder.append(javascript);
        builder.append("]");
        return builder.toString();
    }

}
