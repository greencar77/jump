package greencar77.jump;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityManager {
    private static VelocityManager velocityManager = new VelocityManager();

    private VelocityManager() {
        //make it singleton
    }

    public static VelocityManager getVelocityManager() {
        return velocityManager;
    }

    public StringWriter getFilledTemplate(VelocityContext context, String templateFile) {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "src/main/resources/");
        Velocity.init(p);

        Template template = null;

        try {
            template = Velocity.getTemplate(templateFile);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException  e) {
            throw new RuntimeException(e);
        } catch (MethodInvocationException  e) {
            throw new RuntimeException(e);
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }

        StringWriter result = new StringWriter();

        template.merge(context, result);

        return result;
    }

    public byte[] getFilledTemplate(String templateFile, Properties properties) {
        
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "src/main/resources/");
        Velocity.init(p);
        
        Template template = getTemplate(templateFile);

        Enumeration<?> e = properties.propertyNames();

        VelocityContext context = new VelocityContext();

        while (e.hasMoreElements()) {
          String key = (String) e.nextElement();
          context.put(key, properties.getProperty(key));
        }
        StringWriter result = new StringWriter();
        template.merge(context, result);

        return result.getBuffer().toString().getBytes();
    }

    public byte[] getFilledTemplate(String templateFile, Map<String, String> map) {
        
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "src/main/resources/");
        Velocity.init(p);
        
        Template template = getTemplate(templateFile);

        VelocityContext context = new VelocityContext();

        if (map != null) {
            for (Map.Entry<String, String> entry: map.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
              }
        }
        StringWriter result = new StringWriter();
        template.merge(context, result);

        return result.getBuffer().toString().getBytes();
    }
    
    private Template getTemplate(String templateFile) {

        try {
            return Velocity.getTemplate(templateFile);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException  e) {
            throw new RuntimeException(e);
        } catch (MethodInvocationException  e) {
            throw new RuntimeException(e);
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }
}
