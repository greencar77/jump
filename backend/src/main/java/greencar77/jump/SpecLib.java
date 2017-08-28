package greencar77.jump;

import greencar77.jump.generator.js.JsFiddleGenerator;
import greencar77.jump.generator.js.PlunkerAngularGenerator;
import greencar77.jump.generator.js.StandaloneAngularGenerator;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.js.AngularVersion;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.controller.TemplateController;
import greencar77.jump.model.angular.html.HtmlFragment;
import greencar77.jump.model.angular.html.TemplateHtmlFragment;

public class SpecLib {

    public static void modal() {
        String projectFolder = "modal";
        
        AngularAppModel model = new AngularAppModel();
        model.setBootstrapCss(true);

        model.setProjectFolder(projectFolder);
        model.setTitle("Modal with UI Bootstrap");

        Module module = new Module();
        model.getModules().add(module);
        module.getControllers().add(new Controller()); //default controller for index page

        new StandaloneAngularGenerator<AngularAppModel>(model)
            .generate();
    }

    public static void localStorage() {
        String projectFolder = "localStorage";

        AngularAppModel angularApp = new AngularAppModel(AngularVersion.LATEST, true);
        angularApp.setProjectFolder(projectFolder);
        Module module = new Module();
        angularApp.getModules().add(module);
        module.getControllers().add(new Controller());
        
        TemplateController contr = new TemplateController();
        contr.setName("LocalStorageCtrl");
        contr.setTemplate("localStorage/LocalStorageCtrl.js");
        module.getControllers().add(contr);
        TemplateHtmlFragment fragment = new TemplateHtmlFragment();
        fragment.setDefaultController(contr);
        fragment.setFilename("localStorage");
        fragment.setTemplate("localStorage/localStorage.html");
        angularApp.getHtmlFragments().add(fragment);

        new StandaloneAngularGenerator<AngularAppModel>(angularApp)
            .generate();
    }
    
    public static void cookie() {
        String projectFolder = "cookie";

        AngularAppModel angularApp = new AngularAppModel(AngularVersion.LATEST, true);
        angularApp.setProjectFolder(projectFolder);
        angularApp.setNgCookies(true);
        Module module = new Module();
        angularApp.getModules().add(module);
        module.getControllers().add(new Controller());
        
        TemplateController contr = new TemplateController();
        contr.setName("CookieCtrl");
        contr.setTemplate("cookie/CookieCtrl.js");
        module.getControllers().add(contr);
        TemplateHtmlFragment fragment = new TemplateHtmlFragment();
        fragment.setDefaultController(contr);
        fragment.setFilename("cookie");
        fragment.setTemplate("cookie/cookie.html");
        angularApp.getHtmlFragments().add(fragment);

        new StandaloneAngularGenerator<AngularAppModel>(angularApp)
            .generate();
    }
    
    public static void restTest() {
        String projectFolder = "rest_test";

        AngularAppModel model = new AngularAppModel(AngularVersion.LATEST, false);
        model.setProjectFolder(projectFolder);
        Module module = new Module();
        module.getControllers().add(new Controller());
        model.getModules().add(module);
        
        new PlunkerAngularGenerator(model)
            .generate();
    }
    
    public static void extendController() {
        String projectFolder = "extendController2";

        AngularAppModel angularApp = new AngularAppModel(AngularVersion.LATEST, true);
        angularApp.setProjectFolder(projectFolder);
        Module module = new Module();
        angularApp.getModules().add(module);
        Controller contr0 = new Controller("MainCtrl");
        Controller contr1 = new Controller("FirstCtrl");
        Controller contr2 = new Controller("SecondCtrl");
        
        module.getControllers().add(contr0);
        module.getControllers().add(contr1);
        module.getControllers().add(contr2);
        angularApp.getHtmlFragments().add(new HtmlFragment("first", contr1));
        angularApp.getHtmlFragments().add(new HtmlFragment("second", contr2));
        
        new PlunkerAngularGenerator(angularApp)
            .generate();
    }
    
    public static void plunker() {
        String projectFolder = "aaa";

        AngularAppModel angularApp = new AngularAppModel(AngularVersion.V_1_5_7, false);
        angularApp.setProjectFolder(projectFolder);
        Module module = new Module();
        angularApp.getModules().add(module);
        Controller mainContr = new Controller("MainCtrl");
        module.getControllers().add(mainContr);

        new PlunkerAngularGenerator(angularApp)
            .generate();
    }
    
    public static void jsfiddle() {
        String projectFolder = "aaa";

        AngularAppModel model = new AngularAppModel(AngularVersion.V_1_5_7, true);
        model.setProjectFolder(projectFolder);
        model.getModules().add(new Module());
        
        new JsFiddleGenerator(model, true)
            .generate();
    }
    
    public static void angularFilter() {
        String projectFolder = "temp";

        AngularAppModel model = new AngularAppModel(AngularVersion.V_1_5_7, false);
        model.setProjectFolder(projectFolder);
        Module module = new Module();
        module.getControllers().add(new Controller());
        model.getModules().add(module);
        
        new PlunkerAngularGenerator(model)
            .generate();
    }

}
