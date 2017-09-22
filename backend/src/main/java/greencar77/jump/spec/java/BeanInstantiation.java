package greencar77.jump.spec.java;

public enum BeanInstantiation {
    XML(true, false),
    JAVA_CONFIG(false, true),
    JAVA_CONFIG_XML(true, true);

    private boolean requireXml;
    private boolean requireJavaConfig;

    private BeanInstantiation(boolean requireXml, boolean requireJavaConfig) {
        this.requireXml = requireXml;
        this.requireJavaConfig = requireJavaConfig;
    }

    public boolean isRequireXml() {
        return requireXml;
    }

    public boolean isRequireJavaConfig() {
        return requireJavaConfig;
    }
}
