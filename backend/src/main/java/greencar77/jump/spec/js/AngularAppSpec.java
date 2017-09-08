package greencar77.jump.spec.js;

import greencar77.jump.spec.Spec;

public class AngularAppSpec extends Spec {
    private String appGenerator;
    private boolean bootstrapCss;
    private boolean bootstrapUi;
    private BootstrapUi bootstrapUiSpec;
    private boolean featureTabs;

    public String getAppGenerator() {
        return appGenerator;
    }

    public void setAppGenerator(String appGenerator) {
        this.appGenerator = appGenerator;
    }

    public boolean isFeatureTabs() {
        return featureTabs;
    }

    public void setFeatureTabs(boolean featureTabs) {
        this.featureTabs = featureTabs;
    }

    public boolean isBootstrapCss() {
        return bootstrapCss;
    }

    public void setBootstrapCss(boolean bootstrapCss) {
        this.bootstrapCss = bootstrapCss;
    }

    public boolean isBootstrapUi() {
        return bootstrapUi;
    }

    public void setBootstrapUi(boolean bootstrapUi) {
        this.bootstrapUi = bootstrapUi;
    }

    public BootstrapUi getBootstrapUiSpec() {
        return bootstrapUiSpec;
    }

    public void setBootstrapUiSpec(BootstrapUi bootstrapUiSpec) {
        this.bootstrapUiSpec = bootstrapUiSpec;
    }

}
