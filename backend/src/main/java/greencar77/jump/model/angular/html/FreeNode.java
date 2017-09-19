package greencar77.jump.model.angular.html;

public class FreeNode extends DomNode {
    private StringBuilder content = new StringBuilder();
    
    public FreeNode() {
        super(null, null, null);
    }

    public StringBuilder getContent() {
        return content;
    }
}
