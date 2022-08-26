package io.hyperfoil.tools.pipelineManager.api.dto;

public class ExperimentConfig {

    private String experimentName;
/*
    private String horreumJobID;
    private JenkinsJob jenkinsJob;
    private QDupJob qDupJob;
    private Horreum horreum;
    private HpoExperiment hpoExperiment;
*/
/*
    public String getExperimentName() {
        return pipelineName;
    }

    public String getHorreumJobID() {
        return horreumJobID;
    }

    public void defineJenkinsJob(String job, String job_url ) {
        this.jenkinsJob = new JenkinsJob(job, job_url, new HashMap<>());
    }

    public void defineQdupJob(String targetHost, String user, String scriptUrl) {
        this.qDupJob = new QDupJob(targetHost, user, scriptUrl, new HashMap<>());
    }

    public void defineHpoExperiment(String name, String test_id, Integer total_trials, Integer parallel_trials, String value_type,
                                    String hpo_algo_impl, String objective_function, String slo_class, String direction) {
        this.hpoExperiment = new HpoExperiment(name, test_id, total_trials, parallel_trials, -1, value_type,
                hpo_algo_impl, objective_function, slo_class, direction, new ArrayList<>());
    }

    public void defineHorreum(String jobID){
        this.horreum = new Horreum(jobID, null); //TODO:: define auth for Horreum configuration
    }


    public JenkinsJob getJenkinsJob() {
        return jenkinsJob;
    }

    public QDupJob getqDupJob() {
        return qDupJob;
    }

    public Horreum getHorreum() {
        return horreum;
    }

    public HpoExperiment getHpoExperiment() {
        return hpoExperiment;
    }

    public void setExperimentName(String pipelineName) {
        this.pipelineName = pipelineName;
    }
    public void setHorreumJobID(String jobID) {
        this.horreumJobID = jobID;
    }


    @Override
    public String toString() {
        return "ExperimentConfg{" +
                "experiment=" + pipelineName +
                ", jenkins=" + jenkinsJob +
                ", horreum=" + horreum +
                ", hpo=" + hpoExperiment +
                '}';
    }*/
}
