package greencar77.jump.model.java.maven;

import java.util.Map;
import java.util.Properties;

import greencar77.jump.generator.Generator;

public class ServletWebDescriptor {
    public String servletName;
    public String servletClass;
    public Integer loadOnStartup;
    public Properties initParams = new Properties();
    
    public ServletWebDescriptor(String servletName, String servletClass, Integer loadOnStartup) {
        super();
        this.servletName = servletName;
        this.servletClass = servletClass;
        this.loadOnStartup = loadOnStartup;
    }

    //@Override
    public StringBuilder generateContent(String offset) {
        StringBuilder sb = new StringBuilder();
        
        /*
    <servlet>
        <servlet-name>HelloWorld</servlet-name>
        <servlet-class>kb.kb49.tomcatservlet.HelloServlet</servlet-class>
    </servlet>
         */
        sb.append(offset + "<servlet>" + Generator.LF);
        sb.append(offset + Generator.TAB + "<servlet-name>" + servletName + "</servlet-name>" + Generator.LF);
        sb.append(offset + Generator.TAB + "<servlet-class>" + servletClass + "</servlet-class>" + Generator.LF);
        if (!initParams.isEmpty()) {
            for (Map.Entry<Object, Object> entry: initParams.entrySet()) {
                sb.append(offset + Generator.TAB + "<init-param>" + Generator.LF);
                sb.append(offset + Generator.TAB + Generator.TAB + "<param-name>" + entry.getKey() + "</param-name>" + Generator.LF);
                sb.append(offset + Generator.TAB + Generator.TAB + "<param-value>" + entry.getValue() + "</param-value>" + Generator.LF);
                sb.append(offset + Generator.TAB + "</init-param>" + Generator.LF);
            }
        }
        if (loadOnStartup != null) {
            sb.append(offset + Generator.TAB + "<load-on-startup>" + loadOnStartup + "</load-on-startup>" + Generator.LF);
        }
        sb.append(offset + "</servlet>" + Generator.LF);
        return sb;        
    }
    
}
