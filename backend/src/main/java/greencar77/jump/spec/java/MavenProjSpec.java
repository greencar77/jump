package greencar77.jump.spec.java;

import greencar77.jump.spec.Spec;

public class MavenProjSpec extends Spec {
    private String groupId;
    private String artifactId;
    private String rootPackage = "x.y";
    private String appGenerator;
    private JavaVersion javaVersion = JavaVersion.V18;
    private boolean featureExcel;
    private boolean featureUnitTests;
    private UnitTests unitTests;
    private boolean featureSpring;
    private Spring spring;
    private boolean featureSpringBoot;
    private SpringBoot springBoot;
    private boolean featureMqRabbit;
    private boolean featureHibernate;
    private Hibernate hibernate;
    private boolean invokeCli;

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getAppGenerator() {
        return appGenerator;
    }

    public void setAppGenerator(String appGenerator) {
        this.appGenerator = appGenerator;
    }

    public JavaVersion getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(JavaVersion javaVersion) {
        this.javaVersion = javaVersion;
    }

    public boolean isFeatureExcel() {
        return featureExcel;
    }

    public void setFeatureExcel(boolean featureExcel) {
        this.featureExcel = featureExcel;
    }

    public boolean isFeatureUnitTests() {
        return featureUnitTests;
    }

    public void setFeatureUnitTests(boolean featureUnitTests) {
        this.featureUnitTests = featureUnitTests;
    }

    public boolean isFeatureSpring() {
        return featureSpring;
    }

    public void setFeatureSpring(boolean featureSpring) {
        this.featureSpring = featureSpring;
    }

    public Spring getSpring() {
        return spring;
    }

    public void setSpring(Spring spring) {
        this.spring = spring;
    }

    public boolean isFeatureMqRabbit() {
        return featureMqRabbit;
    }

    public void setFeatureMqRabbit(boolean featureMqRabbit) {
        this.featureMqRabbit = featureMqRabbit;
    }

    public boolean isFeatureHibernate() {
        return featureHibernate;
    }

    public void setFeatureHibernate(boolean featureHibernate) {
        this.featureHibernate = featureHibernate;
    }

    public Hibernate getHibernate() {
        return hibernate;
    }

    public void setHibernate(Hibernate hibernate) {
        this.hibernate = hibernate;
    }

    public boolean isInvokeCli() {
        return invokeCli;
    }

    public void setInvokeCli(boolean invokeCli) {
        this.invokeCli = invokeCli;
    }

    public UnitTests getUnitTests() {
        return unitTests;
    }

    public void setUnitTests(UnitTests unitTests) {
        this.unitTests = unitTests;
    }

    public boolean isFeatureSpringBoot() {
        return featureSpringBoot;
    }

    public void setFeatureSpringBoot(boolean featureSpringBoot) {
        this.featureSpringBoot = featureSpringBoot;
    }

    public SpringBoot getSpringBoot() {
        return springBoot;
    }

    public void setSpringBoot(SpringBoot springBoot) {
        this.springBoot = springBoot;
    }
}
