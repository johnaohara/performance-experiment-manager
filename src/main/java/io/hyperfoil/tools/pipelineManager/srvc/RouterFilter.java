package io.hyperfoil.tools.pipelineManager.srvc;

import io.vertx.ext.web.Router;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@ApplicationScoped
public class RouterFilter {

    private static final String[] PATH_PREFIXES = { "/api/", "/connect", "/dev" };
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(".*[.][a-zA-Z\\d]+");

    public void init(@Observes Router router) {
        router.get("/*").handler(rc -> {
            String path = rc.normalizedPath();
            if (!path.equals("/") && Stream.of(PATH_PREFIXES).noneMatch(path::startsWith) && !FILE_NAME_PATTERN.matcher(path).matches()) {
                rc.reroute("/");
            } else {
                rc.next();
            }
        });
    }
}
