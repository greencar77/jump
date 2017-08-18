package greencar77.jump.generator.js;

import greencar77.jump.generator.Generator;
import greencar77.jump.model.js.AngularAppModel;

public class JsFiddleGenerator extends Generator<AngularAppModel> {

    private boolean singleFile;

    public JsFiddleGenerator(String projectFolder, AngularAppModel model, boolean singleFile) {
        super(projectFolder, model);
        this.singleFile = singleFile;
    }

    @Override
    protected void generateContent() {
        byte[] js = getJsContent();
        
        if (singleFile) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== JavaScript" + LF+ LF);
            sb.append(new String(js));
            saveResource("jsfiddle.txt", sb.toString().getBytes());
        } else {
            saveResource("javascript.txt", js);
        }
    }
    
    private byte[] getJsContent() {
        StringBuilder sb = new StringBuilder();
        
        model.getModules().stream().forEach(m -> {
            sb.append("var mod1 = angular.module('" + m.getName() + "', []);"); //'ngRoute'
            });

        return sb.toString().getBytes();
    }

    @Override
    protected void generateInstructions() {
        // TODO Auto-generated method stub
        
    }

}
