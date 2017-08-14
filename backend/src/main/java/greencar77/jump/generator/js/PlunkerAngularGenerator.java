package greencar77.jump.generator.js;

import java.util.List;

import greencar77.jump.model.js.AngularAppModel;

public class PlunkerAngularGenerator extends StandaloneAngularGenerator<AngularAppModel> {

    public PlunkerAngularGenerator(String projectFolder, AngularAppModel model) {
        super(projectFolder, model);
    }

    @Override
    protected List<String> generateJs() {
        saveResource("script.js", getJsContent());

        return null;
    }
    
    protected byte[] getJsContent() {
        StringBuilder sb = new StringBuilder();
        
        model.getModules().stream().forEach(m -> {
            sb.append("var mod1 = angular.module('" + m.getName() + "', [" 
                    + (model.isNgRoute()? "'ngRoute'": "")
                    + "]);" + LF);
            
            m.getControllers().stream().forEach(c -> {
                sb.append(new String(generateController(c)));
            });
            
            m.getDirectives().stream().forEach(d -> {
                sb.append(new String(generateDirective(d)));
            });
        });
        
        if (model.isNgRoute()) {
            sb.append(new String(generateRoute()));
        }

        return sb.toString().getBytes();
    }
}
