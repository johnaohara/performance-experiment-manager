package io.hyperfoil.tools.pipelineManager.pipeline;

import io.hyperfoil.tools.pipelineManager.PipelineManager;
import io.hyperfoil.tools.pipelineManager.PipelineManagerException;
import io.hyperfoil.tools.pipelineManager.api.PipelineContext;
import io.hyperfoil.tools.pipelineManager.api.PipelineContextImpl;
import io.hyperfoil.tools.pipelineManager.api.PipelineExecutable;
import io.hyperfoil.tools.pipelineManager.plugins.horreum.api.ApiResult;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class BasePipelineTest {

    private static final String TEST_LATCH_NAME="test-latch";

    @Inject
    PipelineManager pipelineManager;

    protected void createNewPipeline(String pipelineName, String definition){
        ApiResult result = pipelineManager.createNewExperiment(definition);

        assertEquals(ApiResult.NewExperimentStatus.SUCCESS, result.status);

        assertNotEquals(0, pipelineManager.getPipelineNames().size());
        assertTrue(pipelineManager.hasPipeline(pipelineName));
    }

    protected PipelineContext executePipeline(String pipelineName, String config){
        return this.executePipeline(pipelineName, config, new CountDownLatch(1));
    }

    protected PipelineContext executePipeline(String pipelineName, String config, CountDownLatch latch){

        this.createNewPipeline(pipelineName, config);


        try {
            PipelineContext pipelineContext = new PipelineContextImpl();
            pipelineContext.setObject(TEST_LATCH_NAME, latch);

            PipelineExecutable executable = pipelineManager.getPipeline(pipelineName).getHead();

            executable.setContext(pipelineContext);

            pipelineManager.schedule(executable);

            if (!latch.await(10, TimeUnit.SECONDS)) {
                fail("timed out waiting for latch");
            }
            return pipelineContext;
        } catch (PipelineManagerException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
        return null;
    }
}
