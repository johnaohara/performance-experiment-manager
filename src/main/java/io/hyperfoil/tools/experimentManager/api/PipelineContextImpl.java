package io.hyperfoil.tools.experimentManager.api;


import java.util.HashMap;
import java.util.Map;

//TODO: needs to be thread safe
public class PipelineContextImpl implements PipelineContext {

    private HashMap<String, Object> content = new HashMap<>();

    //TODO - do we need to duplicate this?
    private Map<String, Object> result = null;


    @Override
    public <T> void setObject(String name, T object) {
        content.put(name, object);
    }

    @Override
    public <T> T getObject(String name) {
        return (T) content.get(name);
    }

    @Override
    public boolean hasObject(String name) {
        return content.containsKey(name);
    }

    @Override
    public void removeObject(String name) {
        if (hasObject(name)) {
            content.remove(name);
        }
    }

    @Override
    public Map<String, Object> getResult() {
        return this.result;
    }

    @Override
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
