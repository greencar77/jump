package greencar77.jump.spec.webapp;

public class Jersey {
    private JerseyMajorVersion jerseyMajorVersion = JerseyMajorVersion.V1;
    private String jerseyVersion = "1.19.4";

    public String getJerseyVersion() {
        return jerseyVersion;
    }

    public void setJerseyVersion(String jerseyVersion) {
        this.jerseyVersion = jerseyVersion;
    }

    public JerseyMajorVersion getJerseyMajorVersion() {
        return jerseyMajorVersion;
    }

    public void setJerseyMajorVersion(JerseyMajorVersion jerseyMajorVersion) {
        this.jerseyMajorVersion = jerseyMajorVersion;
    }
}
