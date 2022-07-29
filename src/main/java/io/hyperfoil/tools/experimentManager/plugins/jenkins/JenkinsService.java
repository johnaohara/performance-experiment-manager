package io.hyperfoil.tools.experimentManager.plugins.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import io.hyperfoil.tools.experimentManager.api.IRuntimeEnvironment;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.URISyntaxException;

@ApplicationScoped
public class JenkinsService implements IRuntimeEnvironment {

    @ConfigProperty( name = "experiment-manager.jenkins.url")
    public String JENKINS_URL;

    @ConfigProperty( name = "experiment-manager.jenkins.username")
    public String USERNAME;

    @ConfigProperty( name = "experiment-manager.jenkins.password")
    public String PASSWORD;

    private JenkinsServer jenkins;

    @Override
    public String newRun() {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void parseConfig() {
        throw new RuntimeException("Not yet implemented");
    }

/*
    public String newRun(ExperimentDAO experimentDAO, TrialConfig trialConfig) {
        connectToJenkins();
        */
/*
         * TODO:: Either need to;
         *  a - expose one-to-one direct mapping of tuneables -> params
         *  b - create dynamically mappings of tunable -> params
         *  c - pass to jenkins job to parse param mappings
         *
         *  supporting (b) will be more versatile, but by default (a) will allow for less config
         * *//*


        Map<String, String> jobParams = trialConfig.tunableConfigs().stream()
                .filter(tunableConfig -> experimentDAO.jenkins.params.containsKey(tunableConfig.name()))
                .collect(Collectors.toMap(tuneable -> experimentDAO.jenkins.params.get(tuneable.name()), tuneable -> tuneable.value().toString()));

        try {

            //TODO: investigate passing a parameter file.
            // will need a HPO job that parses the parameter file and starts the correct jenkins jobs
            JobWithDetails job = jenkins.getJob(experimentDAO.jenkins.job_url);
            if ( job == null){
                return "Could not find job: ".concat(experimentDAO.jenkins.job_url);
            }
            job.build(jobParams);

        } catch (IOException e) {
            return e.getLocalizedMessage();
        }

        return null;
    }

    @Override
    public void parseConfig(ExperimentBuilder builder, Object o) {
        System.out.println("Parse Jenkins Config");

        Map configMap = (Map) o;

        builder.addJenkinsJob(
                configMap.get("job").toString()
                , configMap.get("job_url").toString());

        List<Map<?, ?>> params = (List<Map<?, ?>>) configMap.get("params");
        for (Map paramMapping : params) {
//            builder.add("qDup~params", paramMapping);
            builder.addJenkinsParamMapping(paramMapping.get("name").toString(), paramMapping.get("tuneable").toString());
        }
        /*
        mappings.put("hpo~jenkins", new YamlParser.SectionParser<Map<String, Object>>(
                        (jenkinsMap, builder) -> numElements(jenkinsMap, 3) && containsKeys(jenkinsMap, "job")
                        , (jenkinsMap, builder) -> {
                    builder.addJenkinsJob(
                            jenkinsMap.get("job").toString()
                            , jenkinsMap.get("job_url").toString());

                    List<?> params = (List<?>) jenkinsMap.get("params");
                    for (Object paramMapping : params) {
                        builder.add("jenkins~params", paramMapping);
                    }
                })
        );

        mappings.put("jenkins~params", new YamlParser.SectionParser<Map<String, Object>>(
                (paramsMap, builder) -> numElements(paramsMap, 2) && containsKeys(paramsMap, "name", "tuneable")
                , (paramsMap, builder) -> builder.addJenkinsParamMapping(paramsMap.get("name").toString(), paramsMap.get("tuneable").toString()))


    }
*/
    private void connectToJenkins(){
        try {
            if ( jenkins == null) {
                jenkins = new JenkinsServer(new URI(JENKINS_URL), USERNAME, PASSWORD);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
