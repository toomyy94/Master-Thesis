package pt.ptinovacao.arqospocket.core.notify;

/**
 * Wrapper class to provide information about task execution progress.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
public class TaskProgress extends TestProgress {

    private String taskDataId;

    public String getTaskDataId() {
        return taskDataId;
    }

    void setTaskDataId(String taskDataId) {
        this.taskDataId = taskDataId;
    }
}
