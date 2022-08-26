package io.hyperfoil.tools.pipelineManager.api;

public interface PipelineExecutable extends Runnable {

    PipelineExecutable next();

    void next(PipelineExecutable nxt);

    void setContext(PipelineContext context);

    void run(PipelineContext context);

    void initialize() throws ExecutableInitializationException;
}
