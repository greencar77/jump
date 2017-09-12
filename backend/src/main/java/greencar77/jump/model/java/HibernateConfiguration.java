package greencar77.jump.model.java;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.model.java.classfile.ClassFile;

public class HibernateConfiguration {
    private List<ClassFile> domainClasses = new ArrayList<>();

    public List<ClassFile> getDomainClasses() {
        return domainClasses;
    }
}
