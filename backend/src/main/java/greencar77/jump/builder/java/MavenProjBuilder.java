package greencar77.jump.builder.java;

import org.apache.commons.lang.Validate;

import greencar77.jump.builder.Builder;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.spec.java.MavenProjSpec;

public class MavenProjBuilder<S, M> extends Builder<MavenProjSpec, MavenProjModel> {
    protected static final String DEFAULT_MAIN_CLASS_NAME = "App";
    
    //Having result object as instance variable relieves us from passing it around in method signatures.
    //This is the most heavily used variable in this builder.
    protected MavenProjModel model = new MavenProjModel();

    public MavenProjBuilder() {}
    
    public MavenProjBuilder(MavenProjSpec spec) {
        super(spec);
    }

    @Override
    protected MavenProjModel buildModel() {
        Validate.notNull(getSpec());
        
        String warFilename = getSpec().getArtifactId(); //will be used in url
        
        //config project
        Pom pom = new Pom(getSpec().getGroupId(), getSpec().getArtifactId());
        model.setPom(pom);        
        pom.setPackaging(getPackagingType()); //TODO enum
        pom.getBuild().finalName = warFilename; //this name will be used as build (war) file name
        
        return model;
    }
    
    protected String getPackagingType() {
        return "jar";
    }
}
