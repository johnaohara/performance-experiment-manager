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

import static io.hyperfoil.tools.experimentManager.parser.PipelineFactory.containsKeys;
import static io.hyperfoil.tools.experimentManager.parser.PipelineFactory.numElements;

@Singleton
public class TestProcessor implements Plugin<Map<String, Object>> {

    @Override
    public String getName() {
        return "test-processor";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return new ConfigurationParser.SectionParser<Map<String, Integer>>(
                (section, builder) -> {
                    if (!section.isEmpty() && numElements(section, 1) && containsKeys(section, "processor-id"))
                        return null;
                    List<String> errors = new ArrayList<>();
                    return errors; //TODO: Fill out error handling
                }, //validation
                (config, section, builder) -> {
                    config.addExecutable(
                            TestProcessorExecutable.instance()
                                    .id(section.get("processor-id").toString())
                    );
                });
    }

    public static class TestProcessorExecutable extends BasePipelineExecutable {
        Logger logger = Logger.getLogger(TestProcessorExecutable.class);

        private String id;

        private TestProcessorExecutable() {

        }
        public static TestProcessorExecutable instance(){
            return new TestProcessorExecutable();
        }

        public TestProcessorExecutable id(String id){
            this.id = id;
            return this;
        }

        //TODO: remove we only need this for testing
        public String getId() {
            return id;
        }

        @Override
        public void run(PipelineContext context) {
            logger.infof("Running: test-processor; %s", this.id);
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //do nothing - this executable does not need to initialize any remote service
        }

    }


}
