package greencar77.jump.model.java.classfile;

import java.util.ArrayList;
import java.util.List;

public class RestMethod {
    private String name;
    private String returnType;
    private StringBuilder content = new StringBuilder(); 
    public List<String> classAnnotations = new ArrayList<>();

    public RestMethod(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public StringBuilder getContent() {
        return content;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
