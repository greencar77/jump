package greencar77.jump.builder.js;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import org.apache.commons.lang.Validate;

import greencar77.jump.builder.Builder;
import greencar77.jump.generator.Generator;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.js.AngularVersion;
import greencar77.jump.model.angular.ControlledPage;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.controller.TemplateController;
import greencar77.jump.model.angular.directive.Directive;
import greencar77.jump.model.angular.html.Br;
import greencar77.jump.model.angular.html.DomNode;
import greencar77.jump.model.angular.html.HtmlFragment;
import greencar77.jump.model.angular.html.Input;
import greencar77.jump.model.angular.html.MiscNode;
import greencar77.jump.model.angular.html.NgButton;
import greencar77.jump.model.angular.html.TemplateHtmlFragment;
import greencar77.jump.spec.js.AngularAppSpec;
import greencar77.jump.spec.js.BootstrapUi;

public class AngularAppBuilder extends Builder<AngularAppSpec, AngularAppModel> {
    
    //Having result object as instance variable relieves us from passing it around in method signatures.
    //This is the most heavily used variable in this builder.
    protected AngularAppModel model = new AngularAppModel();

    public AngularAppBuilder(AngularAppSpec spec) {
        super(spec);
    }

    @Override
    protected AngularAppModel buildModel() {
        Validate.notNull(getSpec());
        model.setProjectFolder(getSpec().getProjectName());

        model.setBootstrapCss(getSpec().isBootstrapCss());
        model.setBootstrapUi(getSpec().isBootstrapUi());
        if (getSpec().getBootstrapUiSpec() != null) {
            model.setBootstrapUiVersion(getSpec().getBootstrapUiSpec().getVersion());
        }

        if (getSpec().getAppGenerator() != null) {
            invoke(getSpec().getAppGenerator());
        }
        
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
    
    @Override
    protected void setupDefault() {
        super.setupDefault();
        
        if (getSpec().isBootstrapUi() && getSpec().getBootstrapUiSpec() == null) {
            BootstrapUi bootstrapUi = new BootstrapUi();
            bootstrapUi.setVersion("2.5.0");
            getSpec().setBootstrapUiSpec(bootstrapUi);
        }
        
        if (getSpec().isBootstrapUi()) {
            getSpec().setBootstrapCss(true);
        }
        
        if (getSpec().getAppGenerator() == null) {
            getSpec().setAppGenerator("buildAppEmpty");
        }
    }
    
    protected void buildAppEmpty() {
        addModule();
        model.setTitle("Empty project");
    }
    
    protected void buildAppFeatures() {
        addModule();

        //TODO move to spec
        model.setAngularVersion(AngularVersion.LATEST);
        model.setNgRoute(true);
        model.setTitle("Features project");

        addModule();

        if (getSpec().isFeatureTabs()) {
            appendTabs();
        }
    }

    protected void buildAppTutti() {
        //TODO move to spec
        model.setAngularVersion(AngularVersion.LATEST);
        model.setNgRoute(true);
        model.setTitle("Palette (tutti)");

        addModule();

        Module module = model.getModules().iterator().next();

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

        appendTabs();
    }
    
    protected void appendTabs() {
        model.setBootstrapUi(true);
        ControlledPage tabPage = addControlledPage("tabs", "TabsCtrl");
        DomNode tabRoot = tabPage.getHtmlFragment().getRootNode();
        
        int majorVersion = Integer.valueOf(getSpec().getBootstrapUiSpec().getVersion().substring(0, getSpec().getBootstrapUiSpec().getVersion().indexOf(".")));
        if (majorVersion >=2 ) {
            tabRoot.add(new MiscNode("uib-tabset", null, null, "active=\"active\"")
                    .add(new MiscNode("uib-tab", null, null, "heading=\"Head1\""))
                    .add(new MiscNode("uib-tab", null, null, "heading=\"Head2\""))
                    .add(new MiscNode("uib-tab", null, null, "heading=\"Head3\""))
                    );
        } else {
            tabRoot.add(new MiscNode("tabset", null, null, "active=\"active\"")
                    .add(new MiscNode("tab", null, null, "heading=\"Head1\""))
                    .add(new MiscNode("tab", null, null, "heading=\"Head2\""))
                    .add(new MiscNode("tab", null, null, "heading=\"Head3\""))
                    );
        }
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
}
