package greencar77.jump.builder.java;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

import greencar77.jump.Utils;
import greencar77.jump.builder.Builder;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.MetaSpringContext;
import greencar77.jump.model.java.classfile.Method;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.java.SpringConfigBasis;

public class MavenProjBuilder<S, M> extends Builder<MavenProjSpec, MavenProjModel> {
    protected static final String DEFAULT_MAIN_CLASS_NAME = "App";
    
    //Having result object as instance variable relieves us from passing it around in method signatures.
    //This is the most heavily used variable in this builder.
    protected MavenProjModel model = new MavenProjModel();

    public MavenProjBuilder() {}
    
    public MavenProjBuilder(MavenProjSpec spec) {
        super(spec);
    }

    @Override
    protected MavenProjModel buildModel() {
        Validate.notNull(getSpec());
        
        model.setProjectFolder(getSpec().getProjectName());
        model.setJavaVersion(getSpec().getJavaVersion());
        model.setConfigBasis(getSpec().getSpring().getConfigBasis());
        
        //config project
        Pom pom = new Pom(getSpec().getGroupId(), getSpec().getArtifactId());
        model.setPom(pom);        
        pom.setPackaging(getPackagingType()); //TODO enum
        pom.getBuild().finalName = getSpec().getArtifactId(); //this name will be used as build (jar/war) file name
        pom.properties.put("maven.compiler.target", getSpec().getJavaVersion().getId());
        pom.properties.put("maven.compiler.source", getSpec().getJavaVersion().getId());

        if (getSpec().getAppGenerator() != null) {
            invoke(getSpec().getAppGenerator());
        }

        addDirectDependencies();

        return model;
    }
    
    @Override
    protected void setupDefault() {
        super.setupDefault();
        
        if (getSpec().getGroupId() == null) {
            getSpec().setGroupId("x.y");
        }
        if (getSpec().getArtifactId() == null) {
            getSpec().setArtifactId(getSpec().getProjectName());
        }
        if (getSpec().getAppGenerator() == null) {
            getSpec().setAppGenerator("buildAppMulti");
        }
    }
    
    protected String getPackagingType() {
        return "jar";
    }

    protected void addDirectDependencies() {
        PreferenceConfig preferenceConfig = getPreferenceConfig();

        ArtifactSolver artifactSolver = new ArtifactSolver(preferenceConfig);
        Set<String> consolidatedImportedClassList = new HashSet<>();

        //source classes
        for (ClassFile clazz: model.getClassFiles()) {
            consolidatedImportedClassList.addAll(clazz.imports);
        }

        for (String absoluteClass: consolidatedImportedClassList) {
            if (absoluteClass.startsWith(getSpec().getRootPackage())) {
                continue; //resolve only third party classes
            }
            String artifact = artifactSolver.getArtifact(absoluteClass);
            if (artifact != null) {
                System.out.println(absoluteClass + ":" + artifact);
                model.getPom().addDependencyImported(artifact);
            }
        }

        //test classes //TODO duplicate code
        consolidatedImportedClassList = new HashSet<>();
        for (ClassFile clazz: model.getTestClassFiles()) {
            consolidatedImportedClassList.addAll(clazz.imports);
        }

        for (String absoluteClass: consolidatedImportedClassList) {
            if (absoluteClass.startsWith(getSpec().getRootPackage())) {
                continue; //resolve only third party classes
            }
            String artifact = artifactSolver.getArtifact(absoluteClass);
            if (artifact != null) {
                System.out.println(absoluteClass + ":" + artifact);
                model.getPom().addDependencyTesting(artifact);
            }
        }
    }

    protected PreferenceConfig getPreferenceConfig() {
        return null;
    }


    protected void buildAppMulti() {

        ClassFile mainClass = new ClassFile(getSpec().getRootPackage(), "App");
        Method method = new Method(mainClass, true, null, "main", "String[] args");
        mainClass.getMethods().add(method);
        model.getClassFiles().add(mainClass);
        model.setMainClass(mainClass);

        if (getSpec().isFeatureSpring()) {
            buildAppSpring();
        }
        if (getSpec().isFeatureExcel()) {
            buildAppExcel();
        }
        if (getSpec().isFeatureUnitTests()) {
            appendUnitTests();
        }
    }
    
    protected void appendUnitTests() {
        for (ClassFile sourceClass: model.getClassFiles()) {
            ClassFile testClass = new ClassFile(sourceClass.packageName, sourceClass.getClassName() + "Test");
            for (Method method: sourceClass.getMethods()) {
                Method testMethod = new Method(testClass, false, null, method.getName() + "Test", null);
                testMethod.annotations.add("@Test");
                testClass.imports.add("org.junit.Test");
                testClass.getMethods().add(testMethod);
            }
            model.getTestClassFiles().add(testClass);
        }
    }

