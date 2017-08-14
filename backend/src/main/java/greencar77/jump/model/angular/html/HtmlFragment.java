package greencar77.jump.model.angular.html;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.directive.Directive;

public class HtmlFragment {
    private String path = "";
    private String filename; //without extension
    
    private Controller defaultController;
    private boolean inlineControllerReference = true;
    
    private boolean standalonePage = true;

    private DomNode rootNode = new RootNode();
    private List<Directive> directiveUsages = new ArrayList<Directive>();

    public HtmlFragment() {
        this.path = "views/";
    }

    public HtmlFragment(String name, Controller defaultController) {
        super();
        this.path = "views/";
        this.filename = name;
        this.defaultController = defaultController;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String name) {
        this.filename = name;
    }

    public Controller getDefaultController() {
        return defaultController;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDefaultController(Controller defaultController) {
        this.defaultController = defaultController;
    }

    public List<Directive> getDirectiveUsages() {
        return directiveUsages;
    }

    public DomNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(DomNode rootNode) {
        this.rootNode = rootNode;
    }
    
    public String getFullPath() {
        return path + filename + ".html";
    }

    public boolean isStandalonePage() {
        return standalonePage;
    }

    public void setStandalonePage(boolean standalonePage) {
        this.standalonePage = standalonePage;
    }

    public boolean isInlineControllerReference() {
        return inlineControllerReference;
    }

    public void setInlineControllerReference(boolean inlineControllerReference) {
        this.inlineControllerReference = inlineControllerReference;
    }
}
