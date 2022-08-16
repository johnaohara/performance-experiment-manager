package io.hyperfoil.tools.experimentManager;

import io.hyperfoil.tools.experimentManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.experimentManager.api.PipelineExecutable;
import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.api.dto.RunningExperiment;
import io.hyperfoil.tools.experimentManager.model.ExperimentDAO;
import io.hyperfoil.tools.experimentManager.parser.ConfigParserException;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;
import io.hyperfoil.tools.experimentManager.pipeline.ExperimentPipeline;
import io.hyperfoil.tools.experimentManager.plugins.horreum.api.ApiResult;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class ExperimentManager {

    private static final Logger LOGGER = Logger.getLogger(ExperimentManager.class);

    private Map<String, ExperimentPipeline> pipelines;

    @Inject
    ConfigurationParser yamlParser;

    //TODO: review how best to execute asynchronously
    // atm we probably want worker pool here
    @Inject
    ManagedExecutor executor;


    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Loading plugins");
        pipelines = new HashMap<>();
    }


    public Plugin getPlugin(String name) {
/*
            Plugin plugin = plugins.select(new Handler.HandlerQualifier(name.toLowerCase())).get();
            return  plugin;
*/
        return null;
    }

    public void startPipeline(String name) throws ExperimentManagerException {
        ExperimentPipeline pipeline = getPipeline(name);
        PipelineExecutable executable = pipeline.getHead();
        this.schedule(executable);

    }
    public void schedule(PipelineExecutable executable) {
        executor.runAsync(executable);
    }

    public String handleResult(String handler, Object result) {
        return null;
    }

    @Transactional
    public List<RunningExperiment> getRunningExperiments() {

        List<RunningExperiment> runningExperiments = null;
        try (Stream<ExperimentDAO> experiments = ExperimentDAO.streamAll()) {
            runningExperiments = experiments
                    .map(e -> new RunningExperiment(e.name, e.total_trials, e.currentTrial, e.state))
                    .collect(Collectors.toList());
        }
        return runningExperiments;

    }

    public ApiResult createNewExperiment(String config) {

        try {
            ExperimentPipeline experimentConfig = yamlParser.parseYaml(config);

            pipelines.put(experimentConfig.getExperimentName(), experimentConfig);
            return ApiResult.success();

        } catch (ConfigParserException e) {
            e.printStackTrace();
            return ApiResult.failure("Could not parse config: ".concat(e.getMessage()));
        }
    }

    public ApiResult initializeExperiment(String experimentName) {

        try {
            if( !pipelines.containsKey(experimentName)){
                return ApiResult.failure(String.format("Could not find experiment: %s", experimentName));
            }

            ExperimentPipeline pipeline = pipelines.get(experimentName);
            pipeline.initialize();
            return ApiResult.success();

        } catch (ExecutableInitializationException e) {
            e.printStackTrace();
            return ApiResult.failure("Could not initialize experiment pipeline: ".concat(e.getMessage()));
        }
    }

    public List<String> getExperimentPipelineNames() {

        return this.pipelines.keySet().stream().toList();

    }

    public boolean hasPipeline(String pipelineName){
        return this.pipelines.keySet().contains(pipelineName);
    }

    public ExperimentPipeline getPipeline(String pipelineName) throws ExperimentManagerException {
        if( !hasPipeline(pipelineName)) {
            throw new ExperimentManagerException(String.format("Pipeline not found: %s", pipelineName));
        }
        return this.pipelines.get(pipelineName);
    }

    public ExperimentPipeline findPipeline() {
        //TOOD:: search criteria
        return pipelines.values().stream().findFirst().get();
    }
/*


            LOG.infof("New experiement: %s", Util.prettyPrintExperiment(experimentConfig.toString() ));

            //1. verify that experiment does not already exist
            if (hpoService.experimentExists(experimentConfig.getExperimentName())) {
                hpoService.stopExperimentByName(experimentConfig.getExperimentName());
//                String error = "Experiment already exists in HPO service: %s".formatted(experimentConfig.getExperimentName());
//                LOG.warn(error);
//                return ApiResult.failure(error);
            }

            //2. TODO:: verify that config is valid for lab env
            //a. TODO:: Verify job exists in Jenkins
            //c. TODO:: verify that Horreum job is configured

            //2. persist new experiment configuration
            //a. STATUS is NEW
            ExperimentDAO experiment = HpoMapper.INSTANCE().mapDAO(experimentConfig.getHpoExperiment());
            experiment.state = ExperimentDAO.State.NEW;
            experiment.name = experimentConfig.getExperimentName(); //TODO: check automatic mapping

            experiment.horreum = HpoMapper.INSTANCE().map(experimentConfig.getHorreum());
            if ( experimentConfig.getJenkinsJob() != null ) {
                experiment.jenkins = HpoMapper.INSTANCE().map(experimentConfig.getJenkinsJob());
            }
            if ( experimentConfig.getqDupJob() != null) {
                experiment.qDup = HpoMapper.INSTANCE().map(experimentConfig.getqDupJob());
            }

            experiment.persist();

            //3. Set up experiment
            //a. create new experiment in HPO service
            String result = hpoService.newExperiment(experimentConfig.getHpoExperiment());
            if (result != null) {
                //Failed to create hpo experiment
                return ApiResult.failure("Failed to create new experiment in HPO service: ".concat(result));
            }

            //4. Update persisted experiment state to READY
            experiment.state = ExperimentDAO.State.READY;
            experiment.persist();

            //4. Inform result
            return ApiResult.success(experiment.name);


*/

}
