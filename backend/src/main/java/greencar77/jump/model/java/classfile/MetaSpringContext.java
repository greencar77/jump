package greencar77.jump.model.java.classfile;

import java.util.HashSet;
import java.util.Set;

public class MetaSpringContext {
    private String id;
    private Set<MetaBean> beans = new HashSet<MetaBean>();
    private String componentScanBasePackage;

    public MetaSpringContext(String id) {
        this.id = id;
    }
    
    public void register(MetaBean bean) {
        beans.add(bean);
    }

    public Set<MetaBean> getBeans() {
        return beans;
    }
    
    public MetaSpringContext registerBean(ClassFile clazz, String name) {
        beans.add(new MetaBean(clazz, name));
        return this;
    }

    public String getId() {
        return id;
    }

    public String getComponentScanBasePackage() {
        return componentScanBasePackage;
    }

    public void setComponentScanBasePackage(String componentScanBasePackage) {
        this.componentScanBasePackage = componentScanBasePackage;
    }
}
