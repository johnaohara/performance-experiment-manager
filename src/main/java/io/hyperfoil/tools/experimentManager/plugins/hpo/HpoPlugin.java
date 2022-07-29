package io.hyperfoil.tools.experimentManager.plugins.hpo;

import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class HpoPlugin{
//public class HpoPlugin implements Plugin<Plugin>, Parser<Map<String, Object>> {

//    @Override
    public String getName() {
        return null;
    }

//    @Override
    public Plugin.Type getType() {
        return Plugin.Type.PROCESSOR;
    }

//    @Override
    public Plugin next() {
        return null;
    }

//    @Override
    public void next(Plugin nxt) {

    }

//    @Override
    public ConfigurationParser.SectionParser<Map<String, Object>> getParser() {
        return null;
    }

//    @Override
    public void run() {

    }
}

