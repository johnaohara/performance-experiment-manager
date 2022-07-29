package io.hyperfoil.tools.experimentManager.pipeline;

import io.hyperfoil.tools.experimentManager.ExperimentManager;
import io.hyperfoil.tools.experimentManager.ExperimentManagerException;
import io.hyperfoil.tools.experimentManager.api.PipelineContext;
import io.hyperfoil.tools.experimentManager.api.PipelineContextImpl;
import io.hyperfoil.tools.experimentManager.api.PipelineExecutable;
import io.hyperfoil.tools.experimentManager.plugins.horreum.api.ApiResult;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class BasePipelineTest {

    private static final String TEST_LATCH_NAME="test-latch";

    @Inject
    ExperimentManager experimentManager;

    protected void createNewPipeline(String pipelineName, String definition){
        ApiResult result = experimentManager.createNewExperiment(definition);

        assertEquals(ApiResult.NewExperimentStatus.SUCCESS, result.status);

        assertNotEquals(0, experimentManager.getExperimentPipelineNames().size());
        assertTrue(experimentManager.hasPipeline(pipelineName));
    }

    protected PipelineContext executePipeline(String pipelineName, String config){
        return this.executePipeline(pipelineName, config, new CountDownLatch(1));
    }

    protected PipelineContext executePipeline(String pipelineName, String config, CountDownLatch latch){

        this.createNewPipeline(pipelineName, config);


        try {
            PipelineContext pipelineContext = new PipelineContextImpl();
            pipelineContext.setObject(TEST_LATCH_NAME, latch);

            PipelineExecutable executable = experimentManager.getPipeline(pipelineName).getHead();

            executable.setContext(pipelineContext);

            experimentManager.schedule(executable);

            if (!latch.await(10, TimeUnit.SECONDS)) {
                fail("timed out waiting for latch");
            }
            return pipelineContext;
        } catch (ExperimentManagerException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
        return null;
    }
}
