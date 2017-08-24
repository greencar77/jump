package greencar77.jump.generator;

import java.io.File;
import java.io.IOException;
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
    private static final boolean CLEAN_TEMPLATE_CONTENTS_ONLY = true;  //avoid problem ("java.io.IOException: Unable to delete directory") if folder is opened in a command window
    protected static final String INSTRUCTIONS_FILENAME = "instructions.txt";

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

    public final void generate() {
        System.out.println(this.getClass().getSimpleName());
        clean();
        generateContent();
        generateInstructions();
    }

    protected abstract void generateInstructions();

    private void clean() {
        final File templateFolder = new File(OUTPUT_PATH + projectFolder);
        if (CLEAN_TEMPLATE_CONTENTS_ONLY) {
            if (templateFolder.listFiles() != null) {
                for (File file: templateFolder.listFiles()) {
                    try {
                        System.out.println("Deleting " + file.getAbsolutePath());
                        if (file.isDirectory()) {
                            org.apache.commons.io.FileUtils.deleteDirectory(file);
                        } else {
                            file.delete();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else { //remove the whole folder
            try {
                System.out.println("Deleting " + templateFolder.getAbsolutePath());
                org.apache.commons.io.FileUtils.deleteDirectory(templateFolder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract void generateContent();

    protected File saveResource(String filename, byte[] content) {
        if (files.contains(filename)) {
            throw new RuntimeException("Duplicate file: " + filename);
        }
        files.add(filename);
        return FileUtils.saveFileForced(OUTPUT_PATH + projectFolder + "/" + filename,  content);
    }
}
