package io.hyperfoil.tools.experimentManager.plugins.test;

import io.hyperfoil.tools.experimentManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.experimentManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.experimentManager.api.PipelineContext;
import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.hyperfoil.tools.experimentManager.parser.PipelineFactory.containsKeys;
import static io.hyperfoil.tools.experimentManager.parser.PipelineFactory.numElements;

@Singleton
public class TestContextOutput implements Plugin<Map<String, Object>> {

    static final ConfigurationParser.SectionParser<Map<String, String>> parser;

    static {
        parser = new ConfigurationParser.SectionParser<>(
                (section, builder) -> { //validation
                    if (!section.isEmpty() && numElements(section, 1) && containsKeys(section, "variable"))
                        return null;
                    List<String> errors = new ArrayList<>();
                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> config.addExecutable(
                        TestContextOutputExecutable.instance()
                                .variable(section.get("variable"))
                ));
    }

    @Override
    public String getName() {
        return "test-context-output";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return parser;
    }

    public static class TestContextOutputExecutable extends BasePipelineExecutable {
        Logger logger = Logger.getLogger(TestContextOutputExecutable.class);

        private String variable;

        private TestContextOutputExecutable() {

        }
        public static TestContextOutputExecutable instance(){
            return new TestContextOutputExecutable();
        }

        public TestContextOutputExecutable variable(String varibale){
            this.variable = varibale;
            return this;
        }

        @Override
        public void run(PipelineContext context) {
            if(context.hasObject(this.variable)) {
                Map<String, Object> result =new HashMap<>();
                result.put(this.variable, context.getObject(this.variable));
                context.setResult(result);
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
