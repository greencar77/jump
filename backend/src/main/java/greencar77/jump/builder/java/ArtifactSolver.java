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
        if (absoluteClass.startsWith("org.springframework.boot.autoconfigure.")) {
            return "org.springframework.boot/spring-boot-autoconfigure/1.4.3.RELEASE";
        }
        //after other spring-boot artifacts
        if (absoluteClass.startsWith("org.springframework.boot.")) {
            return "org.springframework.boot/spring-boot/1.4.3.RELEASE";
        }
        if (absoluteClass.startsWith("javax.ws.rs.")) {
            return "javax.ws.rs/jsr311-api/1.1.1";
        }
        if (absoluteClass.startsWith("org.junit.jupiter.")) {
            return "org.junit.jupiter/junit-jupiter-engine/5.0.0";
        }
        
        //Apache
        if (absoluteClass.startsWith("org.apache.poi.xssf.usermodel.")) {
            return "org.apache.poi/poi-ooxml/3.15";
        }
        if (absoluteClass.startsWith("org.apache.naming.")) {
            return "org.apache.tomcat/catalina/6.0.18";
        }
        if (absoluteClass.startsWith("org.apache.derby.")) {
            return "org.apache.derby/derby/10.13.1.1";
        }
        
        //other
        if (absoluteClass.startsWith("org.junit.")) {
            return "junit/junit/4.10";
        }
        if (absoluteClass.startsWith("com.rabbitmq.")) {
            return "com.rabbitmq/amqp-client/4.2.0";
        }
        if (absoluteClass.startsWith("javax.persistence.")) {
            return "org.hibernate.javax.persistence/hibernate-jpa-2.0-api/1.0.0.Final";
        }
        if (absoluteClass.startsWith("org.hibernate.jpa.")) {
            return "org.hibernate/hibernate-core/4.3.0.Final";
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
        if (primaryArtifact.equals("org.hibernate.javax.persistence/hibernate-jpa-2.0-api/1.0.0.Final") && preferenceConfig.getJpa() == Jpa.V2_1) {
            return "org.hibernate.javax.persistence/hibernate-jpa-2.1-api/1.0.0.Final";
        }
        if (primaryArtifact.equals("org.hibernate/hibernate-core/4.3.0.Final") && preferenceConfig.getHibernate() != null) {
            return "org.hibernate/hibernate-core/" + preferenceConfig.getHibernate();
        }
        if (primaryArtifact.startsWith("org.springframework.boot") && preferenceConfig.isSpringBootInheritFromParent()) {
            return primaryArtifact.substring(0, primaryArtifact.lastIndexOf("/"));
        }
        if (primaryArtifact.startsWith("org.springframework/") && preferenceConfig.getSpringVersion() != null) {
            return primaryArtifact.substring(0, primaryArtifact.lastIndexOf("/")) + "/" + preferenceConfig.getSpringVersion();
        }

        //based on class and artifact
        //...

        return primaryArtifact;
    }
}
