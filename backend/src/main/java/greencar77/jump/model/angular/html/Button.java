package greencar77.jump.model.angular.html;

public class Button extends DomNode {
    private String type; //TODO unused?

    public Button(String clazz, String type, String value) {
        super("button", clazz, value);
        this.type = type;
        addAttribute("type", type);
    }

    public String getType() {
        return type;
    }

}
