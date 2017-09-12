package greencar77.jump.generator.java;

import greencar77.jump.generator.AbstractGenerator;
import greencar77.jump.model.java.HibernateConfiguration;
import greencar77.jump.model.java.MavenProjModel;

public class HibernateConfigurationGenerator extends AbstractGenerator {

    private MavenProjModel model;

    public HibernateConfigurationGenerator(MavenProjModel model) {
        super(model.getProjectFolder());
        this.model = model;
    }

    @Override
    public void generate() {
        HibernateConfiguration configuration = model.getHibernateConfiguration();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='utf-8'?>" + LF);
        sb.append("<!DOCTYPE hibernate-configuration PUBLIC" + LF);
        sb.append(TAB + "\"-//Hibernate/Hibernate Configuration DTD//EN\"" + LF);
        sb.append(TAB + "\"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">" + LF);

        sb.append("<hibernate-configuration>" + LF);
        sb.append(TAB + "<session-factory>" + LF);

        //http://shengwangi.blogspot.com/2015/10/how-to-use-embedded-java-db-derby-in-maven.html
        sb.append(TAB + TAB + "<property name=\"connection.url\">jdbc:derby:memory:sample;create=true</property>" + LF);
        sb.append(TAB + TAB + "<property name=\"connection.driver_class\">org.apache.derby.jdbc.EmbeddedDriver</property>" + LF);
        sb.append(TAB + TAB + "<property name=\"dialect\">org.hibernate.dialect.DerbyDialect</property>" + LF);
/*
        <property name="connection.username">root</property>
        <property name="connection.password"></property>
        <!-- DB schema will be updated if needed -->
        <property name="hbm2ddl.auto">update</property>
        <property name="show_sql">false</property>
        <property name="format_sql">false</property>
         */
        
        configuration.getDomainClasses()
            .stream()
            .forEach(c-> { 
                sb.append(TAB + TAB + "<mapping class=\"" + c.getFullName() + "\"></mapping>" + LF);
            });

        sb.append(TAB + "</session-factory>" + LF);
        sb.append("</hibernate-configuration>" + LF);

        saveResource("src/main/resources/" + "hibernate.cfg.xml", sb.toString().getBytes());
    }
}
