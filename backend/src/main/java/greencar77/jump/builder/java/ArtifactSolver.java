package greencar77.jump.builder.java;

import java.util.HashMap;
import java.util.Map;

public class ArtifactSolver {
    private static final String JDK = "JDK";
    private static final Map<String, String> MAP = new HashMap<>();
    
    private PreferenceConfig preferenceConfig;
    
    static {
        //MAP.put("javax.ws.rs.GET", "javax.ws.rs/jsr311-api/1.1.1");
    }
    
    public ArtifactSolver(PreferenceConfig preferenceConfig) {
        this.preferenceConfig = preferenceConfig;
    }

    public String getArtifact(String absoluteClass) {
        String artifact = getArtifactByClass(absoluteClass);
        return resolvePreferences(absoluteClass, artifact);
    }

    private String getArtifactByClass(String absoluteClass) {
        if (absoluteClass.startsWith("java.")) {
            return JDK;
        }
        if (absoluteClass.startsWith("org.springframework.web.servlet.")) {
            return "org.springframework/spring-webmvc/4.3.0.RELEASE";
        }
        if (absoluteClass.startsWith("org.springframework.context.")) {
            return "org.springframework/spring-context/4.3.0.RELEASE";
        }
        if (absoluteClass.startsWith("javax.ws.rs.")) {
            return "javax.ws.rs/jsr311-api/1.1.1";
        }
        if (absoluteClass.startsWith("org.apache.poi.xssf.usermodel.")) {
            return "org.apache.poi/poi-ooxml/3.15";
        }
        if (absoluteClass.startsWith("org.junit.")) {
            return "junit/junit/4.10";
        }
        if (absoluteClass.startsWith("com.rabbitmq.")) {
            return "com.rabbitmq/amqp-client/4.2.0";
        }
        
        if (MAP.containsKey(absoluteClass)) {
            return MAP.get(absoluteClass);
        }
        
        return null;
    }

    private String resolvePreferences(String absoluteClass, String primaryArtifact) {
        if (primaryArtifact == null || primaryArtifact.equals(JDK)) {
            return null;
        }

        //based on artifact
        if (primaryArtifact.equals("javax.ws.rs/jsr311-api/1.1.1") && preferenceConfig.getJaxRs() == JaxRs.V2) {
            return "javax.ws.rs/javax.ws.rs-api/2.0.1";
        }

        //based on class and artifact
        //...

        return primaryArtifact;
    }
}
