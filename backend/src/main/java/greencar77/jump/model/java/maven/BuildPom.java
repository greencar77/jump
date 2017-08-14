package greencar77.jump.model.java.maven;

import java.util.HashMap;
import java.util.Map;

public class BuildPom {
    public String finalName;
    protected Map<String, PluginPom> plugins = new HashMap<String, PluginPom>();

    public void addPlugin(String groupId, String artifactId, String version) {
        PluginPom plugin = new PluginPom(groupId, artifactId, version);
        plugins.put(plugin.getShortName(), plugin);
    }

    public void addPlugin(PluginPom plugin) {
        plugins.put(plugin.getShortName(), plugin);
    }

    public Map<String, PluginPom> getPlugins() {
        return plugins;
    }

}
