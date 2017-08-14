package greencar77.jump.model.java.classfile;

public class MetaClass {
    private String classPackage;
    private String className;
    private boolean bean;

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassPackage() {
        return classPackage;
    }
    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }
    public boolean isBean() {
        return bean;
    }
    public void setBean(boolean bean) {
        this.bean = bean;
    }
}
