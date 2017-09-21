package greencar77.jump.spec.java;

public enum SpringVersion implements VersionProvider {
    V4_3_0("4.3.0.RELEASE"),
    V4_3_11("4.3.11.RELEASE");

    private String versionString;

    private SpringVersion(String versionString) {
        this.versionString = versionString;
    }

    @Override
    public String getVersionString() {
        return versionString;
    }
}
