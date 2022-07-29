package io.hyperfoil.tools.experimentManager.api;

public interface PipelineContext {

    <T> void setObject(String name, T object);
    <T> T getObject(String name);
    boolean hasObject(String name);
    void removeObject(String name);

}
