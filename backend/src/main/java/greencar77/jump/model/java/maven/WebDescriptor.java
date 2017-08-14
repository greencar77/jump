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

    public WebDescriptor() {}

    public WebDescriptor(String filename) {
        super();
        this.filename = filename;
    }

    public void registerServlet(ClassFile classFile, String name, String endpoint) {
        servlets.add(new ServletWebDescriptor(name, classFile.packageName + "." + classFile.className, null));
        servletMappings.add(new ServletMappingWebDescriptor(name, endpoint));
    }
    
    public void registerServletThirdParty(String classNameFull, String name, String endpoint, Integer loadOnStartup) {
        servlets.add(new ServletWebDescriptor(name, classNameFull, loadOnStartup));
        servletMappings.add(new ServletMappingWebDescriptor(name, endpoint));
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

}
