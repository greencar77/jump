package greencar77.jump.generator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import greencar77.jump.FileUtils;
import greencar77.jump.VelocityManager;

public abstract class Generator<M> {
    protected static final VelocityManager TEMPLATE_MANAGER = VelocityManager.getVelocityManager();

    public static final String OUTPUT_PATH = "generated/template/";
    public static final String LF = "\n";
    public static final String TAB = "    "; //\t

    //protected String pathOffset;
    protected String projectFolder;
    protected M model;
    
    private Set<String> files = new HashSet<>();
    
    public Generator(String projectFolder, M model) {
        Validate.notEmpty(projectFolder);

        this.projectFolder = projectFolder;
        this.model = model;
    }
    
    /*
    public Generator(String pathOffset, String projectFolder, M model) {
        Validate.notEmpty(projectFolder);
        Validate.isTrue(projectFolder.indexOf("\\") == -1 && projectFolder.indexOf("/") == -1. "projectFolder must contain only folder name");

        this.projectFolder = projectFolder;
        this.model = model;
    }*/
    
    public abstract void generate();

    protected File saveResource(String filename, byte[] content) {
        if (files.contains(filename)) {
            throw new RuntimeException("Duplicate file: " + filename);
        }
        files.add(filename);
        return FileUtils.saveFileForced(OUTPUT_PATH + projectFolder + "/" + filename,  content);
    }
}
