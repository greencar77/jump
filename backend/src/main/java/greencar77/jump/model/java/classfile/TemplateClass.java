package greencar77.jump.model.java.classfile;

import java.util.HashMap;
import java.util.Map;

import greencar77.jump.generator.java.ClassGenerator;

public class TemplateClass extends ClassFile {
    private String templateName; //path + filename
    private Map<String, String> properties = new HashMap<>();

    public TemplateClass(String packageName, String className, String templateName, Map<String, String> properties) {
        super(packageName, className);
        this.templateName = templateName;
        this.properties = properties;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public StringBuilder generateWith(ClassGenerator generator) {
        return generator.generate(this);        
    }
}
