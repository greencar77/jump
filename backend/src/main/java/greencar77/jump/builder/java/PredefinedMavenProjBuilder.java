package greencar77.jump.builder.java;

import greencar77.jump.FileUtils;
import greencar77.jump.builder.Predefined;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.spec.java.MavenProjSpec;

public class PredefinedMavenProjBuilder extends MavenProjBuilder<MavenProjSpec, MavenProjModel> implements Predefined<MavenProjModel> {

    public PredefinedMavenProjBuilder() {}

    public PredefinedMavenProjBuilder(MavenProjSpec spec) {
        super(spec);
    }

    @Override
    public MavenProjModel build(String specId) {        
        return generateModel(specId);
    }

    public MavenProjModel buildHelloSpringBoot() {
        Pom pom = new Pom("greencar77", "hello-spring-boot");
        model.setPom(pom);
        
        return model;
    }
    
    public MavenProjModel buildWsdlClient() {
        Pom pom = new Pom("x.bp", "brightpoint-client");
        model.setPom(pom);

        model.getRawFiles().add(FileUtils.createRawJavaClassFromTemplate("App.java", "x.bp.brightpointclient", DEFAULT_MAIN_CLASS_NAME));

        return model;
    }

}
