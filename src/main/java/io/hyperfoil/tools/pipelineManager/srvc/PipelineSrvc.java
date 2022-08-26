package io.hyperfoil.tools.pipelineManager.srvc;

import io.hyperfoil.tools.pipelineManager.PipelineManager;
import io.hyperfoil.tools.pipelineManager.api.ApiException;
import io.hyperfoil.tools.pipelineManager.api.dto.RunningPipeline;
import io.hyperfoil.tools.pipelineManager.plugins.horreum.api.ApiResult;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.RestStreamElementType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/api/pipeline")
public class PipelineSrvc {
    @Inject
    PipelineManager pipelineManager;

    // Inject our Book channel
    @Inject
    @Channel("experiments-summary-out")
    Multi<List<RunningPipeline>> pipelinesSummary;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RunningPipeline> pipelines() {
        return pipelineManager.getRunningPipelines();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult newExperiment(String config){
        ApiResult result = pipelineManager.createNewExperiment(config);
        if ( result.status == ApiResult.NewExperimentStatus.SUCCESS){
            return result;
        } else {
            throw new ApiException(result);
        }
    }

    @PUT
    @Path("/state")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResult newExperiment(PipeilineState experimentState){
//        String errors = hpOaaS.changeExperimentState(experimentState.name, experimentState.state);
        String errors = null;
        if ( errors == null){
            return ApiResult.success();
        } else {
            return ApiResult.failure(errors);
        }
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<List<RunningPipeline>> experimentsStream() {
        return pipelinesSummary;
    }


    static class PipeilineState {

        public String name;
        public String state;
    }

}
