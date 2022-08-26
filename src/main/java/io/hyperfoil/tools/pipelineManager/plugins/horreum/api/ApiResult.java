package io.hyperfoil.tools.pipelineManager.plugins.horreum.api;

public class ApiResult {

    public enum NewExperimentStatus {
        SUCCESS,
        FAILURE
    }

    public static ApiResult failure() {
        return new ApiResult(NewExperimentStatus.FAILURE);
    }
    public static ApiResult failure(String msg) {
        return new ApiResult(NewExperimentStatus.FAILURE, msg);
    }

    public static ApiResult success(){
        return new ApiResult(NewExperimentStatus.SUCCESS);
    }

    public static ApiResult success(String msg){
        return new ApiResult(NewExperimentStatus.SUCCESS, msg);
    }

    public ApiResult(NewExperimentStatus status) {
        this(status, "");
    }
    public ApiResult(NewExperimentStatus status, String msg) {
        this.status = status;
        this.message = msg;
    }

    public NewExperimentStatus status;
    public String message;

}
