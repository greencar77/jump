package greencar77.jump.builder.java;

import static greencar77.jump.generator.CodeManager.code;
import static greencar77.jump.generator.CodeManager.indent;
import static greencar77.jump.generator.Generator.TAB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import greencar77.jump.Utils;
import greencar77.jump.builder.Builder;
import greencar77.jump.builder.ValidationException;
import greencar77.jump.model.java.HibernateConfiguration;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.PersistenceUnit;
import greencar77.jump.model.java.classfile.ClassFile;
import greencar77.jump.model.java.classfile.MetaSpringContext;
import greencar77.jump.model.java.classfile.Method;
import greencar77.jump.model.java.classfile.TemplateClass;
import greencar77.jump.model.java.classfile.XmlSpringContextNamespace;
import greencar77.jump.model.java.maven.Dependency;
import greencar77.jump.model.java.maven.DependencyScope;
import greencar77.jump.model.java.maven.Pom;
import greencar77.jump.model.java.maven.XmlSpringContext;
import greencar77.jump.spec.java.EntityManagerSetupStrategy;
import greencar77.jump.spec.java.HibernateVersion;
import greencar77.jump.spec.java.MavenProjSpec;
import greencar77.jump.spec.java.BeanDefinition;
import greencar77.jump.spec.java.BeanInstantiation;
import greencar77.jump.spec.java.UnitTestsMajorVersion;

public class MavenProjBuilder<S, M> extends Builder<MavenProjSpec, MavenProjModel> {
    public static final String IMPORT_COMMENT_PREFIX = "//#";
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
        
        model.setSpec(getSpec());
        model.setProjectFolder(getSpec().getProjectName());
        model.setJavaVersion(getSpec().getJavaVersion());
        
