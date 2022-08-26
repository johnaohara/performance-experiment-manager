package io.hyperfoil.tools.pipelineManager.api.dto;

import io.hyperfoil.tools.pipelineManager.model.PipelineDAO;

public record RunningPipeline(String pipelineName, Integer totalIterations, Integer currentIteration, PipelineDAO.State currentState) {}
