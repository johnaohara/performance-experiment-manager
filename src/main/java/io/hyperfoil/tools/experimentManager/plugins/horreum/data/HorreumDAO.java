package io.hyperfoil.tools.experimentManager.plugins.horreum.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class HorreumDAO extends PanacheEntity {

    public String job;
    public Integer jobID;
    public String auth;
    public String metric;


}
