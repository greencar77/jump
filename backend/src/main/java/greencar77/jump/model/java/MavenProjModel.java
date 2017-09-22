package greencar77.jump.model.java;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.model.Model;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.MetaSpringContext;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.model.java.maven.XmlSpringContext;
import greencar77.jump.spec.java.JavaVersion;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.java.SpringBootVersion;

public class MavenProjModel extends Model {
    private JavaVersion javaVersion;
    private Pom pom;
    private List<RawFile> rawFiles = new ArrayList<>();
    private ClassFile mainClass;
    private List<ClassFile> classFiles = new ArrayList<>();
    private List<ClassFile> testClassFiles = new ArrayList<>();
    private List<String> runtimeClass = new ArrayList<>(); //class names which will be needed only during deploy/runtime
    private PersistenceUnit persistenceUnit;
    private HibernateConfiguration hibernateConfiguration;
    private SpringBootVersion springBootVersion;
    private XmlSpringContext xmlSpringContext;
    
    private MavenProjSpec spec;

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

    public ClassFile getMainClass() {
        return mainClass;
    }

    public void setMainClass(ClassFile mainClass) {
        this.mainClass = mainClass;
    }

    public HibernateConfiguration getHibernateConfiguration() {
        return hibernateConfiguration;
    }

    public void setHibernateConfiguration(HibernateConfiguration hibernateConfiguration) {
        this.hibernateConfiguration = hibernateConfiguration;
    }

    public PersistenceUnit getPersistenceUnit() {
        return persistenceUnit;
    }

    public void setPersistenceUnit(PersistenceUnit persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    public SpringBootVersion getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(SpringBootVersion springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    public MavenProjSpec getSpec() {
        return spec;
    }

    public void setSpec(MavenProjSpec spec) {
        this.spec = spec;
    }

    public XmlSpringContext getXmlSpringContext() {
        return xmlSpringContext;
    }

    public void setXmlSpringContext(XmlSpringContext xmlSpringContext) {
        this.xmlSpringContext = xmlSpringContext;
    }
}
