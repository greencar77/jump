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

    public MavenProjModel specHelloSpringBoot() {
        Pom pom = new Pom("greencar77", "hello-spring-boot");
        model.setPom(pom);
        
        return model;
    }
    
    public MavenProjModel specWsdlClient() {
        Pom pom = new Pom("x.bp", "brightpoint-client");
        model.setPom(pom);

        model.getRawFiles().add(FileUtils.createRawJavaClassFromTemplate("App.java", "x.bp.brightpointclient", DEFAULT_MAIN_CLASS_NAME));

        return model;
    }

    public MavenProjModel specEmptyMavenProject() {
        MavenProjSpec spec = new MavenProjSpec();
        spec.setGroupId("x.y");
        spec.setArtifactId("z");
        spec.setRootPackage(spec.getGroupId());

        setSpec(spec);

        build();

        buildAppSimple();

        return model;
    }

    private void buildAppSimple() {

        model.getRawFiles().add(FileUtils.createRawJavaClassFromTemplate("App.java", getSpec().getRootPackage(), DEFAULT_MAIN_CLASS_NAME));
    }

}
