package greencar77.jump.builder.java;

import java.util.HashMap;
import java.util.Map;

public class ArtifactSolver {
    private static final Map<String, String> MAP = new HashMap<>();
    
    static {
        //MAP.put("javax.ws.rs.GET", "javax.ws.rs/jsr311-api/1.1.1");
    }
    
    public String getArtifact(String absoluteClass) {
        if (absoluteClass.startsWith("org.springframework.web.servlet.")) {
            return "org.springframework/spring-webmvc/4.3.0.RELEASE";
        }
        if (absoluteClass.startsWith("org.springframework.context.")) {
            return "org.springframework/spring-context/4.3.0.RELEASE";
        }
        if (absoluteClass.startsWith("javax.ws.rs.")) {
            return "javax.ws.rs/jsr311-api/1.1.1";
        }
        
        if (MAP.containsKey(absoluteClass)) {
            return MAP.get(absoluteClass);
        }
        
        return null;
    }
}
