package io.hyperfoil.tools.experimentManager.api;

import io.hyperfoil.tools.experimentManager.plugins.horreum.api.ApiResult;

public class ApiException extends RuntimeException {
    public ApiResult failure;
    public ApiException(ApiResult failure) {
        this.failure = failure;
    }
}
