package io.hyperfoil.tools.experimentManager.pipeline;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BasePipelineResourceTests extends BasePipelineTest {


    @BeforeAll
    void setUpExperiment(){
        String pipelineDefinition = getPipelineDefinition();
        createNewPipeline(String.valueOf(java.util.UUID.randomUUID()), pipelineDefinition);
    }

    abstract String getPipelineDefinition();

}
