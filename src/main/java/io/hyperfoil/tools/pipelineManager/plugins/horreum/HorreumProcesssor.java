package io.hyperfoil.tools.pipelineManager.plugins.horreum;

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
public class HorreumProcesssor implements Plugin<Map<String, Object>> {

    static final Logger logger = Logger.getLogger(HorreumProcesssor.class);
    static final ConfigurationParser.SectionParser<Map<String, Integer>> parser;

    static {
        parser = new ConfigurationParser.SectionParser<>(
                (section, builder) -> { //validation
                    if (!section.isEmpty() && numElements(section, 1) && containsKeys(section, "jobID"))
                        return null;
                    List<String> errors = new ArrayList<>();
                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> config.addExecutable(
                        HorreumExecutable.instance()
                                .jobId(section.get("jobID"))
                ));
    }

    @Override
    public String getName() {
        return "horreum";
    }

    @Override
    public Plugin.Type getType() {
        return Plugin.Type.PROCESSOR;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return null;
    }


    public static class HorreumExecutable extends BasePipelineExecutable {
        static final Logger logger = Logger.getLogger(HorreumExecutable.class);

        private Integer horreumJobID;

        private HorreumExecutable() {

        }
        public static HorreumExecutable instance(){
            return new HorreumExecutable();
        }

        public HorreumExecutable jobId(Integer jobId){
            this.horreumJobID = jobId;
            return this;
        }

        @Override
        public void run(PipelineContext context) {
            logger.infof("Processing Horreum job: %d", this.horreumJobID);
        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            //TODO:
            // - check existing definition ,delete if appropriate - make behaviour configurable
            // - create new experiment definition
            // - get initial configuration

        }

    }


}
