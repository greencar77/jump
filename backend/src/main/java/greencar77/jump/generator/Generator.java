package greencar77.jump.generator;

import java.io.File;
import java.io.IOException;

import greencar77.jump.VelocityManager;
import greencar77.jump.model.Model;

public abstract class Generator<M extends Model> extends AbstractGenerator {
    protected static final VelocityManager TEMPLATE_MANAGER = VelocityManager.getVelocityManager();

    private static final boolean CLEAN_TEMPLATE_CONTENTS_ONLY = true;  //avoid problem ("java.io.IOException: Unable to delete directory") if folder is opened in a command window
    protected static final String INSTRUCTIONS_FILENAME = "instructions.txt";

    //protected String pathOffset;
    protected M model;
    
    public Generator(M model) {
        super(model.getProjectFolder());

        this.model = model;
    }
    
    /*
    public Generator(String pathOffset, String projectFolder, M model) {
        Validate.notEmpty(projectFolder);
        Validate.isTrue(projectFolder.indexOf("\\") == -1 && projectFolder.indexOf("/") == -1. "projectFolder must contain only folder name");

        this.projectFolder = projectFolder;
        this.model = model;
    }*/

    @Override
    public final void generate() {
        System.out.println(this.getClass().getSimpleName());
        clean();
        generateContent();
        if (model.isRoot()) {
            generateInstructions();
        }
    }

    protected abstract void generateInstructions();

    private void clean() {
        final File templateFolder = new File(OUTPUT_PATH + model.getProjectFolder());
        if (CLEAN_TEMPLATE_CONTENTS_ONLY) {
            if (templateFolder.listFiles() != null) {
                for (File file: templateFolder.listFiles()) {
                    if (file.getAbsolutePath().contains(".idea")) {
                        //full project re-import into IntelliJ will be required if project files will be missing
                        continue;
                    }
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
}
