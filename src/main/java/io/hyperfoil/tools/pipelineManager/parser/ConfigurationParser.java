package io.hyperfoil.tools.pipelineManager.parser;

import io.hyperfoil.tools.pipelineManager.pipeline.Pipeline;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class ConfigurationParser {

    @Inject
    PipelineFactory pipelineBuilder;

    public Pipeline build(String yml) throws ConfigParserException {
        //initialize yaml parser
        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);

        //load configuration yaml
        Map<String, Object> yamlConfig = (Map<String, Object>) load.loadFromString(yml);

        //build configuration from parsed yaml
        Pipeline config = new Pipeline();
        pipelineBuilder.build(yamlConfig, config);
        return config ;
    }

    public static class SectionParser<T> {
        public validator<T> validator;
        public procesor<T> procesor;

        public SectionParser(ConfigurationParser.validator<T> validator, ConfigurationParser.procesor<T> procesor) {
            this.validator = validator;
            this.procesor = procesor;
        }

    }

    public interface validator<T> {
        List<String> validate(T section, PipelineFactory pipelineFactory);
    }

    public interface procesor<T> {
        void process(Pipeline config, T section, PipelineFactory pipelineFactory) throws ConfigParserException;
    }

}