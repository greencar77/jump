package greencar77.jump.generator.java;

import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.classfile.TemplateClass;

public interface ClassGenerator {
    StringBuilder generate(TemplateClass clazz);
    StringBuilder generate(RestClassFile clazz);
    StringBuilder generate(ClassFile clazz);
}
