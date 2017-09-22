package greencar77.jump.model.java.classfile;

public enum XmlSpringContextNamespace {
    BEANS("beans"),
    CONTEXT("context");

    private String prefix;

    private XmlSpringContextNamespace(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
