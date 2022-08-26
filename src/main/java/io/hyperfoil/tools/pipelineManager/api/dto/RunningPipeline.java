package io.hyperfoil.tools.pipelineManager.api.dto;

import io.hyperfoil.tools.pipelineManager.model.PipelineDAO;

public record RunningPipeline(String pipelineName, Integer total_Trials, Integer currentTrial, PipelineDAO.State currentState) {}
