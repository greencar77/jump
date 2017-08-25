package greencar77.jump.model.java.maven;

public enum DependencyScope {
    //https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Dependency_Scope
    COMPILE("compile", true, true, true),
    PROVIDED("provided", true, false, false),
    RUNTIME("runtime", false, false, true),
    TEST("test", false, true, false);

    public static final DependencyScope ANY = COMPILE;

    private String xmlTitle;
    private boolean compileAvailable;
    private boolean testAvailable;
    private boolean runtimeAvailable;
    
    private DependencyScope(String xmlTitle, boolean compileAvailable, boolean testAvailable, boolean runtimeAvailable) {
        this.xmlTitle = xmlTitle;
        this.compileAvailable = compileAvailable;
        this.testAvailable = testAvailable;
        this.runtimeAvailable = runtimeAvailable;
    }
    public boolean isCompileAvailable() {
        return compileAvailable;
    }

    public boolean isTestAvailable() {
        return testAvailable;
    }

    public boolean isRuntimeAvailable() {
        return runtimeAvailable;
    }
    public String getXmlTitle() {
        return xmlTitle;
    }
}
