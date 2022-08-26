package io.hyperfoil.tools.pipelineManager.pipeline;

import io.hyperfoil.tools.pipelineManager.api.ExecutableInitializationException;
import io.hyperfoil.tools.pipelineManager.api.PipelineExecutable;

public class Pipeline {

    private volatile String pipelineName;
    private volatile PipelineExecutable head;
    private volatile PipelineState state;

    public void pipelineHead(PipelineExecutable executable) {
        this.head = executable;
    }

    public PipelineExecutable getHead(){
        return head;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void pipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public void addExecutable(PipelineExecutable executable) {
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

    public int size() {
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

    public void initialize() throws ExecutableInitializationException {
        PipelineExecutable cur = head;
        while(cur != null){
            cur.initialize();
            cur = cur.next();
        }

    }


    public enum PipelineState{
        UNUSED
    }
}
