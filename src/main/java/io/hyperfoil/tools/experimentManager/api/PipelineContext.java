package io.hyperfoil.tools.experimentManager.api;

import java.util.Map;

public interface PipelineContext {

    <T> void setObject(String name, T object);
    <T> T getObject(String name);
    boolean hasObject(String name);
    void removeObject(String name);

    //TODO:: should we use a separate data structure, or insert as regular context Object with reserved key name
    Map<String, Object> getResult();
    void setResult(Map<String, Object> result);


}
