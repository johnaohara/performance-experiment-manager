package io.hyperfoil.tools.pipelineManager.plugins.test;

import io.hyperfoil.tools.pipelineManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.pipelineManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.pipelineManager.api.PipelineContext;
import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.hyperfoil.tools.pipelineManager.parser.PipelineFactory.containsKeys;
import static io.hyperfoil.tools.pipelineManager.parser.PipelineFactory.numElements;

@Singleton
public class TestContextLogger implements Plugin<Map<String, Object>> {

    static final ConfigurationParser.SectionParser<Map<String, Integer>> parser;

    static {
        parser = new ConfigurationParser.SectionParser<Map<String, Integer>>(
                (section, builder) -> { //validation
                    if (!section.isEmpty() && numElements(section, 1) && containsKeys(section, "variable"))
                        return null;
                    List<String> errors = new ArrayList<>();
                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> config.addExecutable(
                        TestProcessorExecutable.instance()
                                .variable(section.get("variable").toString())
                ));
    }

    @Override
    public String getName() {
        return "test-context-logger";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return parser;
    }

    public static class TestProcessorExecutable extends BasePipelineExecutable {
        Logger logger = Logger.getLogger(TestProcessorExecutable.class);

        private String variable;

        private TestProcessorExecutable() {

        }
        public static TestProcessorExecutable instance(){
            return new TestProcessorExecutable();
        }

        public TestProcessorExecutable variable(String varibale){
            this.variable = varibale;
            return this;
        }

        @Override
        public void run(PipelineContext context) {
            if(context.hasObject(this.variable)) {
                logger.infof("Context Variable: %s; %s", this.variable, context.getObject(this.variable).toString());
            } else {
                logger.errorf("Context Variable MISSING: %s", this.variable);
            }
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //do nothing - this executable does not need to initialize any remote service
        }

    }


}
