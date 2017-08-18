package greencar77.jump.model.angular.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomNode {
    private String name;
    private String clazz;
    private String value;
    private Map<String, String> attributes = new HashMap<>();
    protected String rawAttributes;
    private List<DomNode> nodes = new ArrayList<DomNode>();

    public DomNode(String name, String clazz, String value) {
        super();
        this.name = name;
        this.clazz = clazz;
        if (clazz != null) {
            addAttribute("class", clazz);
        }
        this.value = value;
    }

    public List<DomNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<DomNode> nodes) {
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getValue() {
        return value;
    }
    
    public DomNode add(DomNode node) {
        nodes.add(node);
        return this;
    }

    public String getRawAttributes() {
        return rawAttributes;
    }

    public void setRawAttributes(String rawAttributes) {
        this.rawAttributes = rawAttributes;
    }
}
