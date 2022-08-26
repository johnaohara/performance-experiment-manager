package io.hyperfoil.tools.pipelineManager.plugins.util;

import io.hyperfoil.tools.pipelineManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.pipelineManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.pipelineManager.api.PipelineContext;
import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OutputMapperPlugin implements Plugin<Map<String, Object>> {

    static final ConfigurationParser.SectionParser<Map<String, Object>> parser;

    static {
        parser = new ConfigurationParser.SectionParser<>(
                (section, builder) -> { //validation
                    if (!section.isEmpty() && (section.keySet().size() > 0) && section.containsKey("mapping"))
                        return null;
                    List<String> errors = new ArrayList<>();
                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> {

                    String type = "simple"; //TODO:: think of better type name
                    if (section.containsKey("type")) {
                        type = (String) section.get("type");
                    }

                    Map<String, String> mappingDef = (Map<String, String>) section.get("mapping");

                    TestOutputExecutable outputExecutable = TestOutputExecutable.instance();

                    switch (type) {
                        case "array":
                            outputExecutable.type(TestOutputExecutable.MAPPING_TYPE.ARRAY);
                            break;
                        default:
                            outputExecutable.type(TestOutputExecutable.MAPPING_TYPE.SIMPLE);
                    }


                    mappingDef.forEach((key, value) -> outputExecutable.variable(key, value));
                    config.addExecutable(outputExecutable);
                });
    }

    @Override
    public String getName() {
        return "output-mapping";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return parser;
    }

    public static class TestOutputExecutable extends BasePipelineExecutable {
        Logger logger = Logger.getLogger(TestOutputExecutable.class);

        private Map<String, String> paramMapping;
        private MAPPING_TYPE mappingType;

        private TestOutputExecutable() {
            paramMapping = new HashMap<>();
        }

        public static TestOutputExecutable instance() {
            return new TestOutputExecutable();
        }

        public TestOutputExecutable variable(String name, String value) {
            this.paramMapping.put(name, value);
            return this;
        }

        public TestOutputExecutable type(MAPPING_TYPE type) {
            this.mappingType = type;
            return this;
        }


        @Override
        public void run(PipelineContext context) {
            Map<String, Object> mappedResult = new HashMap<>();
            switch (this.mappingType) {
                case SIMPLE -> paramMapping.forEach((key, value) -> mappedResult.put(value, context.getResult().get(key)));
                case ARRAY -> paramMapping.forEach((key, value) -> {
                    String val = mappedResult.get(value) == null ? "" : String.valueOf(mappedResult.get(value));
                    mappedResult.put(value, val.concat(" ").concat(String.valueOf(context.getResult().get(key))));
                });
            }

            context.setResult(mappedResult);
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //do nothing - this executable does not need to initialize any remote service
        }

        private enum MAPPING_TYPE {
            SIMPLE,
            ARRAY
        }
    }


}
