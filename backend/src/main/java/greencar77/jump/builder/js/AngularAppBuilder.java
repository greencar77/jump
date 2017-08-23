package greencar77.jump.builder.js;

import org.apache.commons.lang.Validate;

import greencar77.jump.builder.Builder;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.angular.ControlledPage;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.controller.TemplateController;
import greencar77.jump.model.angular.html.HtmlFragment;
import greencar77.jump.model.angular.html.TemplateHtmlFragment;
import greencar77.jump.spec.js.AngularAppSpec;

public class AngularAppBuilder extends Builder<AngularAppSpec, AngularAppModel> {
    
    //Having result object as instance variable relieves us from passing it around in method signatures.
    //This is the most heavily used variable in this builder.
    protected AngularAppModel model = new AngularAppModel();

    public AngularAppBuilder(AngularAppSpec spec) {
        super(null);
    }

    @Override
    protected AngularAppModel buildModel() {
        Validate.notNull(getSpec());
        //TODO use Spec to build model
        
        return model;
    }
    
    protected void addPalette() {
        Module module = model.getModules().iterator().next();
        
        Controller contr = new TemplateController("palette/PaletteCtrl.js", "PaletteCtrl");
        module.getControllers().add(contr);
        //angularApp.getHtmlFragments().add(new PaletteHtmlFragment("palette", contr));
        TemplateHtmlFragment html = new TemplateHtmlFragment();
        html.setTemplate("palette/palette.html");
        html.setFilename("palette");
        model.getHtmlFragments().add(html);
    }

    protected void addControlledPage(String htmlFilename, String controllerName, String resourceFolder, String resourceFile) {
        Module module = model.getModules().iterator().next();
        
        Controller contr = new TemplateController(resourceFolder + "/" + controllerName + ".js", controllerName);
        module.getControllers().add(contr);
        TemplateHtmlFragment html = new TemplateHtmlFragment();
        html.setTemplate(resourceFolder + "/" + resourceFile + ".html");
        html.setFilename(htmlFilename);
        model.getHtmlFragments().add(html);
    }

    protected ControlledPage addControlledPage(String htmlFilename, String controllerName) {
        Module module = model.getModules().iterator().next();
        
        Controller contr = new Controller(controllerName);
        module.getControllers().add(contr);
        HtmlFragment html = new HtmlFragment(htmlFilename, contr);
        model.getHtmlFragments().add(html);
        
        return new ControlledPage(html, contr);
    }

    protected Module addModule() {
        Module module = new Module();
        model.getModules().add(module);
        module.getControllers().add(new Controller()); //default controller for index page
        
        return module;
    }

    protected HtmlFragment register(HtmlFragment htmlFragment) {
        model.getHtmlFragments().add(htmlFragment);
        return htmlFragment;
    }
}
