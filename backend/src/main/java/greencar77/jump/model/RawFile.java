package greencar77.jump.model;

public class RawFile {
    private String path;
    private byte[] content;

    public RawFile(String path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }
}
