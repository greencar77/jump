package greencar77.jump.builder.java;

public class PreferenceConfig {
    private JaxRs jaxRs;
    private Jpa jpa;
    private String hibernate;

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
}
