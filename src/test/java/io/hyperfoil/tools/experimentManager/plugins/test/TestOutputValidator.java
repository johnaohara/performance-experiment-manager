package io.hyperfoil.tools.experimentManager.plugins.test;

import io.hyperfoil.tools.experimentManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.experimentManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.experimentManager.api.PipelineContext;
import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class TestOutputValidator implements Plugin<Map<String, Object>> {

    static final ConfigurationParser.SectionParser<Map<String, Integer>> parser;

    static {
        parser = new ConfigurationParser.SectionParser<>(
                (section, builder) -> { //validation
                    if (section == null)
                        return null;
                    List<String> errors = new ArrayList<>();
                    errors.add("body should be empty");
                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> config.addExecutable(
                        TestInputLoggerExecutable.instance()
                ));
    }

    @Override
    public String getName() {
        return "test-output-validator";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return parser;
    }

    public static class TestInputLoggerExecutable extends BasePipelineExecutable {
        Logger logger = Logger.getLogger(TestInputLoggerExecutable.class);

        private TestInputLoggerExecutable() {

        }
        public static TestInputLoggerExecutable instance(){
            return new TestInputLoggerExecutable();
        }

        @Override
        public void run(PipelineContext context) {
            Map result = context.getResult();
            if(result != null) {
                logger.infof("Results: ");
                result.forEach( (key, entry) -> logger.infof("\t%s; %s", key, entry.toString()));

            } else {
                logger.errorf("Result is empty");
            }
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //do nothing - this executable does not need to initialize any remote service
        }

    }


}
