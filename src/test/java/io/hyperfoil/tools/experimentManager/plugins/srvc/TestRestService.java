package io.hyperfoil.tools.experimentManager.plugins.srvc;

import io.hyperfoil.tools.experimentManager.ExperimentManager;
import io.hyperfoil.tools.experimentManager.api.PipelineContext;
import io.hyperfoil.tools.experimentManager.api.PipelineContextImpl;
import io.hyperfoil.tools.experimentManager.api.PipelineExecutable;
import io.hyperfoil.tools.experimentManager.pipeline.ExperimentPipeline;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/test/plugin")
public class TestRestService {

    Logger logger = Logger.getLogger(TestRestService.class);

    @Inject
    ExperimentManager experimentManager;

    @POST
    public void newTestResult(TestDTO testDTO){
        logger.infof("New test result: %s - %d", testDTO.id(), testDTO.result());


        //TOOD: extract to some form of dispatch
        PipelineContext context = new PipelineContextImpl();
        context.<TestDTO>setObject("result", testDTO);
        ExperimentPipeline pipeline = experimentManager.findPipeline();
        PipelineExecutable executable = pipeline.getHead();
        executable.setContext(context);
        experimentManager.schedule(executable);


    }
}
