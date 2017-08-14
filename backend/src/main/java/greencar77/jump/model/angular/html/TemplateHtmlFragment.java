package greencar77.jump.model.angular.html;

import greencar77.jump.model.angular.controller.Controller;

public class TemplateHtmlFragment extends HtmlFragment {
    private String template;

    public TemplateHtmlFragment() {
        super();
    }

    public TemplateHtmlFragment(String name, Controller defaultController, String template) {
        super(name, defaultController);
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
