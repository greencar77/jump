package greencar77.jump.spec.java;

public enum SpringBootVersion {
    V1_4_7("1.4.7.RELEASE"),
    V1_5_7("1.5.7.RELEASE");
    
    private String versionString;
    
    private SpringBootVersion(String versionString) {
        this.versionString = versionString;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }
}
