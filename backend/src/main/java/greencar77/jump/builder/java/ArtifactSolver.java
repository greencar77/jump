package greencar77.jump.builder.java;

import java.util.HashMap;
import java.util.Map;

public class ArtifactSolver {
    private static final Map<String, String> MAP = new HashMap<>();
    
    static {
        MAP.put("javax.ws.rs.GET", "javax.ws.rs/jsr311-api/1.1.1");
    }
    
    public String getArtifact(String absoluteClass) {
        if (MAP.containsKey(absoluteClass)) {
            return MAP.get(absoluteClass);
        }
        
        return null;
    }
}
