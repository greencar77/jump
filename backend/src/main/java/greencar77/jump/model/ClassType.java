package greencar77.jump.model;

public enum ClassType {
    SOURCE("src/main/java/"),
    TEST("src/test/java/");
    
    private ClassType(String innerPath) {
        this.innerPath = innerPath;
    }
    
    private String innerPath;

    public String getInnerPath() {
        return innerPath;
    }
}
