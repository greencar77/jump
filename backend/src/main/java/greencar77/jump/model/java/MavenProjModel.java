package greencar77.jump.model.java;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.model.Model;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.maven.Pom;

public class MavenProjModel extends Model {
    private Pom pom;
    private List<RawFile> rawFiles = new ArrayList<>();
    private List<ClassFile> classFiles = new ArrayList<>();

    public Pom getPom() {
        return pom;
    }

    public void setPom(Pom pom) {
        this.pom = pom;
    }

    public List<RawFile> getRawFiles() {
        return rawFiles;
    }

    public List<ClassFile> getClassFiles() {
        return classFiles;
    }
}
