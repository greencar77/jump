package greencar77.jump;

import greencar77.jump.builder.java.PredefinedMavenProjBuilder;
import greencar77.jump.builder.js.PredefinedAngularAppBuilder;
import greencar77.jump.builder.webapp.PredefinedWebAppBuilder;
import greencar77.jump.generator.java.MavenProjGenerator;
import greencar77.jump.generator.js.PlunkerAngularGenerator;
import greencar77.jump.generator.js.StandaloneAngularGenerator;
import greencar77.jump.generator.webapp.WebAppGenerator;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.js.AngularAppModel;


public class App {
    
    public static void main(String[] args) {
        //SpecLib.modal();
        
        predefinedStandalone("tutti");
        //predefinedPlunker("inlineTemplating");
        //predefinedMavenProj("helloSpringBoot");
        //predefinedMavenProj("wsdlClient");
        //predefinedWebApp("webappSimple");
        //predefinedWebApp("spring4RestTomcat");
        //predefinedWebApp("spring4RestWildfly");
        //predefinedMavenProj("emptyMavenProject");
    }
    
    protected static void predefinedStandalone(String specId) {
        PredefinedAngularAppBuilder builder = new PredefinedAngularAppBuilder(); //no Spec, predefined builder method will be used
        
        StandaloneAngularGenerator<AngularAppModel> generator = new StandaloneAngularGenerator<AngularAppModel>(specId, builder.build(specId));        
        generator.generate();
    }
    
    protected static void predefinedPlunker(String specId) {
        PredefinedAngularAppBuilder builder = new PredefinedAngularAppBuilder(); //no Spec, predefined builder method will be used
        
        PlunkerAngularGenerator generator = new PlunkerAngularGenerator(specId, builder.build(specId));        
        generator.generate();
    }
    
    protected static void predefinedMavenProj(String specId) {
        PredefinedMavenProjBuilder builder = new PredefinedMavenProjBuilder(); //no Spec, predefined builder method will be used
        
        MavenProjGenerator<MavenProjModel> generator = new MavenProjGenerator<MavenProjModel>(specId, builder.build(specId));
        generator.generate();
    }

    protected static void predefinedWebApp(String specId) {
        new WebAppGenerator(specId, new PredefinedWebAppBuilder().build(specId)).generate();
    }
}
