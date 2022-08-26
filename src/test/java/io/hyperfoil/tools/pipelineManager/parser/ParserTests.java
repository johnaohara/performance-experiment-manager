package io.hyperfoil.tools.pipelineManager.parser;

import io.hyperfoil.tools.pipelineManager.api.PipelineExecutable;
import io.hyperfoil.tools.pipelineManager.pipeline.Pipeline;
import io.hyperfoil.tools.pipelineManager.plugins.test.TestProcessor;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class ParserTests {

    @Inject
    ConfigurationParser yamlParser;

    @Test
    public void testSingleStepPipeline() {
        var testPipelineDef = """
                test-pipeline:
                  - test-receiver:
                      test-id: 1
                """;

        try {
            Pipeline config = yamlParser.parseYaml(testPipelineDef);
            assertNotNull(config);
            assertEquals("test-pipeline", config.getPipelineName());
            assertEquals(1, config.size());


        } catch (ConfigParserException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testMultiStepPipeline() {
        var testPipelineDef = """
                    test-pipeline:
                      - test-receiver:
                          receiver-id: 10
                      - test-processor:
                          processor-id: 12
                """;

        try {
            Pipeline config = yamlParser.parseYaml(testPipelineDef);
            assertNotNull(config);
            assertEquals("test-pipeline", config.getPipelineName());
            assertEquals(2, config.size());


            PipelineExecutable executable = config.getHead().next();
            assertTrue(executable instanceof TestProcessor.TestProcessorExecutable);

            assertEquals("12", ((TestProcessor.TestProcessorExecutable) executable).getId());

        } catch (ConfigParserException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testInvalidContextSetterPipeline() {
        var testPipelineDef = """
                    invalid-context-pipeline:
                      - context-setter:
                          variable: my-test-variable
                """;

        try {
            yamlParser.parseYaml(testPipelineDef);
            fail("Should have thrown ConfigParserException");

        } catch (ConfigParserException e) {
            assertTrue(e.getErrors().contains("Expecting 2 elements for: context-setter"));
        }
    }
}
