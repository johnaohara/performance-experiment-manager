package io.hyperfoil.tools.experimentManager.model;

import io.hyperfoil.tools.experimentManager.plugins.horreum.data.HorreumDAO;
import io.hyperfoil.tools.experimentManager.plugins.jenkins.data.JenkinsDAO;
import io.hyperfoil.tools.experimentManager.plugins.qdup.data.QDupDAO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "experiment")
public class ExperimentDAO extends PanacheEntity {

    @NotNull
    public String name;
    @NotNull
    public Integer test_id;
    public State state;
    public Integer parrallelism = 1;
    public Integer total_trials = 100;
    public String direction;
    public String hpo_algo_impl;
    public String objective_function;
    public String value_type;
    public Integer currentTrial = 0;

    @OneToMany(cascade = CascadeType.ALL)
    public Set<TunableDAO> tunables;

    @OneToMany(cascade = CascadeType.ALL)
    public Map<Integer, TrialResultDAO> trialHistory;

    @OneToOne(cascade = CascadeType.ALL)
    public TrialResultDAO recommended;

    @OneToOne(cascade=CascadeType.ALL)
    public HorreumDAO horreum;

    @OneToOne(cascade=CascadeType.ALL)
    public JenkinsDAO jenkins;

    @OneToOne(cascade=CascadeType.ALL)
    public QDupDAO qDup;
/*
TODO: extract from here and use for
    1 - DAO
    2 - DTO
    3 - State Machine
 */

    public enum State {
        NEW {
            @Override
            public State nextState() {
                return READY;
            }
        },
        READY {
            @Override
            public State nextState() {
                return RUNNING;
            }
        },
        RUNNING {
            @Override
            public State nextState() {
                return FINISHED;
            }
        },
        PAUSED {
            @Override
            public State nextState() {
                return RUNNING;
            }
        },
        FAILURE {
            @Override
            public State nextState() {
                return null;
            }
        },
        FINISHED {
            @Override
            public State nextState() {
                return null;
            }
        };

        public abstract State nextState();
        }

    public static ExperimentDAO findByTestId(Integer testId) {
        Map<String, Object> params = new HashMap<>();
        params.put("test_id", testId);
        params.put("state", State.RUNNING);
        PanacheQuery<ExperimentDAO> query = find("test_id = :test_id and state = :state", params);
        return query.firstResult();
    }

}
