package greencar77.jump.model.java.maven;

import greencar77.jump.generator.Generator;

public class ServletMappingWebDescriptor {
    public String servletName;
    public String urlPattern;

    public ServletMappingWebDescriptor(String servletName, String urlPattern) {
        super();
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    //@Override
    public StringBuilder generateContent(String offset) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(offset + "<servlet-mapping>" + Generator.LF);
        sb.append(offset + Generator.TAB + "<servlet-name>" + servletName + "</servlet-name>" + Generator.LF);
        sb.append(offset + Generator.TAB + "<url-pattern>" + urlPattern + "</url-pattern>" + Generator.LF);
        sb.append(offset + "</servlet-mapping>" + Generator.LF);
        return sb;        
    }

}
