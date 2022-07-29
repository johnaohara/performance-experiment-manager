package io.hyperfoil.tools.experimentManager.api;


import java.util.HashMap;

//TODO: needs to be thread safe
public class PipelineContextImpl implements PipelineContext {

    private HashMap<String, Object> content = new HashMap<>();


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
        if(hasObject(name)){
            content.remove(name);
        }
    }
}
