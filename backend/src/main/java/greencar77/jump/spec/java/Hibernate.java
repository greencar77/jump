package greencar77.jump.spec.java;

public class Hibernate {
    private HibernateVersion version;
    private EntityManagerSetupStrategy entityManagerSetupStrategy;

    public HibernateVersion getVersion() {
        return version;
    }

    public void setVersion(HibernateVersion version) {
        this.version = version;
    }

    public EntityManagerSetupStrategy getEntityManagerSetupStrategy() {
        return entityManagerSetupStrategy;
    }

    public void setEntityManagerSetupStrategy(EntityManagerSetupStrategy entityManagerSetupStrategy) {
        this.entityManagerSetupStrategy = entityManagerSetupStrategy;
    }
}
