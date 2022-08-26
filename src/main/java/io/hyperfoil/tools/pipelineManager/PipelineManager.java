package io.hyperfoil.tools.pipelineManager;

import io.hyperfoil.tools.pipelineManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.pipelineManager.api.PipelineExecutable;
import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.api.dto.RunningPipeline;
import io.hyperfoil.tools.pipelineManager.model.PipelineDAO;
import io.hyperfoil.tools.pipelineManager.parser.ConfigParserException;
import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;
import io.hyperfoil.tools.pipelineManager.pipeline.Pipeline;
import io.hyperfoil.tools.pipelineManager.plugins.horreum.api.ApiResult;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class PipelineManager {

    private static final Logger LOGGER = Logger.getLogger(PipelineManager.class);

    @Channel("experiments-summary-out")
    Emitter<List<RunningPipeline>> experimentSummaryEmitter;

    private Map<String, Pipeline> pipelines;

    @Inject
    ConfigurationParser parser;

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

    public void startPipeline(String name) throws PipelineManagerException {
        Pipeline pipeline = getPipeline(name);
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
    public List<RunningPipeline> getRunningPipelines() {

        List<RunningPipeline> runningPipelines = null;
/*
        try (Stream<PipelineDAO> pipeliness = PipelineDAO.streamAll()) {
            runningPipelines = pipeliness
                    .map(e -> new RunningPipeline(e.name, e.total_trials, e.currentTrial, e.state))
                    .collect(Collectors.toList());
        }
*/
        //TOOD:: make this generic from database
        runningPipelines = this.pipelines.values().stream().map( pipeline -> new RunningPipeline(pipeline.getPipelineName(), 0, 1, PipelineDAO.State.NEW)).collect(Collectors.toList());
        return runningPipelines;

    }

    public ApiResult createNewExperiment(String config) {

        try {
            Pipeline pipeline = parser.build(config);

            LOGGER.infof("New pipeline created: %s", pipeline.getPipelineName());

            pipelines.put(pipeline.getPipelineName(), pipeline);
            return ApiResult.success();

        } catch (ConfigParserException e) {
            e.printStackTrace();
            return ApiResult.failure("Could not parse config: ".concat(e.getMessage()));
        }
    }

    public ApiResult initializePipeline(String pipelineName) {

        try {
            if( !pipelines.containsKey(pipelineName)){
                return ApiResult.failure(String.format("Could not find pipeline: %s", pipelineName));
            }

            Pipeline pipeline = pipelines.get(pipelineName);
            pipeline.initialize();
            return ApiResult.success();

        } catch (ExecutableInitializationException e) {
            e.printStackTrace();
            return ApiResult.failure("Could not initialize pipeline: ".concat(e.getMessage()));
        }
    }

    public List<String> getPipelineNames() {

        return this.pipelines.keySet().stream().toList();

    }

    public boolean hasPipeline(String pipelineName){
        return this.pipelines.keySet().contains(pipelineName);
    }

    public Pipeline getPipeline(String pipelineName) throws PipelineManagerException {
        if( !hasPipeline(pipelineName)) {
            throw new PipelineManagerException(String.format("Pipeline not found: %s", pipelineName));
        }
        return this.pipelines.get(pipelineName);
    }

    public Pipeline findPipeline() {
        //TOOD:: search criteria
        return pipelines.values().stream().findFirst().get();
    }
/*


            LOG.infof("New experiement: %s", Util.prettyPrintExperiment(pipelineConfig.toString() ));

            //1. verify that pipeline does not already exist
            if (hpoService.pipelineExists(pipelineConfig.getExperimentName())) {
                hpoService.stopExperimentByName(pipelineConfig.getExperimentName());
//                String error = "Experiment already exists in HPO service: %s".formatted(pipelineConfig.getExperimentName());
//                LOG.warn(error);
//                return ApiResult.failure(error);
            }

            //2. TODO:: verify that config is valid for lab env
            //a. TODO:: Verify job exists in Jenkins
            //c. TODO:: verify that Horreum job is configured

            //2. persist new pipeline configuration
            //a. STATUS is NEW
            ExperimentDAO pipeline = HpoMapper.INSTANCE().mapDAO(pipelineConfig.getHpoExperiment());
            pipeline.state = ExperimentDAO.State.NEW;
            pipeline.name = pipelineConfig.getExperimentName(); //TODO: check automatic mapping

            pipeline.horreum = HpoMapper.INSTANCE().map(pipelineConfig.getHorreum());
            if ( pipelineConfig.getJenkinsJob() != null ) {
                pipeline.jenkins = HpoMapper.INSTANCE().map(pipelineConfig.getJenkinsJob());
            }
            if ( pipelineConfig.getqDupJob() != null) {
                pipeline.qDup = HpoMapper.INSTANCE().map(pipelineConfig.getqDupJob());
            }

            pipeline.persist();

            //3. Set up pipeline
            //a. create new pipeline in HPO service
            String result = hpoService.newExperiment(pipelineConfig.getHpoExperiment());
            if (result != null) {
                //Failed to create hpo pipeline
                return ApiResult.failure("Failed to create new pipeline in HPO service: ".concat(result));
            }

            //4. Update persisted pipeline state to READY
            pipeline.state = ExperimentDAO.State.READY;
            pipeline.persist();

            //4. Inform result
            return ApiResult.success(pipeline.name);


*/

}
