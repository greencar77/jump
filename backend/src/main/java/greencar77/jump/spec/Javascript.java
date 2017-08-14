package greencar77.jump.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //TODO remove
public class Javascript {
    private Boolean angular;
    private Boolean uiRoutes;
    private AngularSource angularSource;

    public Boolean getAngular() {
        return angular;
    }

    public void setAngular(Boolean angular) {
        this.angular = angular;
    }

    public Boolean getUiRoutes() {
        return uiRoutes;
    }

    public void setUiRoutes(Boolean uiRoutes) {
        this.uiRoutes = uiRoutes;
    }

    public AngularSource getAngularSource() {
        return angularSource;
    }

    public void setAngularSource(AngularSource angularSource) {
        this.angularSource = angularSource;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Javascript [angular=");
        builder.append(angular);
        builder.append(", uiRoutes=");
        builder.append(uiRoutes);
        builder.append(", angularSource=");
        builder.append(angularSource);
        builder.append("]");
        return builder.toString();
    }
}
