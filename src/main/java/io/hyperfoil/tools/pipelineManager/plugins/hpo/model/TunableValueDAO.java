package io.hyperfoil.tools.pipelineManager.plugins.hpo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class TunableValueDAO extends PanacheEntity {
    public String tunable;
    public Float value;

    public TunableValueDAO() {
    }

    public TunableValueDAO(String tunable, Float value) {
        this.tunable = tunable;
        this.value = value;
    }
}
