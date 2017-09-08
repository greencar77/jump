package greencar77.jump.builder.js;

import greencar77.jump.builder.Predefined;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.directive.Directive;
import greencar77.jump.model.angular.html.HtmlFragment;
import greencar77.jump.model.angular.html.TemplateHtmlFragment;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.js.AngularVersion;
import greencar77.jump.spec.js.AngularAppSpec;

/**
 * Contains hard-coded recipes (Spec object is not used) *
 */
public class PredefinedAngularAppBuilder extends AngularAppBuilder implements Predefined<AngularAppModel> {

    public PredefinedAngularAppBuilder() {
        super(null);
    }
    
    @Override
    public AngularAppModel build(String specId) {        
        return generateModel(specId);
    }

    public AngularAppModel specTutti() {
        AngularAppSpec spec = new AngularAppSpec();

        setSpec(spec);

        build();        
        
        buildAppTutti();

        return model;
    }

    public AngularAppModel specInlineTemplating() {
        model.setTitle("Inline templating");
        model.setNgRoute(true);

        addModule();
        
        addControlledPage("first", "FirstCtrl");
        addControlledPage("second", "SecondCtrl");
        
        model.setInlineHtml(true);

        return model;
    }

    public void specDirectiveMulti() {
        
        model.setAngularVersion(AngularVersion.LATEST);
        model.setNgRoute(true);
        model.setTitle("Directive Demo");
        
        addModule();
        Module module = model.getModules().iterator().next();

        //directives
        Directive directive1 = new Directive("dirFirst", "dir_first");
        directive1.setHtml(new TemplateHtmlFragment("dir_first", null, "directives/dir_first.html"));
        module.getDirectives().add(directive1);

        Directive directive2 = new Directive("simpleInput", "simple-input");
        directive2.setHtml(new TemplateHtmlFragment("simple_input", null, "directives/simple_input.html"));
        module.getDirectives().add(directive2);

        addControlledPage("directivesTemplate", "DirectiveDemoCtrl", "directiveDemo", "directives");
        
        Controller contr = new Controller("DirectiveMultiCtrl");
        module.getControllers().add(contr);
        HtmlFragment html = new HtmlFragment("directivesAdded", contr);
        html.getDirectiveUsages().add(directive1);
        html.getDirectiveUsages().add(directive1);
        html.getDirectiveUsages().add(directive2);
        model.getHtmlFragments().add(html);
    }
}
