package greencar77.jump.model.java;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.model.Model;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.MetaSpringContext;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.spec.java.JavaVersion;

public class MavenProjModel extends Model {
    private JavaVersion javaVersion;
    private Pom pom;
    private List<RawFile> rawFiles = new ArrayList<>();
    private ClassFile mainClass;
    private List<ClassFile> classFiles = new ArrayList<>();
    private List<ClassFile> testClassFiles = new ArrayList<>();
    private List<String> runtimeClass = new ArrayList<>(); //class names which will be needed only during deploy/runtime
    private MetaSpringContext springContext;

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

    public List<String> getRuntimeClass() {
        return runtimeClass;
    }

    public void setRuntimeClass(List<String> runtimeClass) {
        this.runtimeClass = runtimeClass;
    }

    public JavaVersion getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(JavaVersion javaVersion) {
        this.javaVersion = javaVersion;
    }

    public List<ClassFile> getTestClassFiles() {
        return testClassFiles;
    }

    public void setTestClassFiles(List<ClassFile> testClassFiles) {
        this.testClassFiles = testClassFiles;
    }

    public MetaSpringContext getSpringContext() {
        return springContext;
    }

    public void setSpringContext(MetaSpringContext springContext) {
        this.springContext = springContext;
    }

    public ClassFile getMainClass() {
        return mainClass;
    }

    public void setMainClass(ClassFile mainClass) {
        this.mainClass = mainClass;
    }
}
