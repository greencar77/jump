package greencar77.jump.model.java.classfile;

import java.util.HashMap;
import java.util.Map;

import greencar77.jump.generator.Generator;

public class PojoClassFile extends ClassFile {

    //key=name, value=type
    public Map<String, String> properties = new HashMap<String, String>();

    public PojoClassFile(String packageName, String className, String classNameTail) {
        super(packageName, className, classNameTail, false);
    }

    @Override
    public StringBuilder generateStateBody() {
        StringBuilder sb = new StringBuilder();
        
        for (Map.Entry<String, String> entry: properties.entrySet()) {
            sb.append(Generator.TAB + "private " + entry.getValue() + " " + entry.getKey() + ";" + Generator.LF);
        }
        return sb;
    }

    @Override
    public StringBuilder generateBody() {
        StringBuilder sb = new StringBuilder();
        
        for (Map.Entry<String, String> entry: properties.entrySet()) {
            /*
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
             */
            sb.append(Generator.LF);
            sb.append(Generator.TAB + "public " + entry.getValue() + " get" + getFirstUpper(entry.getKey()) + "() {" + Generator.LF);
            sb.append(Generator.TAB + Generator.TAB + "return " + entry.getKey() + ";" + Generator.LF);
            sb.append(Generator.TAB + "}" + Generator.LF);
            
            sb.append(Generator.LF);
            sb.append(Generator.TAB + "public void set" + getFirstUpper(entry.getKey()) + "(" + entry.getValue() + " " + entry.getKey()  + ") {" + Generator.LF);
            sb.append(Generator.TAB + Generator.TAB + "this." + entry.getKey() + " = " + entry.getKey() + ";" + Generator.LF);
            sb.append(Generator.TAB + "}" + Generator.LF);
        }
        return sb;
    }
    
    public static final String getFirstUpper(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
