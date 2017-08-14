package greencar77.jump.spec;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Platform {
    @JsonProperty("java")
    JAVA,
    @JsonProperty("javascript")
    JAVASCRIPT;
}
