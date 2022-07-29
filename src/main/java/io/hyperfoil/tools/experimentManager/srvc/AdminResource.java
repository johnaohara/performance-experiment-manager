package io.hyperfoil.tools.experimentManager.srvc;

import io.hyperfoil.tools.experimentManager.ExperimentManager;
import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Path("/admin")
public class AdminResource {

    @Inject
    ExperimentManager experimentManager;

//    @GET
//    @Path("/plugins")
//    public Uni<List<String>> getInstalledHandlers(){
//        return Uni.createFrom().item(experimentManager.getInstalledHandlers());
//    }

    @GET
    @Path("/plugin/{name}")
    public Uni<Plugin> getPluginResourceByName(@PathParam("name") String name){
        Plugin plugin = experimentManager.getPlugin(name);
        if (plugin != null){
            return Uni.createFrom().item(plugin);
        }
        throw new WebApplicationException("Plugin not found: " + name);
    }

}
