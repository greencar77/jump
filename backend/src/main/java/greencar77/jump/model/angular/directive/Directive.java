package greencar77.jump.model.angular.directive;

import greencar77.jump.model.angular.html.HtmlFragment;

public class Directive {
    private String name;
    private String normalizedName; //camelCase
    private HtmlFragment html;

    public Directive() {}

    public Directive(String normalizedName, String name) {
        super();
        this.normalizedName = normalizedName;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public HtmlFragment getHtml() {
        return html;
    }

    public void setHtml(HtmlFragment html) {
        this.html = html;
    }
}