    protected void buildAppSpring() {
        Method method;

        //bean class
        ClassFile beanOne = newClass(getSpec().getRootPackage() + ".domain", "Alpha");
        method = newMethod(beanOne, false, "int", "getFive", null);
        addMethodContent(method, "return 5;");

        ClassFile beanTwo = newClass(getSpec().getRootPackage() + ".domain", "Beta");
        method = newMethod(beanTwo, false, "int", "getTen", null);
        addMethodContent(method, "return 10;");

        MetaSpringContext springContext = new MetaSpringContext("ctx")
                .registerBean(beanOne, "alphaBean")
                .registerBean(beanTwo, "betaBean");
        model.setSpringContext(springContext);

        ClassFile springDemo = newClass(getSpec().getRootPackage(), "SpringDemo");
        method = newMethod(springDemo, false, (String) null, "run", null);
        if (getSpec().getSpring().getConfigBasis() == SpringConfigBasis.XML) {
            addMethodContent(method,
                    "ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {\"" + springContext.getId() + ".xml\"});"
                    );
            springDemo.imports.add("org.springframework.context.ApplicationContext");
            springDemo.imports.add("org.springframework.context.support.ClassPathXmlApplicationContext");
        } else { //JAVA
            //https://www.tutorialspoint.com/spring/spring_java_based_configuration.htm
            ClassFile springConfigClass = createConfigClass(springContext);
            addMethodContent(method,
                    "ApplicationContext context = new AnnotationConfigApplicationContext(" + springConfigClass.className + ".class" + ");"
                    );
            springDemo.imports.add("org.springframework.context.ApplicationContext");
            springDemo.imports.add("org.springframework.context.annotation.AnnotationConfigApplicationContext");
        }

        addMethodContent(model.getMainClass().getMethods().get(0),
                "new SpringDemo().run();"
                );
    }

    protected ClassFile createConfigClass(MetaSpringContext springContext) {
        Validate.isTrue(getSpec().getSpring().getConfigBasis() == SpringConfigBasis.JAVA);

        ClassFile springConfigClass = newClass(getSpec().getRootPackage(), "SpringConfig");
        addAnnotation(springConfigClass, "@Configuration", "org.springframework.context.annotation.Configuration");
        springContext.getBeans().stream().forEach(b -> {
            Method beanMethod = newMethod(springConfigClass, false, b.getClassFile(),
                    Utils.toLowerCaseFirst(b.getClassFile().className), null);
            addAnnotation(beanMethod, "@Bean", "org.springframework.context.annotation.Bean");
            addMethodContent(beanMethod,
                    "return new " + b.getClassFile().className + "();"
                    );
        });

        return springConfigClass;
    }

    protected ClassFile newClass(String packageName, String className) {
        ClassFile result = new ClassFile(packageName, className);
        model.getClassFiles().add(result);
        return result;
    }

    //Example "public static void getFive()"
    protected Method newMethod(ClassFile clazz, String methodSpec) { //TODO unfinished
        Validate.isTrue(methodSpec.indexOf("(") > -1);
        String beforeParenth = methodSpec.substring(0, methodSpec.indexOf("("));
        String afterParenth = methodSpec.substring(methodSpec.indexOf("("));

        List<String> tokens = Arrays.asList(beforeParenth.split(" "));
        boolean isStatic = false;
        if (tokens.contains("static")) {
            isStatic = true;
        }

        String methodName = tokens.get(tokens.size() - 1); //the last one (immediately before parentheses)
        String signature = afterParenth.substring(0, afterParenth.indexOf(")"));
        if (signature.length() == 0) {
            signature = null;
        }

        String returnClass = null; //TODO

        Method result = new Method(clazz, isStatic, returnClass, methodName, signature);
        clazz.getMethods().add(result);
        return result;
    }

    protected Method newMethod(ClassFile clazz, boolean staticFlag, String returnType, String name, String signature) {
        Method result = new Method(clazz, staticFlag, returnType, name, signature);
        clazz.getMethods().add(result);
        return result;
    }

    protected Method newMethod(ClassFile clazz, boolean staticFlag, ClassFile returnClass, String name, String signature) {
        Method result = new Method(clazz, staticFlag, returnClass.className, name, signature);
        clazz.getMethods().add(result);
        clazz.imports.add(returnClass.getFullName());
        return result;
    }

    protected void addAnnotation(Method method, String annotation, String... imports) {
        method.annotations.add(annotation);
        for (String importString: imports) {
            method.getClassFile().imports.add(importString);
        }
    }

    protected void addAnnotation(ClassFile clazz, String annotation, String... imports) {
        clazz.annotations.add(annotation);
        for (String importString: imports) {
            clazz.imports.add(importString);
        }
    }
    
    protected void addMethodContent(Method method, String... lines) {
        method.getContent().append(code(indent(TAB + TAB,
                lines
                )));
    }

    protected void buildAppExcel() {
        Method method;

        ClassFile clazz = new ClassFile(getSpec().getRootPackage(), "ExcelReader");
        method = new Method(clazz, false, null, "run", null);
        method.getContent().append(code(indent(TAB + TAB,
                "try {",
                TAB + "XSSFWorkbook workbook = new XSSFWorkbook(\"sample1.xlsx\");",
                "} catch (IOException e) {",
                TAB + "throw new RuntimeException(e);",
                "}"
                )));
        clazz.imports.add("org.apache.poi.xssf.usermodel.XSSFWorkbook");
        clazz.imports.add("java.io.IOException");        
        clazz.getMethods().add(method);
        model.getClassFiles().add(clazz);
    }
}
