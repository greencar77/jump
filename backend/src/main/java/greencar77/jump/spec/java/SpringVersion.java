package greencar77.jump.spec.java;

public enum SpringVersion implements VersionProvider {
    V4_3_0("4.3.0.RELEASE", "4.3"),
    V4_3_4("4.3.4.RELEASE", "4.3"),
    V4_3_11("4.3.11.RELEASE", "4.3");

    private String versionString;
    private String xmlSchemaVersion; //Look into http://www.springframework.org/schema/context/

    private SpringVersion(String versionString, String xmlSchemaVersion) {
        this.versionString = versionString;
        this.xmlSchemaVersion = xmlSchemaVersion;
    }

    @Override
    public String getVersionString() {
        return versionString;
    }

    public String getXmlSchemaVersion() {
        return xmlSchemaVersion;
    }
}
