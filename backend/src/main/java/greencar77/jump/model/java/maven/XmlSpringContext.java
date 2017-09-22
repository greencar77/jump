package greencar77.jump.model.java.maven;

import java.util.HashSet;
import java.util.Set;

import greencar77.jump.model.java.classfile.MetaSpringContext;
import greencar77.jump.model.java.classfile.XmlSpringContextNamespace;

public class XmlSpringContext {
    private Set<XmlSpringContextNamespace> namespaces = new HashSet<>();
    
    private MetaSpringContext metaSpringContext;

    public XmlSpringContext(MetaSpringContext metaSpringContext) {
        this.metaSpringContext = metaSpringContext;
    }

    public MetaSpringContext getMetaSpringContext() {
        return metaSpringContext;
    }

    public void setMetaSpringContext(MetaSpringContext metaSpringContext) {
        this.metaSpringContext = metaSpringContext;
    }
    
    public void addNamespace(XmlSpringContextNamespace namespace) {
        namespaces.add(namespace);
    }

    public Set<XmlSpringContextNamespace> getNamespaces() {
        return namespaces;
    }
}
