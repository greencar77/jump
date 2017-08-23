package greencar77.jump.model.java.maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import greencar77.jump.model.java.classfile.ClassFile;

public class WebDescriptor {
    private String filename = "web.xml";
    
    public Map<String, String> contextParams = new HashMap<String, String>();
    public Set<ServletWebDescriptor> servlets = new HashSet<ServletWebDescriptor>();
    public Set<ServletMappingWebDescriptor> servletMappings = new HashSet<ServletMappingWebDescriptor>();
    public List<String> listeners = new ArrayList<>();
    private StringBuilder securitySection;

    public WebDescriptor() {}

    public WebDescriptor(String filename) {
        super();
        this.filename = filename;
    }

    public ServletWebDescriptor registerServlet(ClassFile classFile, String name, String endpoint) {
        ServletWebDescriptor servletWebDescriptor = new ServletWebDescriptor(name, classFile.packageName + "." + classFile.className, null);
        servlets.add(servletWebDescriptor);
        servletMappings.add(new ServletMappingWebDescriptor(name, endpoint));
        return servletWebDescriptor;
    }
    
    public ServletWebDescriptor registerServletThirdParty(String classNameFull, String name, String endpoint, Integer loadOnStartup) {
        ServletWebDescriptor servletWebDescriptor = new ServletWebDescriptor(name, classNameFull, loadOnStartup);
        servlets.add(servletWebDescriptor);
        servletMappings.add(new ServletMappingWebDescriptor(name, endpoint));
        return servletWebDescriptor;
    }
    
    public void registerServletThirdParty(ServletWebDescriptor servlet, String endpoint) {
        servlets.add(servlet);
        servletMappings.add(new ServletMappingWebDescriptor(servlet.servletName, endpoint));
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public StringBuilder getSecuritySection() {
        return securitySection;
    }

    public void setSecuritySection(StringBuilder securitySection) {
        this.securitySection = securitySection;
    }

}
