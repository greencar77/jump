package greencar77.jump.model.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import greencar77.jump.model.Model;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.html.HtmlFragment;

public class AngularAppModel extends Model {
    //Third-party dependencies
    private boolean bootstrapCss;
    private boolean bootstrapUi;
    private boolean jquery;
    private String bootstrapVersion;
    private List<String> libs = new ArrayList<String>();

    //Angular
    private AngularVersion angularVersion = AngularVersion.LATEST;
    private boolean ngRoute = false;
    private boolean ngCookies = false;

    private String title;

    private Set<Module> modules = new HashSet<>();
    private Set<HtmlFragment> htmlFragments = new HashSet<>();
    
    private boolean inlineHtml;
    
    public AngularAppModel() {}
    
    public AngularAppModel(AngularVersion angularVersion, boolean ngRoute) {
        this.angularVersion = angularVersion;
        this.ngRoute = ngRoute;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Set<HtmlFragment> getHtmlFragments() {
        return htmlFragments;
    }

    public boolean isNgRoute() {
        return ngRoute;
    }

    public AngularVersion getAngularVersion() {
        return angularVersion;
    }

    public void setAngularVersion(AngularVersion angularVersion) {
        this.angularVersion = angularVersion;
    }

    public void setNgRoute(boolean ngRoute) {
        this.ngRoute = ngRoute;
    }

    public boolean isNgCookies() {
        return ngCookies;
    }

    public void setNgCookies(boolean ngCookies) {
        this.ngCookies = ngCookies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBootstrapVersion() {
        return bootstrapVersion;
    }

    public void setBootstrapVersion(String bootstrapVersion) {
        this.bootstrapVersion = bootstrapVersion;
    }

    public boolean isBootstrapCss() {
        return bootstrapCss;
    }

    public void setBootstrapCss(boolean bootstrapCss) {
        this.bootstrapCss = bootstrapCss;
    }

    public boolean isJquery() {
        return jquery;
    }

    public void setJquery(boolean jquery) {
        this.jquery = jquery;
    }

    public boolean isInlineHtml() {
        return inlineHtml;
    }

    public void setInlineHtml(boolean inlineHtml) {
        this.inlineHtml = inlineHtml;
    }

    public List<String> getLibs() {
        return libs;
    }

    public boolean isBootstrapUi() {
        return bootstrapUi;
    }

    public void setBootstrapUi(boolean bootstrapUi) {
        this.bootstrapUi = bootstrapUi;
    }

}
