package io.hyperfoil.tools.pipelineManager.srvc;

import io.hyperfoil.tools.pipelineManager.PipelineManager;
import io.hyperfoil.tools.pipelineManager.api.Plugin;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

@Path("/admin")
public class AdminResource {

    @Inject
    PipelineManager pipelineManager;

//    @GET
//    @Path("/plugins")
//    public Uni<List<String>> getInstalledHandlers(){
//        return Uni.createFrom().item(experimentManager.getInstalledHandlers());
//    }

    @GET
    @Path("/plugin/{name}")
    public Uni<Plugin> getPluginResourceByName(@PathParam("name") String name){
        Plugin plugin = pipelineManager.getPlugin(name);
        if (plugin != null){
            return Uni.createFrom().item(plugin);
        }
        throw new WebApplicationException("Plugin not found: " + name);
    }

}
