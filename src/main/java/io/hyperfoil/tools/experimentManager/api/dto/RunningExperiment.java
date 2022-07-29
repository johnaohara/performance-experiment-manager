package io.hyperfoil.tools.experimentManager.api.dto;

import io.hyperfoil.tools.experimentManager.model.ExperimentDAO;

public record RunningExperiment(String experimentName, Integer total_Trials, Integer currentTrial, ExperimentDAO.State currentState) {}