        if (getSpec().getSpringBoot() != null) {
            model.setSpringBootVersion(getSpec().getSpringBoot().getVersion());
        }

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
            getSpec().setAppGenerator("buildAppFeatures");
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
                model.getPom().addDependencyImported(artifact, absoluteClass);
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
                model.getPom().addDependencyTesting(artifact, absoluteClass);
            }
        }
        
        //runtime classes
        for (String absoluteClass: model.getRuntimeClass()) {
            if (absoluteClass.startsWith(getSpec().getRootPackage())) {
                continue; //resolve only third party classes
            }
            String artifact = artifactSolver.getArtifact(absoluteClass);
            if (artifact != null) {
                System.out.println(absoluteClass + ":" + artifact);
                model.getPom().addDependencyRuntime(artifact, absoluteClass);
            }
        }
        
        if (getSpec().isInvokeCli()) {
            model.getPom().getDependencies()
                .stream()
                .forEach(d -> {
                    if (!d.getScope().isRuntimeAvailable()) {
                        d.setScope(DependencyScope.ANY);
                    }
                });
        }
    }

    protected PreferenceConfig getPreferenceConfig() {
        PreferenceConfig result = new PreferenceConfig();

        if (getSpec().isFeatureHibernate()) {
            result.setHibernate(getSpec().getHibernate().getVersion().getVersionString());
            if (getSpec().getHibernate().getVersion().getIndex() >= HibernateVersion.V4_3_0.getIndex()) {
                //hibernate-core depends on jpa21 
                result.setJpa(Jpa.V2_1);
                System.out.println("JPA preference: " + result.getJpa());
            }
        }
        
        if (getSpec().isFeatureSpringBoot()) {
            result.setSpringBootInheritFromParent(true);
        }

        if (getSpec().isFeatureSpring()) {
            result.setSpringVersion(getSpec().getSpring().getVersion().getVersionString());
        }

        return result;
    }

    protected void buildAppFeatures() {

        ClassFile mainClass = new ClassFile(getSpec().getRootPackage(), "App");
        Method method = new Method(mainClass, true, null, "main", "String[] args");
        mainClass.getMethods().add(method);
        model.getClassFiles().add(mainClass);
        model.setMainClass(mainClass);

        if (getSpec().isFeatureSpring()) {
            MetaSpringContext metaSpringContext = createSpringContext();
            setupSpring(metaSpringContext);
            if (getSpec().isFeatureSpringBoot()) {
                setupSpringBoot();
            }
        }
        if (getSpec().isFeatureExcel()) {
            appendExcel();
        }
        if (getSpec().isFeatureMqRabbit()) {
            appendMqRabbit();
        }
        if (getSpec().isFeatureHibernate()) {
            appendHibernate();
        }

        //now classes for other features are generated
        if (getSpec().isFeatureUnitTests()) {
            appendUnitTests();
        }
    }
    
    protected void appendUnitTests() {
        if (getSpec().getUnitTests().getMajorVersion() == UnitTestsMajorVersion.V5) {
            appendUnitTests5();
        } else { //junit 3, 4
            appendUnitTestsVintage();
        }
        
    }
    
    protected void appendUnitTestsVintage() {
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
    
    protected void appendUnitTests5() {
        for (ClassFile sourceClass: model.getClassFiles()) {
            ClassFile testClass = new ClassFile(sourceClass.packageName, sourceClass.getClassName() + "Test");
            for (Method method: sourceClass.getMethods()) {
                Method testMethod = new Method(testClass, false, null, method.getName() + "Test", null);
                testMethod.annotations.add("@Test");
                testClass.imports.add("org.junit.jupiter.api.Test");
                testClass.getMethods().add(testMethod);
            }
            model.getTestClassFiles().add(testClass);
        }
    }

    protected void setupSpring(MetaSpringContext metaSpringContext) {
        Method method;
        
        if (getSpec().getSpring().getBeanInstantiation().isRequireXml()) {
            XmlSpringContext xmlSpringContext = new XmlSpringContext(metaSpringContext);
            model.setXmlSpringContext(xmlSpringContext);
        }

        ClassFile springDemo = newClass(getSpec().getRootPackage(), "SpringDemo");
        method = newMethod(springDemo, false, (String) null, "run", null);
        if (getSpec().getSpring().getBeanInstantiation().isRequireJavaConfig()) {
            //https://www.tutorialspoint.com/spring/spring_java_based_configuration.htm
            ClassFile springConfigClass = createConfigClass(metaSpringContext);
            addContent(method,
                    "//#org.springframework.context.ApplicationContext",
                    "//#org.springframework.context.annotation.AnnotationConfigApplicationContext",
                    "ApplicationContext context = new AnnotationConfigApplicationContext(" + springConfigClass.className + ".class" + ");"
                    );
        } else if (getSpec().getSpring().getBeanInstantiation().isRequireXml()) {
            if (getSpec().getSpring().getBeanDefinition() == BeanDefinition.ANNOTATED_CLASS) {
                metaSpringContext.setComponentScanBasePackage(getSpec().getRootPackage());
                model.getXmlSpringContext().addNamespace(XmlSpringContextNamespace.CONTEXT);
            }
            addContent(method,
                    "//#org.springframework.context.ApplicationContext",
                    "//#org.springframework.context.support.ClassPathXmlApplicationContext",
                    "ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {\"" + metaSpringContext.getId() + ".xml\"});"
                    );
        } else {
            throw new RuntimeException("No Spring config");
        }

        addContent(model.getMainClass().getMethods().get(0),
                "new " + springDemo.className + "().run();"
                );
    }
    
    protected MetaSpringContext createSpringContext() {
        Method method;

        //bean class
        ClassFile beanOne = newClass(getSpec().getRootPackage() + ".domain", "Alpha");
        method = newMethod(beanOne, false, "int", "getFive", null);
        addContent(method, "return 5;");

        ClassFile beanTwo = newClass(getSpec().getRootPackage() + ".domain", "Beta");
        method = newMethod(beanTwo, false, "int", "getTen", null);
        addContent(method, "return 10;");

        MetaSpringContext result = new MetaSpringContext("ctx")
                .registerBean(beanOne, "alphaBean")
                .registerBean(beanTwo, "betaBean");

        if (getSpec().getSpring().getBeanDefinition() == BeanDefinition.ANNOTATED_CLASS) {
            result.getBeans().stream().forEach(b -> {
                addAnnotation(b.getClassFile(), "@Component", "org.springframework.stereotype.Component");
            });
        }

        return result;
    }

    protected ClassFile createConfigClass(MetaSpringContext springContext) {
        Validate.isTrue(getSpec().getSpring().getBeanInstantiation().isRequireJavaConfig());

        ClassFile springConfigClass = newClass(getSpec().getRootPackage(), "SpringConfig");
        addAnnotation(springConfigClass, "@Configuration", "org.springframework.context.annotation.Configuration");
        
        switch (getSpec().getSpring().getBeanInstantiation()) {
            case XML: throw new RuntimeException();
            case JAVA_CONFIG:
                if (getSpec().getSpring().getBeanDefinition() == BeanDefinition.CLASS) {
                    springContext.getBeans().stream().forEach(b -> {
                        Method beanMethod = newMethod(springConfigClass, false, b.getClassFile(),
                                Utils.toLowerCaseFirst(b.getClassFile().className), null);
                        addAnnotation(beanMethod, "@Bean", "org.springframework.context.annotation.Bean");
                        addContent(beanMethod,
                                "return new " + b.getClassFile().className + "();"
                                );
                    });
                } else if (getSpec().getSpring().getBeanDefinition() == BeanDefinition.ANNOTATED_CLASS) {
                    addAnnotation(springConfigClass, "@ComponentScan", "org.springframework.context.annotation.ComponentScan");
                } else {
                    throw new RuntimeException(getSpec().getSpring().getBeanDefinition().name());
                }
                break;
            case JAVA_CONFIG_XML:
                addAnnotation(springConfigClass, "@ImportResource(\"classpath:" + springContext.getId() + ".xml\")", "org.springframework.context.annotation.ImportResource");
                break;
            default: throw new RuntimeException(getSpec().getSpring().getBeanInstantiation().name());
        }
        

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
    
    protected void addContent(Method method, String... lines) {
        List<String> meaningfulLines = new ArrayList<>();
        for (String line: lines) {
            if (line.startsWith(IMPORT_COMMENT_PREFIX)) {
                method.getClassFile().imports.add(line.substring(IMPORT_COMMENT_PREFIX.length()));
            } else {
                meaningfulLines.add(line);
            }
        }

        lines = meaningfulLines.toArray(new String[0]);

        method.getContent().append(code(indent(TAB + TAB,
                lines
                )));
    }
    
    protected void addContent(ClassFile clazz, String... lines) {
        List<String> meaningfulLines = new ArrayList<>();
        for (String line: lines) {
            if (line != null && line.startsWith(IMPORT_COMMENT_PREFIX)) {
                clazz.imports.add(line.substring(IMPORT_COMMENT_PREFIX.length()));
            } else {
                meaningfulLines.add(line);
            }
        }

        lines = meaningfulLines.toArray(new String[0]);

        clazz.getStateBody().append(code(indent(TAB,
                lines
                )));
    }

    protected void appendExcel() {
        ClassFile clazz = newClass(getSpec().getRootPackage(), "ExcelReader");
        Method method = newMethod(clazz, false, (String) null, "run", null);
        addContent(method,
                "try {",
                "//#org.apache.poi.xssf.usermodel.XSSFWorkbook",
                TAB + "XSSFWorkbook workbook = new XSSFWorkbook(\"sample1.xlsx\");",
                "//#java.io.IOException",
                "} catch (IOException e) {",
                TAB + "throw new RuntimeException(e);",
                "}"
                );

        addContent(model.getMainClass().getMethods().get(0),
                "//#" + clazz.getFullName(),
                "new " + clazz.className + "().run();"
                );
    }

    protected void appendMqRabbit() {
        ClassFile clazz = newClass(getSpec().getRootPackage(), "RabbitClient");
        addContent(clazz,
                "private final static String QUEUE_NAME = \"hello\";",
                ""
                );

        //https://www.rabbitmq.com/tutorials/tutorial-one-java.html
        Method method = newMethod(clazz, false, (String) null, "run", null);
        addContent(method,
                "//#com.rabbitmq.client.ConnectionFactory",
                "ConnectionFactory factory = new ConnectionFactory();",
                "factory.setHost(\"localhost\");",
                "",
                "try {",
                "//#com.rabbitmq.client.Connection",
                TAB + "Connection connection = factory.newConnection();",
                "//#com.rabbitmq.client.Channel",
                TAB + "Channel channel = connection.createChannel();",
                TAB + "channel.queueDeclare(QUEUE_NAME, false, false, false, null);",
                TAB + "String message = \"Hello World!\";",
                TAB + "channel.basicPublish(\"\", QUEUE_NAME, null, message.getBytes(\"UTF-8\"));",
                TAB + "System.out.println(\" [x] Sent '\" + message + \"'\");",
                TAB + "channel.close();",
                TAB + "connection.close();",
                "//#java.io.IOException",
                "} catch (IOException e) {",
                TAB + "throw new RuntimeException(e);",
                "//#java.util.concurrent.TimeoutException",
                "} catch (TimeoutException e) {",
                TAB + "throw new RuntimeException(e);",
                "}"
                );

        addContent(model.getMainClass().getMethods().get(0),
                "//#" + clazz.getFullName(),
                "new " + clazz.className + "().run();"
                );
    }

    protected void appendHibernate() {
        Validate.isTrue(getSpec().getHibernate().getEntityManagerSetupStrategy() == EntityManagerSetupStrategy.PROGRAMMATICALLY);

        PersistenceUnit persistenceUnit = setupHibernate();
        //<!-- https://stackoverflow.com/a/22103666 -->
        //<!-- provider>org.hibernate.ejb.HibernatePersistence</provider-->
        persistenceUnit.setProviderClass("org.hibernate.jpa.HibernatePersistenceProvider");
        model.getRuntimeClass().add("org.hibernate.jpa.HibernatePersistenceProvider");
        model.setPersistenceUnit(persistenceUnit);
        
        //http://www.alexecollins.com/tutorial-hibernate-jpa-part-1/index.html
        
        Map<String, String> props = new HashMap<>();
        String domainPackage = getSpec().getRootPackage() + ".domain";
        props.put("package", domainPackage);
        ClassFile userClass = new TemplateClass(domainPackage, "User", "hiber_domain/User.java", props);
        userClass.imports.add("javax.persistence.Column");
        userClass.imports.add("javax.persistence.Entity");
        //...and others
        model.getClassFiles().add(userClass);
        ClassFile roleClass = new TemplateClass(domainPackage, "Role", "hiber_domain/Role.java", props);
        model.getClassFiles().add(roleClass);
        roleClass.imports.add("javax.persistence.Column");
        roleClass.imports.add("javax.persistence.Entity");
        //...and others
        
        ClassFile clazz = newClass(getSpec().getRootPackage() + ".hiber", "EntityManagerDemo");
        clazz.classNameTail = "implements Runnable";

        Method setupMethod = newMethod(clazz, false, (String) null, "setup", null);
        setupMethod.setThrowsClause("throws Exception");
        addContent(setupMethod,
                "//#org.apache.naming.java.javaURLContextFactory",
                "//#javax.naming.Context",
                "System.setProperty(Context.INITIAL_CONTEXT_FACTORY, javaURLContextFactory.class.getName());",
                "System.setProperty(Context.URL_PKG_PREFIXES, \"org.apache.naming\");",
                "",
                "//#javax.naming.InitialContext",
                "InitialContext ic = new InitialContext();",
                "ic.createSubcontext(\"java:\");",
                "ic.createSubcontext(\"java:comp\");",
                "ic.createSubcontext(\"java:comp/env\");",
                "ic.createSubcontext(\"java:comp/env/jdbc\");",
                "",
                "//#org.apache.derby.jdbc.EmbeddedDataSource",
                "EmbeddedDataSource ds = new EmbeddedDataSource();",
                "ds.setDatabaseName(\"tutorialDB\");",
                "// tell Derby to create the database if it does not already exist",
                "ds.setCreateDatabase(\"create\");",
                "ic.bind(\"java:comp/env/jdbc/tutorialDS\", ds);"
                );

        Method demoMethod = newMethod(clazz, false, (String) null, "demo", null);
        addContent(demoMethod,
                "//#javax.persistence.EntityManager",
                "//#javax.persistence.Persistence",
                "EntityManager entityManager = Persistence.createEntityManagerFactory(\"" + persistenceUnit.getName() + "\").createEntityManager();",
                "entityManager.getTransaction().begin();",
                "//#" + userClass.getFullName(),
                "User user = new User();",
                "//#java.util.Date",
                "user.setName(Long.toString(new Date().getTime()));",
                "entityManager.persist(user);",
                "entityManager.getTransaction().commit();",
                "System.out.println(\"user=\" + user + \", user.id=\" + user.getId());",
                "User foundUser = entityManager.find(User.class, user.getId());",
                "System.out.println(\"foundUser=\" + foundUser);",
                "entityManager.close();",
                ""
                );

        Method method = newMethod(clazz, false, (String) null, "run", null);
        addContent(method,
                "try {",
                TAB + setupMethod.getName() + "();",
                "} catch (Exception e) {",
                TAB + "throw new RuntimeException(e);",
                "}",
                demoMethod.getName() + "();"
                );

        addContent(model.getMainClass().getMethods().get(0),
                "//#" + clazz.getFullName(),
                "new " + clazz.className + "().run();"
                );
        
        HibernateConfiguration hibernateConfiguration = new HibernateConfiguration();
        hibernateConfiguration.getDomainClasses().add(roleClass);
        hibernateConfiguration.getDomainClasses().add(userClass);
        model.setHibernateConfiguration(hibernateConfiguration);
    }
    
    protected PersistenceUnit setupHibernate() {
        PersistenceUnit persistenceUnit = new PersistenceUnit();
        persistenceUnit.setName("pu1");
        
        return persistenceUnit;
    }

    protected void setupSpringBoot() {
        Validate.notNull(getSpec().getSpringBoot());

        Dependency parentDependency = new Dependency("org.springframework.boot/spring-boot-starter-parent/" + model.getSpringBootVersion().getVersionString());
        model.getPom().setParent(parentDependency);

        model.getPom().addDependencyRuntime("org.springframework.boot/spring-boot-starter", null);
        
        //without this maven-compiler-plugin:3.1:compile throws "class file for org.springframework.core.io.DefaultResourceLoader not found"
        //spring-core is included in org.springframework.boot:spring-boot-starter, but it has "runtime" scope
        model.getPom().addDependencyImported("org.springframework/spring-core", null); //version will be inherited from spring-boot parent

        model.getPom().getBuild().addPlugin("org.springframework.boot", "spring-boot-maven-plugin", null);

        //Spring invocation
        model.getMainClass().annotations.add("@SpringBootApplication");
        model.getMainClass().imports.add("org.springframework.boot.autoconfigure.SpringBootApplication");

        addContent(model.getMainClass().getMethods().get(0),
                "//#org.springframework.boot.SpringApplication",
                "SpringApplication.run(" + model.getMainClass().className + ".class, args);"
                );
    }

    @Override
    protected void validate() {
        super.validate();

        if (getSpec().isFeatureSpringBoot()) {
            if (!getSpec().isFeatureSpring()) {
                throw new ValidationException("Spring is required for SpringBoot");
            }
            
            if (!getSpec().getSpring().getVersion().getVersionString().equals(getSpec().getSpringBoot().getVersion().getSpring())) {
                throw new ValidationException("Incompatible versions: SpringBoot requires " + getSpec().getSpringBoot().getVersion().getSpring() + " but user: " + getSpec().getSpring().getVersion().getVersionString());
            }
        }
    }
}
