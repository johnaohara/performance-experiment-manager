package io.hyperfoil.tools.pipelineManager.plugins.qdup;

import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;

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
