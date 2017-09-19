package greencar77.jump.builder.java;

public class PreferenceConfig {
    private JaxRs jaxRs;
    private Jpa jpa;
    private String hibernate;
    private boolean springBootInheritFromParent;

    public JaxRs getJaxRs() {
        return jaxRs;
    }

    public void setJaxRs(JaxRs jaxRs) {
        this.jaxRs = jaxRs;
    }

    public Jpa getJpa() {
        return jpa;
    }

    public void setJpa(Jpa jpa) {
        this.jpa = jpa;
    }

    public String getHibernate() {
        return hibernate;
    }

    public void setHibernate(String hibernate) {
        this.hibernate = hibernate;
    }

    public boolean isSpringBootInheritFromParent() {
        return springBootInheritFromParent;
    }

    public void setSpringBootInheritFromParent(boolean springBootInheritFromParent) {
        this.springBootInheritFromParent = springBootInheritFromParent;
    }
}
