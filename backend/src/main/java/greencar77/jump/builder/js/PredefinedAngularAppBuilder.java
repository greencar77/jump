package greencar77.jump.builder.js;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import greencar77.jump.builder.Predefined;
import greencar77.jump.generator.Generator;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.js.AngularVersion;
import greencar77.jump.model.angular.ControlledPage;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.directive.Directive;
import greencar77.jump.model.angular.html.Br;
import greencar77.jump.model.angular.html.DomNode;
import greencar77.jump.model.angular.html.HtmlFragment;
import greencar77.jump.model.angular.html.Input;
import greencar77.jump.model.angular.html.NgButton;
import greencar77.jump.model.angular.html.TemplateHtmlFragment;

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

    public AngularAppModel buildTutti() {       
        
        model.setBootstrapCss(true);
        model.setAngularVersion(AngularVersion.LATEST);
        model.setNgRoute(true);
        model.setBootstrapUi(true);
        model.setTitle("Palette (tutti)");
        
        Module module = addModule();

        //palette
        addPalette();
        
        //directives
        Directive directive = new Directive("dirFirst", "dir_first");
        directive.setHtml(new TemplateHtmlFragment("dir_first", null, "directives/dir_first.html"));
        module.getDirectives().add(directive);
        
        addControlledPage("directives", "DirectiveDemoCtrl", "directiveDemo", "directives");

        //multiElements
        ControlledPage controlledPage = addControlledPage("multiElements", "MultiElementsCtrl");
        
        //popup
        ControlledPage controlledPopup = setupPopup("popup", "PopupCtrl");
        
        //page itself        
        Controller controller = controlledPage.getController();
        controller.getParameters().add("$uibModal"); //requires UI bootstrap
        StringBuilder ctrl = controller.getContent();
        ctrl.append(Generator.LF);

        DomNode rootNode = controlledPage.getHtmlFragment().getRootNode();

        rootNode.add(new NgButton("btn btn-default", "button", "Click me!", "buttonPush()"));
        ctrl.append(code(indent(TAB,
                "$scope.buttonPush = function() {",
                TAB + "$uibModal.open({",
                TAB + TAB + "templateUrl: '" + controlledPopup.getHtmlFragment().getFullPath() + "',",
                TAB + TAB + "controller: '" + controlledPopup.getController().getName() + "',",
                TAB + "});",
                "};"
                ))
                );

        rootNode.add(new Br());

        rootNode.add(new NgButton("btn btn-default", "button", "Click alert!", "buttonAlert()"));        
        ctrl.append(code(indent(TAB,
                "$scope.buttonAlert = function() {",
                TAB + "alert('aaa');",
                "};"
                ))
                );

        return model;
    }

    public AngularAppModel buildInlineTemplating() {
        model.setTitle("Inline templating");
        model.setNgRoute(true);

        addModule();
        
        addControlledPage("first", "FirstCtrl");
        addControlledPage("second", "SecondCtrl");
        
        model.setInlineHtml(true);

        return model;
    }
    
    protected ControlledPage setupPopup(String htmlFilename, String controllerName) {
        ControlledPage controlledPopup = addControlledPage(htmlFilename, controllerName);
        HtmlFragment popupHtml = controlledPopup.getHtmlFragment();
        popupHtml.setStandalonePage(false);
        popupHtml.setInlineControllerReference(false); //will be done in open function
        DomNode rootNode = popupHtml.getRootNode();
        rootNode.add(new Input(null, null));
        rootNode.add(new Br());
        rootNode.add(new NgButton("btn btn-primary", "button", "OK", "buttonOk()"));
        rootNode.add(new NgButton("btn btn-warning", "button", "Cancel", "buttonCancel()"));
        
        Controller controller = controlledPopup.getController();
        controller.getParameters().add("$uibModalInstance"); //requires UI bootstrap
        
        controller.getContent().append(code(indent(TAB,
                "$scope.buttonOk = function() {",
                TAB + "$uibModalInstance.close('aaa');",
                "};",
                //Generator.LF,
                "$scope.buttonCancel = function() {",
                TAB + "$uibModalInstance.dismiss('cancel');",
                "};"
                ))
                );
        
        return controlledPopup;
    }

    public void buildDirectiveMulti() {
        
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
