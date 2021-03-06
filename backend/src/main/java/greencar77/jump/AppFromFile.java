package greencar77.jump;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import greencar77.jump.builder.BuilderFactory;
import greencar77.jump.generator.GeneratorFactory;
import greencar77.jump.spec.Spec;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.js.AngularAppSpec;
import greencar77.jump.spec.webapp.WebAppSpec;

public class AppFromFile {
    
    private static final String PATH = "c:\\github\\greencar77\\jump\\";
    private static final String BUFFER_PATH = PATH + "buffer\\config.txt";

    public static void main(String[] args) {
        new AppFromFile().run();
    }
    
    public void run() {
        Class<? extends Spec> clazz = detectClass();
        Spec spec = loadSpec(clazz);
        GeneratorFactory.create(BuilderFactory.create(spec).build()).generate();
    }

    protected Class<? extends Spec> detectClass() {
        List<String> lines;
        try {
            lines = org.apache.commons.io.FileUtils.readLines(new File(getBufferPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        String name = null;
        for (String line: lines) {
            if (line.contains("\"level\"")) {
                name = line.substring(line.indexOf(":") + 1); //truncate key
                name = name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\""));
            }
        }
        
        return detectClass(name);
    }

    protected Class<? extends Spec> detectClass(String name) {
        if (name.equals(WebAppSpec.class.getSimpleName())) {
            return WebAppSpec.class;
        } else if (name.equals(MavenProjSpec.class.getSimpleName())) {
                return MavenProjSpec.class;
        } else if (name.equals(AngularAppSpec.class.getSimpleName())) {
            return AngularAppSpec.class;
        } else {
            throw new RuntimeException(name);
        }
    }

    protected Spec loadSpec(Class<? extends Spec> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            return mapper.readValue(new File(getBufferPath()), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected String getBufferPath() {
        return BUFFER_PATH;
    }
}
