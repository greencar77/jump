package greencar77.jump.spec.java;

public enum SpringBootVersion implements VersionProvider {
    V1_4_2("1.4.2.RELEASE", "4.3.4.RELEASE"),
    V1_4_7("1.4.7.RELEASE", "4.3.9.RELEASE"),
    V1_5_7("1.5.7.RELEASE", "4.3.11.RELEASE");
    
    private String versionString;
    private String spring;
    
    private SpringBootVersion(String versionString, String spring) {
        this.versionString = versionString;
        this.spring = spring;
    }

    @Override
    public String getVersionString() {
        return versionString;
    }

    public String getSpring() {
        return spring;
    }
}
