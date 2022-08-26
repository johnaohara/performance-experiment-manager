package io.hyperfoil.tools.pipelineManager.pipeline;

import io.hyperfoil.tools.pipelineManager.api.PipelineContext;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PipelineMappingTests  extends BasePipelineTest {

    @Test
    public void simpleOneToOneMappingTest() {
        var testPipelineDef = """
                    test-propagation-pipeline:
                      - test-output-factory:
                          memoryRequest: 256
                          cpuRequest:  2.5
                      - output-mapping:
                          mapping: 
                            memoryRequest: mem
                            cpuRequest:  cpu
                      - test-latch-release:
                """;

        PipelineContext pipelineContext = executePipeline("test-propagation-pipeline", testPipelineDef);

        if( pipelineContext == null){
            fail("Returned null PipelineContext");
        }
        assertTrue(pipelineContext.getResult().containsKey("cpu"));
        assertEquals(2.5, pipelineContext.getResult().get("cpu"));
    }

    @Test
    public void arrayMappingTest() {
        var testPipelineDef = """
                    test-propagation-pipeline:
                      - test-output-factory:
                          memoryRequest: 256
                          cpuRequest:  2.5
                          someOther: test
                      - output-mapping:
                          type: array
                          mapping:
                            memoryRequest: jvm
                            cpuRequest:  jvm
                            someOther: commandLine
                      - test-latch-release:
                """;

        PipelineContext pipelineContext = executePipeline("test-propagation-pipeline", testPipelineDef);

        if( pipelineContext == null){
            fail("Returned null PipelineContext");
        }
        assertTrue(pipelineContext.getResult().containsKey("jvm"));
        assertEquals("256 2.5", String.valueOf(pipelineContext.getResult().get("jvm")).trim()); //TODO: handle string concat correctly

        assertTrue(pipelineContext.getResult().containsKey("commandLine"));
        assertEquals("test", String.valueOf(pipelineContext.getResult().get("commandLine")).trim()); //TODO: handle string concat correctly

    }

    @Test
    public void keyValueMappingTest() {
        var testPipelineDef = """
                    test-propagation-pipeline:
                      - test-output-factory:
                          memoryRequest: 256
                          cpuRequest:  2.5
                      - output-mapping:
                          type: key-value
                          mapping:
                            memoryRequest: jvm
                            cpuRequest:  jvm
                      - test-latch-release:
                """;

        PipelineContext pipelineContext = executePipeline("test-propagation-pipeline", testPipelineDef);

        if( pipelineContext == null){
            fail("Returned null PipelineContext");
        }
        assertTrue(pipelineContext.getResult().containsKey("jvm"));
//        assertEquals(2.5, pipelineContext.getResult().get("jvm"));
    }


}
