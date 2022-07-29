package io.hyperfoil.tools.experimentManager.pipeline;

import io.hyperfoil.tools.experimentManager.api.PipelineExecutable;

public class ExperimentPipeline {

    private volatile String experimentName;
    private volatile PipelineExecutable head;

    public void pipelineHead(PipelineExecutable executable) {
        this.head = executable;
    }

    public PipelineExecutable getHead(){
        return head;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void experimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public void addPipelineExecutable(PipelineExecutable executable) {
        if (head == null) {
            this.head = executable;
        } else {
            PipelineExecutable pointer = head;

            while (pointer.next() != null) {
                pointer = pointer.next();
            }
            pointer.next(executable);
        }
    }

    public int pipelinesize() {
        int count = 0;
        if(head != null){
            count++;
            PipelineExecutable pointer = head;

            while (pointer.next() != null) {
                pointer = pointer.next();
                count++;
            }
        }
        return count;
    }

}
