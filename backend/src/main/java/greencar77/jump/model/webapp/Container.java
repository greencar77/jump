package greencar77.jump.model.webapp;

public enum Container {
    WILDFLY(8080),
    TOMCAT(8080);
    
    private int defaultPort;
    
    private Container(int defaultPort) {
        this.defaultPort = defaultPort;
    }

    public int getDefaultPort() {
        return defaultPort;
    }
}
