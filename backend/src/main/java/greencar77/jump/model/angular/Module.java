package greencar77.jump.model.angular;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.directive.Directive;

public class Module {
    private String name = "mainModule";
    private List<Controller> controllers = new ArrayList<>();
    private List<Directive> directives = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Controller> getControllers() {
        return controllers;
    }

    public List<Directive> getDirectives() {
        return directives;
    }
}
