package io.hyperfoil.tools.pipelineManager.plugins.horreum;

import io.hyperfoil.tools.pipelineManager.PipelineManager;
import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.hyperfoil.tools.pipelineManager.plugins.horreum.api.ApiResult;
import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;
import io.hyperfoil.tools.horreum.entity.json.Run;
import org.jboss.logmanager.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/horreum")
public class HorreumReceiver{
//public class HorreumReceiver extends BasePlugin<HorreumPlugin> implements Parser<Map<String, Object>> {

    Logger logger = Logger.getLogger(HorreumReceiver.class.getName());

    @Inject
    PipelineManager pipelineManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("newRun")
    public ApiResult handleNewRun(Run run){
        logger.info("New run received. Test ID: ".concat(String.valueOf(run.testid)).concat("; with Run id: ").concat(String.valueOf(run.id)));


/*
        String response = null;
        try {
            response = horreumPlugin.handleResult(run);
        } catch (Exception e){
            if ( response == null ) {
                response = e.getLocalizedMessage();
            }
        }

        if ( response == null ) {
            return ApiResult.success();
        } else {
            logger.warning("Failed to process experiment result: ".concat(response));
            return ApiResult.failure(response);
        }
*/
        return ApiResult.success();
    }

    public String getName() {
        return "horreum-receiver";
    }

    public Plugin.Type getType() {
        return Plugin.Type.RECEIVER;
    }

//    @Override
    public ConfigurationParser.SectionParser getParser() {
        return null;
    }

//    @Override
    public void run() {

    }
}
