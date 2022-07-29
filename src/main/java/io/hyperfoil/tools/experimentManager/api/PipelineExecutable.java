package io.hyperfoil.tools.experimentManager.api;

public interface PipelineExecutable extends Runnable {

    PipelineExecutable next();

    void next(PipelineExecutable nxt);

    void setContext(PipelineContext context);

    void run(PipelineContext context);
}
