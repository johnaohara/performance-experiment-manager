package io.hyperfoil.tools.experimentManager.parser;

import io.hyperfoil.tools.experimentManager.pipeline.ExperimentPipeline;
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

    public ExperimentPipeline parseYaml(String yml) throws ConfigParserException {
        //initialize yaml parser
        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);

        //load configuration yaml
        Map<String, Object> yamlConfig = (Map<String, Object>) load.loadFromString(yml);

        //build configuration from parsed yaml
        ExperimentPipeline config = new ExperimentPipeline();
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
        List<String> validate(T section, PipelineFactory experimentBuilder);
    }

    public interface procesor<T> {
        void process(ExperimentPipeline config, T section, PipelineFactory experimentBuilder) throws ConfigParserException;
    }

}