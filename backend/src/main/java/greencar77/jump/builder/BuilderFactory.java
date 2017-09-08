package greencar77.jump.builder;

import greencar77.jump.builder.java.MavenProjBuilder;
import greencar77.jump.builder.js.AngularAppBuilder;
import greencar77.jump.builder.webapp.WebAppBuilder;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.webapp.WebAppModel;
import greencar77.jump.spec.Spec;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.js.AngularAppSpec;
import greencar77.jump.spec.webapp.WebAppSpec;

public class BuilderFactory {
    public static Builder<?, ?> create(Spec spec) {
        if (spec instanceof WebAppSpec) {
            return new WebAppBuilder<WebAppSpec, WebAppModel>((WebAppSpec) spec);
        } else if (spec instanceof MavenProjSpec) {
            return new MavenProjBuilder<MavenProjSpec, MavenProjModel>((MavenProjSpec) spec);
        } else if (spec instanceof AngularAppSpec) {
            return new AngularAppBuilder((AngularAppSpec) spec);
        }
        
        throw new RuntimeException("Unsupported spec type: " + spec.getClass().getSimpleName());
    }
}
