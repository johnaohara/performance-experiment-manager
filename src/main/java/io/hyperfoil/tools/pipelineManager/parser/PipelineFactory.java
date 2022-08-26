package io.hyperfoil.tools.pipelineManager.parser;

import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.pipeline.Pipeline;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class PipelineFactory {

    @Inject
    @Any
    Instance<Plugin<?>> pluginBeans;

    Map<String, Plugin> plugins = new HashMap<>();

    private Map<String, ConfigurationParser.SectionParser> mappings = new HashMap<>();


    public void startup(@Observes StartupEvent startupEvent) {

        //special case mapping for root element
        mappings.put("root", new ConfigurationParser.SectionParser<Map<String, List<?>>>(
                        (section, builder) -> {
                            if (section != null && !section.isEmpty()
                                    && section.size() == 1
                                    && section.values().stream().findFirst().get() instanceof List<?>
                            ) {
                                return null;
                            }

                            List<String> errors = new ArrayList<>();
                            if (section == null) {
                                errors.add("No pipline defined");
                            } else {
                                if (section.isEmpty()) {
                                    errors.add("No pipline defined");
                                }
                                if (!(section.values() instanceof List<?>)) {
                                    errors.add("Expecting a List of pipeline steps");
                                }
                            }
                            return errors;
                        }, //validation
                        (config, section, builder) -> {
                            //TODO: callback to set config properties - this needs to be decoupled
                            config.pipelineName(section.keySet().stream().findFirst().get()); //get pipeline name
                            List<Map<String, Object>> steps = (List<Map<String, Object>>) section.values().stream().findFirst().get();
                            for (Map<String, Object> step : steps) { //can not use streams here, otherwise ConfigParserException is not propagated back from lambda :(
                                for (Map.Entry<String, Object> entry : step.entrySet()) {
                                    builder.build(config, entry.getKey(), entry.getValue());
                                }
                            }
                        }
                )
        );


        pluginBeans.forEach(plugin -> {
            plugins.put(plugin.getName(), plugin);
            mappings.put(plugin.getName(), plugin.getParser());
        });

    }

    public static boolean numElements(Map map, Integer numElements) {
        return map.keySet().size() == numElements;
    }

    public static boolean containsKeys(Map map, String... elements) {
        for (String element : elements) {
            if (!map.containsKey(element)) {
                return false;
            }
        }
        return true;
    }


    public static PipelineFactory instance() {
        return new PipelineFactory();
    }

    void build(Object value, Pipeline config) throws ConfigParserException {
        this.build(config, "root", value);
    }

    void build(Pipeline config, String mappingName, Object value) throws ConfigParserException {
        if (mappings.containsKey(mappingName) && mappings.get(mappingName) != null) {
            ConfigurationParser.SectionParser sectionParser = mappings.get(mappingName);
            var errors = sectionParser.validator.validate(value, this);
            if (errors == null) {
                sectionParser.procesor.process(config, value, this);
            } else {
                throw new ConfigParserException("Could not validate mapping: " + mappingName, errors);
            }
        } else {
            throw new ConfigParserException("Unknown mapping value: " + mappingName);
        }
    }

}
