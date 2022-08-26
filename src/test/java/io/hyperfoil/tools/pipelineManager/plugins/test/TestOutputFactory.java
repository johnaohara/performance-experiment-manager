package io.hyperfoil.tools.pipelineManager.plugins.test;

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
public class TestOutputFactory implements Plugin<Map<String, Object>> {

    static final ConfigurationParser.SectionParser<Map<String, Object>> parser;

    static {
        parser = new ConfigurationParser.SectionParser<>(
                (section, builder) -> { //validation
                    if (!section.isEmpty() && (section.keySet().size() > 0) )
                        return null;
                    List<String> errors = new ArrayList<>();
                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> {
                    TestOutputExecutable outputExecutable = TestOutputExecutable.instance();
                    section.forEach((key, value) -> outputExecutable.variable(key,value));
                    config.addExecutable(outputExecutable);
                });
    }

    @Override
    public String getName() {
        return "test-output-factory";
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

        private Map<String, Object> output;

        private TestOutputExecutable() {
            output = new HashMap<>();
        }
        public static TestOutputExecutable instance(){
            return new TestOutputExecutable();
        }

        public TestOutputExecutable variable(String name, Object value){
            this.output.put(name, value);
            return this;
        }

        @Override
        public void run(PipelineContext context) {
            context.setResult(this.output);
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //do nothing - this executable does not need to initialize any remote service
        }

    }


}
