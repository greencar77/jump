package greencar77.jump.generator;

import greencar77.jump.generator.webapp.WebAppGenerator;
import greencar77.jump.model.Model;
import greencar77.jump.model.webapp.WebAppModel;

public class GeneratorFactory {
    public static Generator<?> create(Model model) {
        if (model instanceof WebAppModel) {
            return new WebAppGenerator((WebAppModel) model);
        }
        
        throw new RuntimeException("Unsupported model type: " + model.getClass().getSimpleName());
    }

}