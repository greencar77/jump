package greencar77.jump.model;

public class Model {
    private String projectFolder; //generation instance id, change it for each generation
    private boolean root = true;

    public String getProjectFolder() {
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
