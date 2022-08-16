package io.hyperfoil.tools.experimentManager.plugins.test;

import io.hyperfoil.tools.experimentManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.experimentManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.experimentManager.api.PipelineContext;
import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class TestReceiver implements Plugin<Map<String, Object>> {

    Logger logger = Logger.getLogger(TestReceiver.class);

    @Override
    public String getName() {
        return "test-receiver";
    }

    @Override
    public Type getType() {
        return Type.RECEIVER;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return new ConfigurationParser.SectionParser<Map<String, Map>>(
                (section, builder) -> null, //validation
                (config, section, builder) -> {
                    logger.debug("Parsing test-receiver section");
                    config.addExecutable(TestReceiverExecutable.instance().id("1"));
                });
    }

    public static class TestReceiverExecutable extends BasePipelineExecutable {

        Logger logger = Logger.getLogger(TestReceiverExecutable.class);

        private String id;

        static TestReceiverExecutable instance(){
            return new TestReceiverExecutable();
        }

        private TestReceiverExecutable() {
        }

        TestReceiverExecutable id(String id){
            this.id = id;
            return this;
        }

        @Override
        public void run(PipelineContext context) {
            logger.infof("Running: test-receiver; %s", this.id);
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //do nothing - this executable does not need to initialize any remote service
        }
    }

}
