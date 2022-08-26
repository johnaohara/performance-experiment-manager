package io.hyperfoil.tools.pipelineManager.api;

import io.hyperfoil.tools.pipelineManager.plugins.horreum.api.ApiResult;

public class ApiException extends RuntimeException {
    public ApiResult failure;
    public ApiException(ApiResult failure) {
        this.failure = failure;
    }
}
