package greencar77.jump.generator.java;

import greencar77.jump.generator.AbstractGenerator;
import greencar77.jump.model.java.MavenProjModel;
import greencar77.jump.model.java.PersistenceUnit;

public class PersistenceGenerator extends AbstractGenerator {

    private MavenProjModel model;

    public PersistenceGenerator(MavenProjModel model) {
        super(model.getProjectFolder());
        this.model = model;
    }

    @Override
    public void generate() {
        PersistenceUnit persistenceUnit = model.getPersistenceUnit();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='utf-8'?>" + LF);
        sb.append("<persistence xmlns=\"http://java.sun.com/xml/ns/persistence\"" + LF);
        sb.append(TAB + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + LF);
        sb.append(TAB + "xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd\"" + LF);
        sb.append(TAB + "version=\"2.0\">" + LF);
        sb.append(TAB + "<persistence-unit name=\"" + persistenceUnit.getName() + "\" transaction-type=\"RESOURCE_LOCAL\">" + LF);
        sb.append(TAB + TAB + "<provider>" + persistenceUnit.getProviderClass() + "</provider>" + LF);
        sb.append(TAB + TAB + "<non-jta-data-source>java:comp/env/jdbc/tutorialDS</non-jta-data-source>" + LF);
        sb.append(TAB + TAB + "<properties>" + LF);
        sb.append(TAB + TAB + TAB + "<property name=\"hibernate.show_sql\" value=\"true\" />" + LF);
        sb.append(TAB + TAB + TAB + "<property name=\"hibernate.format_sql\" value=\"true\" />" + LF);
        sb.append(TAB + TAB + TAB + "<property name=\"hibernate.dialect\" value=\"org.hibernate.dialect.DerbyDialect\" />" + LF);
        sb.append(TAB + TAB + TAB + "<property name=\"hibernate.hbm2ddl.auto\" value=\"update\" />" + LF);
        sb.append(TAB + TAB + "</properties>" + LF);
        sb.append(TAB + "</persistence-unit>" + LF);
        sb.append("</persistence>" + LF);

        saveResource("src/main/resources/META-INF/" + "persistence.xml", sb.toString().getBytes());
    }
}
