package greencar77.jump.model.js;

public enum AngularVersion {
    V_1_5_7,
    LATEST;
    
    public static boolean is16Plus(AngularVersion version) {
        if (version == LATEST) {
            return true;
        } else {
            return false;
        }
    }
}
