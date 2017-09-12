package greencar77.jump.generator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import greencar77.jump.FileUtils;

public abstract class AbstractGenerator {
    public static final String LF = "\n";
    public static final String TAB = "    "; //\t

    public static final String OUTPUT_PATH = "generated/template/";
    
    private String projectFolder;
    private Set<String> files = new HashSet<>();
    
    protected AbstractGenerator(String projectFolder) {
        Validate.notEmpty(projectFolder);
        this.projectFolder = projectFolder;
    }
    
    public abstract void generate();

    protected File saveResource(String filename, byte[] content) {
        if (files.contains(filename)) {
            throw new RuntimeException("Duplicate file: " + filename);
        }
        files.add(filename);
        return FileUtils.saveFileForced(OUTPUT_PATH + projectFolder + "/" + filename,  content);
    }
}
