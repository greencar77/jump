package greencar77.jump.model.angular.html;

public class NgButton extends Button {

    public NgButton(String clazz, String type, String value, String ngClick) {
        super(clazz, type, value);
        if (ngClick != null) {
            addAttribute("ng-click", ngClick);
        }
    }

}
