package greencar77.jump.model.java.classfile;

import greencar77.jump.generator.java.ClassGenerator;

public class RestClassFile extends ClassFile {
    private String path;

    public RestClassFile(String packageName, String className) {
        super(packageName, className);
    }

    public RestClassFile(String packageName, String className, String classNameTail, boolean logging, String path) {
        super(packageName, className, classNameTail, logging);
    }

    @Override
    public StringBuilder generateWith(ClassGenerator generator) {
        return generator.generate(this);        
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        classAnnotations.add("@Path(\"" + this.path + "\")");
        imports.add("javax.ws.rs.Path");
    }
}
