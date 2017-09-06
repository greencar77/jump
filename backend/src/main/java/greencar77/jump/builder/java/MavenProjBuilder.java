package greencar77.jump.builder.java;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

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
        Method method = new Method(true, null, "main", "String[] args");
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
                Method testMethod = new Method(false, null, method.getName() + "Test", null);
                testMethod.annotations.add("@Test");
                testClass.imports.add("org.junit.Test");
                testClass.getMethods().add(testMethod);
            }
            model.getTestClassFiles().add(testClass);
        }
    }


    protected void buildAppSpring() {
        ClassFile clazz;
        Method method;

        //bean class
        ClassFile beanClass = new ClassFile(getSpec().getRootPackage() + ".domain", "Alpha");
        method = new Method(false, "int", "getFive", null);
        method.getContent().append(code(indent(TAB + TAB,
                "return 5;"
                )));
        beanClass.getMethods().add(method);
        model.getClassFiles().add(beanClass);
        
        ClassFile beanTwo = new ClassFile(getSpec().getRootPackage() + ".domain", "Beta");
        method = new Method(false, "int", "getTen", null);
        method.getContent().append(code(indent(TAB + TAB,
                "return 10;"
                )));
        beanClass.getMethods().add(method);
        model.getClassFiles().add(beanTwo);
        
        MetaSpringContext springContext = new MetaSpringContext("ctx");
        springContext.registerBean(beanClass, "alphaBean");
        springContext.registerBean(beanTwo, "betaBean");
        
        clazz = new ClassFile(getSpec().getRootPackage(), "SpringDemo");
        method = new Method(false, null, "run", null);
        if (getSpec().getSpring().getConfigBasis() == SpringConfigBasis.XML) {
            method.getContent().append(code(indent(TAB + TAB,
                    "ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {\"" + springContext.getId() + ".xml\"});"
                    )));
            clazz.imports.add("org.springframework.context.ApplicationContext");
            clazz.imports.add("org.springframework.context.support.ClassPathXmlApplicationContext");
        } else { //JAVA
            //https://www.tutorialspoint.com/spring/spring_java_based_configuration.htm
            ClassFile springConfigClass = createConfigClass(springContext);
            method.getContent().append(code(indent(TAB + TAB,
                    "ApplicationContext context = new AnnotationConfigApplicationContext(" + springConfigClass.className + ".class" + ");"
                    )));
            clazz.imports.add("org.springframework.context.ApplicationContext");
            clazz.imports.add("org.springframework.context.annotation.AnnotationConfigApplicationContext");
        }
        clazz.getMethods().add(method);
        model.getClassFiles().add(clazz);
        
        model.getMainClass().getMethods().get(0).getContent().append(code(indent(TAB + TAB,
                "new SpringDemo().run();")
                ));
        
        model.setSpringContext(springContext);
    }

    protected ClassFile createConfigClass(MetaSpringContext springContext) {
        if (getSpec().getSpring().getConfigBasis() == SpringConfigBasis.JAVA) {
            ClassFile springConfigClass = new ClassFile(getSpec().getRootPackage(), "SpringConfig");
            springConfigClass.annotations.add("@Configuration");
            springConfigClass.imports.add("org.springframework.context.annotation.Configuration");
            model.getClassFiles().add(springConfigClass);
            springContext.getBeans().stream().forEach(b -> {
                Method beanMethod = new Method(false, b.getClassFile().className, b.getClassFile().className, null);
                beanMethod.annotations.add("@Bean");
                springConfigClass.imports.add("org.springframework.context.annotation.Bean");
                beanMethod.getContent().append(code(indent(TAB + TAB,
                        "return new " + b.getClassFile().className + "();"
                        )));
                springConfigClass.getMethods().add(beanMethod);
                springConfigClass.imports.add(b.getClassFile().getFullName());
            });

            return springConfigClass;
        }

        throw new RuntimeException();
    }

    protected void buildAppExcel() {
        Method method;

        ClassFile clazz = new ClassFile(getSpec().getRootPackage(), "ExcelReader");
        method = new Method(false, null, "run", null);
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
