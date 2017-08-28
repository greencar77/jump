package greencar77.jump.generator.java;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.maven.cli.MavenCli;

import greencar77.jump.FileUtils;
import greencar77.jump.generator.Generator;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.classfile.RestMethod;
import greencar77.jump.model.java.classfile.TemplateClass;
import greencar77.jump.model.java.maven.BuildPom;
import greencar77.jump.model.java.maven.Dependency;
import greencar77.jump.model.java.maven.DependencyScope;
import greencar77.jump.model.java.maven.PluginPom;
import greencar77.jump.model.java.maven.Pom;

public class MavenProjGenerator<M> extends Generator<MavenProjModel>
    implements ClassGenerator {

    public MavenProjGenerator(MavenProjModel model) {
        super(model);
    }

    @Override
    protected void generateContent() {
        for (RawFile rawFile: model.getRawFiles()) {
            saveResource(rawFile.getPath(), rawFile.getContent());
        }

        byte[] pom = generatePom();        
        saveResource("pom.xml", pom);
        
        mavenBuild();
    }

    protected void generateClassFiles() {
        for (ClassFile file: model.getClassFiles()) {            
            model.getRawFiles().add(FileUtils.createRawJavaClass(file.packageName, file.className, file.generateWith(this)));
        }        
    }

    @Override
    public StringBuilder generate(TemplateClass template) {
        StringBuilder sb = new StringBuilder();

        sb.append(new String(TEMPLATE_MANAGER.getFilledTemplate("templates/" + template.getTemplateName(), template.getProperties())));

        return sb;
    }

    protected byte[] generatePom() {
        StringBuilder sb = new StringBuilder();
        
        Validate.notNull(model.getPom());
        Pom pom = model.getPom();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LF);
        sb.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        sb.append(" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">" + LF);

        sb.append(TAB + "<modelVersion>4.0.0</modelVersion>" + LF);
        sb.append(LF);
        sb.append(TAB + "<groupId>" + pom.getGroupId() + "</groupId>" + LF);
        sb.append(TAB + "<artifactId>" + pom.getArtifactId() + "</artifactId>" + LF);
        sb.append(TAB + "<version>1.0-SNAPSHOT</version>" + LF);

        sb.append(TAB + "<packaging>" + pom.getPackaging() + "</packaging>" + LF);
        sb.append(LF);
        sb.append(TAB + "<name>" + pom.getArtifactId() + "</name>" + LF);
        sb.append(LF);
//        sb.append(TAB + "<properties>" + LF);
//        sb.append(TAB + TAB + "<bundle.name>${project.name}</bundle.name>" +  LF);
//        sb.append(TAB + "</properties>" +  LF);
        if (!pom.properties.isEmpty()) {
            sb.append(TAB + "<properties>" + LF);
            for (Map.Entry<Object, Object> entry: pom.properties.entrySet()) {
                sb.append(TAB + TAB + "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">" + LF);
            }
            sb.append(TAB + "</properties>" + LF);
        }
       
        sb.append(generateContent(TAB, pom.getBuild()) + LF);

        sb.append(LF);
        sb.append(TAB + "<dependencies>" + LF);
        for (Dependency dependency: pom.getDependencies()) {
            outputDependency(sb, dependency);
        }
        sb.append(TAB + "</dependencies>" + LF);

        sb.append("</project>" + LF);

        return sb.toString().getBytes();
    }
    
    protected StringBuilder generateContent(String offset, BuildPom build) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(offset + "<build>" + LF);
        if (build.finalName != null) {
            sb.append(offset + TAB + "<finalName>" + build.finalName + "</finalName>" + LF);
        }
        
        if (build.getPlugins().size() > 0) {
            String offset2 = offset + TAB;
            sb.append(offset2 + "<plugins>" + LF);
            for (PluginPom plugin: build.getPlugins().values()) {
                sb.append(plugin.generateContent(offset2 + TAB));
            }
            sb.append(offset2 + "</plugins>" + LF);
        }
        sb.append(offset + "</build>" + LF);
        return sb;        
    }


    private void outputDependency(StringBuilder sb, Dependency dependency) {
        String parts[] = dependency.getName().split("/");
        sb.append(TAB + TAB + "<dependency>" + LF);
        sb.append(TAB + TAB + TAB + "<groupId>" + parts[0] + "</groupId>" + LF);
        sb.append(TAB + TAB + TAB + "<artifactId>" + parts[1] + "</artifactId>" + LF);
        sb.append(TAB + TAB + TAB + "<version>" + parts[2] + "</version>" + LF);
        if (dependency.getScope() != DependencyScope.ANY) {
            sb.append(TAB + TAB + TAB + "<scope>" + dependency.getScope().getXmlTitle() + "</scope>" + LF);
        }
        sb.append(TAB + TAB + "</dependency>" + LF);
    }

    protected void mavenBuild() {
        String command = "clean package";
        String path = new File(OUTPUT_PATH + model.getProjectFolder() + "/").getAbsolutePath();

        //https://stackoverflow.com/questions/5141788/how-to-run-maven-from-java/19904341#19904341
        MavenCli cli = new MavenCli();
        cli.doMain(command.split(" "), path, System.out, System.out);
    }

    @Override
    public StringBuilder generate(RestClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append("package " + clazz.packageName + ";" + LF);
        sb.append(LF);
        
        if (clazz.imports.size() > 0) {
            for (String importString: clazz.imports) {
                sb.append("import " + importString + ";" + LF);            
            }
            sb.append(LF);
        }

        sb.append(generateClassAnnotations(clazz));
        sb.append("public class " + clazz.className + " " + (clazz.classNameTail == null? "": clazz.classNameTail) + " {" + LF);
        //sb.append(generateStateBody()); //TODO
        sb.append(generateBody(clazz)); //TODO
        sb.append("}" + LF);
        
        return sb;
    }
    
    protected StringBuilder generateClassAnnotations(ClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        if (clazz.classAnnotations.size() > 0) {
            for (String annotation: clazz.classAnnotations) {
                sb.append(annotation + LF);
            }
        }
        
        return sb;
    }
    
    protected StringBuilder generateMethodAnnotations(RestMethod method, String indent) {
        StringBuilder sb = new StringBuilder();

        if (method.classAnnotations.size() > 0) {
            for (String annotation: method.classAnnotations) {
                sb.append(indent + annotation + LF);
            }
        }
        
        return sb;
    }
    
    protected StringBuilder generateBody(RestClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        if (clazz.getMethods().size() > 0) {
            for (RestMethod method: clazz.getMethods()) {
                sb.append(generateMethodAnnotations(method, TAB));
                sb.append(TAB + "public " + method.getReturnType() + " " + method.getName() + "() {" + LF);
                sb.append(method.getContent()).append(LF);
                sb.append(TAB + "}" + LF);
            }
        }
        
        return sb;
    }
    protected StringBuilder generateBody(ClassFile clazz) {
        return clazz.getBody();
    }

    @Override
    public StringBuilder generate(ClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append("package " + clazz.packageName + ";" + LF);
        sb.append(LF);
        
        if (clazz.imports.size() > 0) {
            for (String importString: clazz.imports) {
                sb.append("import " + importString + ";" + LF);            
            }
            sb.append(LF);
        }

        sb.append(generateClassAnnotations(clazz));
        sb.append("public class " + clazz.className + " " + (clazz.classNameTail == null? "": clazz.classNameTail) + " {" + LF);
        //sb.append(generateStateBody()); //TODO
        sb.append(generateBody(clazz));
        sb.append("}" + LF);
        
        return sb;
    }

    @Override
    protected void generateInstructions() {
        //TODO
    }

}
