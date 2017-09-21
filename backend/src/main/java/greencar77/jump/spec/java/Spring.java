package greencar77.jump.spec.java;

public class Spring {
    private SpringVersion version;
    private SpringConfigBasis configBasis;

    public SpringConfigBasis getConfigBasis() {
        return configBasis;
    }

    public void setConfigBasis(SpringConfigBasis configBasis) {
        this.configBasis = configBasis;
    }

    public SpringVersion getVersion() {
        return version;
    }

    public void setVersion(SpringVersion version) {
        this.version = version;
    }
}
