package greencar77.jump.model.java.classfile;

public class MetaBean {
    private ClassFile classFile;
    private String name;

    public MetaBean(ClassFile classFile, String name) {
        super();
        this.classFile = classFile;
        this.name = name;
    }
    public ClassFile getClassFile() {
        return classFile;
    }
    public String getName() {
        return name;
    }
}
