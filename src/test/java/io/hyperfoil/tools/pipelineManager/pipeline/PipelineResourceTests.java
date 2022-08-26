package io.hyperfoil.tools.pipelineManager.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hyperfoil.tools.pipelineManager.plugins.srvc.TestDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Disabled //TODO:: define pipeline definition
public class PipelineResourceTests extends BasePipelineResourceTests{

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    String getPipelineDefinition() {
        return """
                
                """;
    }

    @Test
    public void testPipelineResourceEndpoint() throws JsonProcessingException {



        TestDTO dto = new TestDTO("my-pipeline", 10);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .post("/test/plugin")
                .then()
                .statusCode(204);
    }



}