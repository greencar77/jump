package greencar77.jump.spec.java;

public class Spring {
    private SpringVersion version;
    private BeanDefinition beanDefinition;
    private BeanInstantiation beanInstantiation;

    public SpringVersion getVersion() {
        return version;
    }

    public void setVersion(SpringVersion version) {
        this.version = version;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    public BeanInstantiation getBeanInstantiation() {
        return beanInstantiation;
    }

    public void setBeanInstantiation(BeanInstantiation beanInstantiation) {
        this.beanInstantiation = beanInstantiation;
    }
}
