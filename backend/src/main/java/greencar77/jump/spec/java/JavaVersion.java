package greencar77.jump.spec.java;

public enum JavaVersion {
    V15("1.5"),
    V16("1.6"),
    V17("1.7"),
    V18("1.8");
    
    private String id;
    
    private JavaVersion(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
