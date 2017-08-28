package greencar77.jump.generator;

import greencar77.jump.generator.java.MavenProjGenerator;
import greencar77.jump.generator.webapp.WebAppGenerator;
import greencar77.jump.model.Model;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.webapp.WebAppModel;

public class GeneratorFactory {
    public static Generator<?> create(Model model) {
        if (model instanceof WebAppModel) {
            return new WebAppGenerator((WebAppModel) model);
        } else if (model instanceof MavenProjModel) {
            return new MavenProjGenerator<MavenProjModel>((MavenProjModel) model);
        } else {
            throw new RuntimeException("Unsupported model type: " + model.getClass().getSimpleName());
        }
    }
}
