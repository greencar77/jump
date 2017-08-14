package greencar77.jump.model.angular.controller;

public class TemplateController extends Controller {
    private String template;

    public TemplateController() {}

    public TemplateController(String template, String name) {
        super(name);
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
