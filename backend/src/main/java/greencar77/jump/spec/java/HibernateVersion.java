package greencar77.jump.spec.java;

public enum HibernateVersion {
    V3_6_3(1, "3.6.3.Final"),
    V4_3_0(2, "4.3.0.Final"),
    V5_2_7(3, "5.2.7.Final"),
    V5_2_10(4, "5.2.10.Final");
    
    private int index;
    private String versionString;
    
    private HibernateVersion(int index, String versionString) {
        this.index = index;
        this.versionString = versionString;
    }

    public int getIndex() {
        return index;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }
}
