package greencar77.jump.model.angular;

import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.html.HtmlFragment;

public class ControlledPage {
    private HtmlFragment htmlFragment;
    private Controller controller;

    public ControlledPage(HtmlFragment htmlFragment, Controller controller) {
        super();
        this.htmlFragment = htmlFragment;
        this.controller = controller;
    }
    
    public Controller getController() {
        return controller;
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public HtmlFragment getHtmlFragment() {
        return htmlFragment;
    }
    public void setHtmlFragment(HtmlFragment htmlFragment) {
        this.htmlFragment = htmlFragment;
    }
}
