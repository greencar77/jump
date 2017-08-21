package greencar77.jump.model.webapp;

public enum WebFramework {
    JERSEY("Jersey"),
    RESTEASY("RestEasy"),
    SPRING_MVC("Spring MVC"),
    ;
    
    private String title;
    
    private WebFramework(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
