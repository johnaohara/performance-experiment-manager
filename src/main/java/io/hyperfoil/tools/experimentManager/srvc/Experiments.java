package io.hyperfoil.tools.experimentManager.srvc;

import io.hyperfoil.tools.experimentManager.ExperimentManager;
import io.hyperfoil.tools.experimentManager.api.ApiException;
import io.hyperfoil.tools.experimentManager.api.dto.RunningExperiment;
import io.hyperfoil.tools.experimentManager.plugins.horreum.api.ApiResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path("/experiments")
public class Experiments {
    @Inject
    ExperimentManager experimentManager;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RunningExperiment> experiments() {
        return experimentManager.getRunningExperiments();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult newExperiment(String config){
        ApiResult result = experimentManager.createNewExperiment(config);
        if ( result.status == ApiResult.NewExperimentStatus.SUCCESS){
            return result;
        } else {
            throw new ApiException(result);
        }
    }
}
