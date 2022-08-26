package io.hyperfoil.tools.pipelineManager.srvc;

import io.hyperfoil.tools.pipelineManager.PipelineManager;
import io.hyperfoil.tools.pipelineManager.api.ApiException;
import io.hyperfoil.tools.pipelineManager.api.dto.RunningPipeline;
import io.hyperfoil.tools.pipelineManager.plugins.horreum.api.ApiResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/api/pipeline")
public class PipelineSrvc {
    @Inject
    PipelineManager pipelineManager;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RunningPipeline> pipelines() {
        return pipelineManager.getRunningExperiments();
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
}
