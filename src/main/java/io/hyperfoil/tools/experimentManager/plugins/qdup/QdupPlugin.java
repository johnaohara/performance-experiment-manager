package io.hyperfoil.tools.experimentManager.plugins.qdup;

import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class QdupPlugin {
//public class QdupPlugin extends BasePlugin<Plugin> implements Parser<Map<String, Object>> {

//    @Override
    public String getName() {
        return "qdup";
    }

//    @Override
    public Plugin.Type getType() {
        return Plugin.Type.PROCESSOR;
    }

//    @Override
    public ConfigurationParser.SectionParser<Map<String, Object>> getParser() {
        return null;
    }


//    @Override
    public void run() {

    }
}
