package io.hyperfoil.tools.pipelineManager.plugins.srvc;

import io.hyperfoil.tools.pipelineManager.PipelineManager;
import io.hyperfoil.tools.pipelineManager.api.PipelineContext;
import io.hyperfoil.tools.pipelineManager.api.PipelineContextImpl;
import io.hyperfoil.tools.pipelineManager.api.PipelineExecutable;
import io.hyperfoil.tools.pipelineManager.pipeline.Pipeline;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/test/plugin")
public class TestRestService {

    Logger logger = Logger.getLogger(TestRestService.class);

    @Inject
    PipelineManager pipelineManager;

    @POST
    public void newTestResult(TestDTO testDTO){
        logger.infof("New test result: %s - %d", testDTO.id(), testDTO.result());


        //TOOD: extract to some form of dispatch
        PipelineContext context = new PipelineContextImpl();
        context.<TestDTO>setObject("result", testDTO);
        Pipeline pipeline = pipelineManager.findPipeline();
        PipelineExecutable executable = pipeline.getHead();
        executable.setContext(context);
        pipelineManager.schedule(executable);


    }
}
