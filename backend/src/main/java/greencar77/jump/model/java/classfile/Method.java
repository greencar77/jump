package greencar77.jump.model.java.classfile;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private String name;
    private String signature;
    private String returnType;
    private StringBuilder content = new StringBuilder(); 
    public List<String> classAnnotations = new ArrayList<>();
    private boolean staticFlag;

    public Method(String name) {
        super();
        this.name = name;
    }

    //with most attributes
    public Method(boolean staticFlag, String returnType, String name, String signature) {
        super();
        this.staticFlag = staticFlag;
        this.returnType = returnType;
        this.name = name;
        this.signature = signature;
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

    public boolean isStaticFlag() {
        return staticFlag;
    }

    public void setStaticFlag(boolean staticFlag) {
        this.staticFlag = staticFlag;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
