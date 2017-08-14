package greencar77.jump;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import greencar77.jump.model.RawFile;

public class FileUtils {
    private static final VelocityManager TEMPLATE_MANAGER = VelocityManager.getVelocityManager();

    public static File saveFile(String filename, byte[] content) {
        FileOutputStream fos = null;

        try {
            File file = new File(filename);
            System.out.println(file.getAbsolutePath());
            fos = new FileOutputStream(file);
            if (content == null) {
                content = "".getBytes();
            }
            fos.write(content);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static File saveFileForced(String filename, byte[] content) {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        return saveFile(filename, content);
    }
    
    public static String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", "/") + "/";
    }
    
    public static RawFile createRawJavaClass(String packageName, String className, byte[] content) {
        return new RawFile("src/main/java/" + FileUtils.packageToPath(packageName) + className + ".java", content);
    }

    public static RawFile createRawJavaClass(String packageName, String className, StringBuilder content) {
        return new RawFile("src/main/java/" + FileUtils.packageToPath(packageName) + className + ".java", content.toString().getBytes());
    }
    
    public static RawFile createRawJavaClassFromTemplate(String templateName, String packageName, String className) {
        Map<String, String> map = new HashMap<>();
        map.put("package", packageName);
        map.put("class", className);
        return new RawFile("src/main/java/" + FileUtils.packageToPath(packageName) + className + ".java",
                TEMPLATE_MANAGER.getFilledTemplate("templates/" + templateName, map));
    }
}
