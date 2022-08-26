package io.hyperfoil.tools.pipelineManager.plugins.jenkins;

import io.hyperfoil.tools.pipelineManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.pipelineManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.pipelineManager.api.PipelineContext;
import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;
import io.hyperfoil.tools.pipelineManager.plugins.horreum.HorreumProcesssor;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.hyperfoil.tools.pipelineManager.parser.PipelineFactory.containsKeys;
import static io.hyperfoil.tools.pipelineManager.parser.PipelineFactory.numElements;

@Singleton
public class JenkinsPlugin implements Plugin<Map<String, Object>> {
    //public class JenkinsPlugin extends BasePlugin<Plugin> implements Parser<Map<String, Object>> {
    static final Logger logger = Logger.getLogger(JenkinsPlugin.class);
    static final ConfigurationParser.SectionParser<Map<String, String>> parser;

    static {


        parser = new ConfigurationParser.SectionParser<>(
                (section, builder) -> { //validation
                    if (section != null && !section.isEmpty() && numElements(section, 3) && containsKeys(section, "job", "job_url", "params")) {
                        return null;
                    }
                    List<String> errors = new ArrayList<>();
                    errors.add("Usage:");
                    errors.add("jenkins:");
                    errors.add("  job: test");
                    errors.add("  job_url: http://example.com/Jobs/test");
                    errors.add("  params:");
                    errors.add("    - name: \"memoryRequest\"");
                    errors.add("      tuneable: \"memoryRequest\"");

                    return errors; //TODO: Fill out error handling
                },
                (config, section, builder) -> {
                    List<JenkinsExecutable.JenkinsParam> jenkinsParams;

                    JenkinsExecutable jenkinsExecutable = JenkinsExecutable.instance()
                            .job(section.get("job").toString())
                            .job_url(section.get("job_url").toString());

                    config.addExecutable(jenkinsExecutable);

                });
    }

    //    @Override
    public String getName() {
        return "jenkins";
    }

    //    @Override
    public Plugin.Type getType() {
        return Plugin.Type.RUNNER;
    }

    public ConfigurationParser.SectionParser getParser() {
        return parser;
    }

    public static class JenkinsExecutable extends BasePipelineExecutable {
        static final Logger logger = Logger.getLogger(HorreumProcesssor.HorreumExecutable.class);

        private String job;
        private String job_url;
        private List<JenkinsParam> params = new ArrayList<>();

        private JenkinsExecutable() {

        }
        public static JenkinsExecutable instance(){
            return new JenkinsExecutable();
        }

        public JenkinsExecutable job(String job){
            this.job = job;
            return this;
        }
        public JenkinsExecutable job_url(String url){
            this.job_url = url;
            return this;
        }

        @Override
        public void run(PipelineContext context) {

        }

        @Override
        public void initialize() throws ExecutableInitializationException {
            // TODO:
            // - check that jenkins job exists
            // -
        }

        private record  JenkinsParam (String paramName, String tunable) {}
    }
}
