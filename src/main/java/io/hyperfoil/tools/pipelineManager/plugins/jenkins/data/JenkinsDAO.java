package io.hyperfoil.tools.pipelineManager.plugins.jenkins.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.HashMap;

@Entity
public class JenkinsDAO extends PanacheEntity {

    public String job;
    public String job_url;

    @SuppressWarnings("JpaAttributeTypeInspection")
    public HashMap<String, String> params;
}
