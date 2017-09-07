package greencar77.jump.generator.java;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.maven.cli.MavenCli;
import org.apache.velocity.VelocityContext;

import greencar77.jump.FileUtils;
import greencar77.jump.generator.Generator;
import greencar77.jump.model.ClassType;
import greencar77.jump.model.RawFile;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.MetaBean;
import greencar77.jump.model.java.classfile.MetaSpringContext;
import greencar77.jump.model.java.classfile.RestClassFile;
import greencar77.jump.model.java.classfile.Method;
import greencar77.jump.model.java.classfile.TemplateClass;
import greencar77.jump.model.java.maven.BuildPom;
import greencar77.jump.model.java.maven.Dependency;
import greencar77.jump.model.java.maven.DependencyScope;
import greencar77.jump.model.java.maven.PluginPom;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.spec.java.SpringConfigBasis;

public class MavenProjGenerator<M> extends Generator<MavenProjModel>
    implements ClassGenerator {
    
    private static final Comparator<Dependency> DEPENDENCIES_ALPHABETICALLY = new Comparator<Dependency>() {

        @Override
        public int compare(Dependency d1, Dependency d2) {
            return d1.getFullName().compareTo(d2.getFullName());
        }
        
    };

    public MavenProjGenerator(MavenProjModel model) {
        super(model);
    }

    @Override
    protected void generateContent() {

        generateClassFiles();        

        for (RawFile rawFile: model.getRawFiles()) {
            saveResource(rawFile.getPath(), rawFile.getContent());
        }
        
        if (model.getSpringContext() != null) {
            generateSpringContext();
        }

        byte[] pom = generatePom();        
        saveResource("pom.xml", pom);
        
        mavenBuild();
    }

    protected void generateClassFiles() {
        for (ClassFile file: model.getClassFiles()) {
            model.getRawFiles().add(FileUtils.createRawJavaClass(ClassType.SOURCE, file.packageName, file.className, file.generateWith(this)));
        }        
        for (ClassFile file: model.getTestClassFiles()) {
            model.getRawFiles().add(FileUtils.createRawJavaClass(ClassType.TEST, file.packageName, file.className, file.generateWith(this)));
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
        List<Dependency> list = new ArrayList<>(pom.getDependencies());
        Collections.sort(list, DEPENDENCIES_ALPHABETICALLY);
        for (Dependency dependency: list) {
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
        sb.append(generateBody(clazz));
        sb.append("}" + LF);
        
        return sb;
    }
    
    protected StringBuilder generateClassAnnotations(ClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        if (clazz.annotations.size() > 0) {
            for (String annotation: clazz.annotations) {
                sb.append(annotation + LF);
            }
        }
        
        return sb;
    }
    
    protected StringBuilder generateMethodAnnotations(Method method, String indent) {
        StringBuilder sb = new StringBuilder();

        if (method.annotations.size() > 0) {
            for (String annotation: method.annotations) {
                sb.append(indent + annotation + LF);
            }
        }
        
        return sb;
    }
    
    protected StringBuilder generateBody(RestClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        if (clazz.getMethods().size() > 0) {
            for (Method method: clazz.getMethods()) {
                sb.append(generateMethodAnnotations(method, TAB));
                sb.append(TAB + "public" + " " + (method.getReturnType() == null? "void": method.getReturnType()) + " " + method.getName() + "() {" + LF);
                sb.append(method.getContent()).append(LF);
                sb.append(TAB + "}" + LF);
            }
        }
        
        return sb;
    }

    protected StringBuilder generateBody(ClassFile clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append(clazz.getStateBody());

        if (clazz.getMethods().size() > 0) {
            for (Method method: clazz.getMethods()) {
                sb.append(generateMethodAnnotations(method, TAB));
                sb.append(TAB + "public"
                        + (method.isStaticFlag()? " static": "")
                        + " " + (method.getReturnType() == null? "void": method.getReturnType())
                        + " " + method.getName() + "("
                        + (method.getSignature() == null? "": method.getSignature())
                        + ") {" + LF);
                sb.append(method.getContent()).append(LF);
                sb.append(TAB + "}" + LF);
            }
        }

        sb.append(clazz.getBody());

        return sb;
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
        sb.append(generateBody(clazz));
        sb.append("}" + LF);
        
        return sb;
    }

    @Override
    protected void generateInstructions() {
        //TODO
    }

    protected void generateSpringContext() {
        if (model.getConfigBasis() != SpringConfigBasis.XML) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LF);
        sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"" + LF);
        sb.append(TAB + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + LF);
                
        sb.append(TAB + "xsi:schemaLocation=\"" + LF);
        sb.append(TAB + "http://www.springframework.org/schema/beans" + LF);
        sb.append(TAB + "http://www.springframework.org/schema/beans/spring-beans-3.0.xsd" + LF);
                
        sb.append(TAB + "\">" + LF);

        MetaSpringContext context = model.getSpringContext();
        if (context.getBeans().size() > 0) {
            for (MetaBean bean: context.getBeans()) {
                sb.append(TAB + "<bean id=\"")
                .append(bean.getName())
                .append("\" class=\"").append(bean.getClassFile().getFullName())
                .append("\" />" + LF);
            }

        }
        sb.append("</beans>" + LF);

        saveResource("src/main/resources/" + context.getId() + ".xml", sb.toString().getBytes());
    }

}
