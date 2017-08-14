package greencar77.jump.model.angular.controller;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private String name = "MainCtrl";
    private List<String> parameters = new ArrayList<>();
    private StringBuilder content = new StringBuilder();

    public Controller() {
        parameters.add("$scope");
    }

    public Controller(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringBuilder getContent() {
        return content;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
