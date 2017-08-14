package greencar77.jump.model.java.maven;

import java.util.ArrayList;
import java.util.List;

import greencar77.jump.generator.Generator;

public class JaxbPluginPom extends PluginPom {
    
    public List<String> executions = new ArrayList<String>();
    private String projectPackage;

    public JaxbPluginPom(String version, String projectPackage) {
        super("org.jvnet.jaxb2.maven2", "maven-jaxb2-plugin", version);
        this.projectPackage = projectPackage;
    }
    
    @Override
    protected StringBuilder getExecutions(String offset) {
        //http://lauraliparulo.altervista.org/jaxb-part-8-generate-classes-from-dtd-with-maven/
        //https://jaxb.java.net/guide/Compiling_DTD.html
        StringBuilder sb = new StringBuilder();
        
        sb.append(offset + "<executions>" + Generator.LF);
        for (String execution: executions) {
            String[] parts = execution.split(";");
            sb.append(offset + Generator.TAB + "<execution>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + "<id>" + parts[1] + "</id>" + Generator.LF);

            sb.append(offset + Generator.TAB + Generator.TAB + "<goals>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + "<goal>generate</goal>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + "</goals>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + "<configuration>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + "<generateDirectory>target/generated-sources/" + parts[1] + "</generateDirectory>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + "<generatePackage>" + projectPackage + ".domain." + parts[1] + "</generatePackage>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + "<schemaIncludes>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + Generator.TAB + "<value>" + parts[0] + "</value>" + Generator.LF);
            sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + "</schemaIncludes>" + Generator.LF);
            //sb.append(offset + Generator.TAB + Generator.TAB + Generator.TAB + "<extension>true</extension>" + Generator.LF);            
            sb.append(offset + Generator.TAB + Generator.TAB + "</configuration>" + Generator.LF);
            sb.append(offset + Generator.TAB + "</execution>" + Generator.LF);
        }
        sb.append(offset + "</executions>" + Generator.LF);
        return sb;
    }

}
