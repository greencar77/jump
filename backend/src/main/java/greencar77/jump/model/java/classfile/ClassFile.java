package greencar77.jump.model.java.classfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import greencar77.jump.generator.Generator;
import greencar77.jump.generator.java.ClassGeneratorVisitable;
import greencar77.jump.generator.java.ClassGenerator;

public class ClassFile implements ClassGeneratorVisitable {
    public Set<String> imports = new TreeSet<String>();
    public String packageName;
    public String className;
    public String classNameTail;
    public List<String> classAnnotations = new ArrayList<>();
    private StringBuilder stateBody = new StringBuilder();
    private StringBuilder body = new StringBuilder();
    private boolean logging;
    
    public ClassFile(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }
    
    public ClassFile(String packageName, String className, String classNameTail, boolean logging) {
        super();
        this.packageName = packageName;
        this.className = className;
        this.classNameTail = classNameTail;
        this.logging = logging;
    }
    
    public String getFullName() {
        return packageName + "." + className;
    }

    public StringBuilder generateStateBody() {
        return stateBody;
    }

    public StringBuilder generateBody() {
        return body;
    }

    public StringBuilder getStateBody() {
        return stateBody;
    }

    public StringBuilder getBody() {
        return body;
    }
    
    public void insertLogger() {
        if (logging) {
            stateBody.append(Generator.LF);
            stateBody.append(Generator.TAB + "private static final Logger LOG = LoggerFactory.getLogger(" + className + ".class);" + Generator.LF);
            imports.add("org.slf4j.Logger");
            imports.add("org.slf4j.LoggerFactory");
        }
    }

    public void insertMethodLog(String methodName) {
        if (logging) {
            body.append(Generator.TAB + Generator.TAB + "LOG.debug(\"" + methodName + "\");" + Generator.LF);
        }
    }

    @Override
    public StringBuilder generateWith(ClassGenerator generator) {
        return generator.generate(this);        
    }
}
