package pt.ptinovacao.arqospocket.views.testshistoric.testsdetails;

import android.view.View;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.voicecall.audio.MediaPlayerHelper;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.views.TasksBaseAdapter;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;

/**
 * Created by pedro on 17/04/2017.
 */
public class TasksAdapter extends TasksBaseAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TasksAdapter.class);

    private ArrayList<TaskResult> taskResultArrayList;

    public TasksAdapter(ArQoSBaseActivity activity, TestsFragment.StateTest stateTest,
            ArrayList<TaskResult> testDataArrayList) {
        super(activity, stateTest);
        this.taskResultArrayList = testDataArrayList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        TaskResult taskResult = taskResultArrayList.get(position);

        CalculateStateTestsToResourceUtils colorPercent;

        if (BaseExecutableTask.RESULT_TASK_SUCCESS.equals(taskResult.getStatus())) {
            colorPercent = new CalculateStateTestsToResourceUtils(TestsFragment.StateTest.COMPLETED);
        } else {
            colorPercent = new CalculateStateTestsToResourceUtils(TestsFragment.StateTest.FAILED);
        }

        holder.ivPercentage.setImageResource(colorPercent.getIntResourceImage());
        holder.tvPercentage.setText(colorPercent.getIntResourceStringPercentage());
        holder.tvPercentage.setTextColor(getActivity().getResources().getColor(colorPercent.getIntResourceColor()));
        holder.tvName.setText(taskResult.getTaskName());

        if (TaskType.RECORD_AUDIO_TASK.equals(taskResult.getTaskId()) && !Strings.isNullOrEmpty(
                ((RecordAudioTaskResult) taskResultArrayList.get(position)).getAudioRecordingFileName())) {
            holder.buttonPlay.setVisibility(View.VISIBLE);
            holder.buttonPlay.setTag(new Integer(position));
            holder.buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int element = (int) v.getTag();

                    if (MediaPlayerHelper.getInstance(getActivity().getArQosApplication()).isStart()) {
                        MediaPlayerHelper.getInstance(getActivity().getArQosApplication()).stopPlaying();
                        holder.buttonPlay.setText("Play");
                    } else {
                        holder.buttonPlay.setText("Stop");
                        RecordAudioTaskResult recordAudioTaskResult =
                                (RecordAudioTaskResult) taskResultArrayList.get(element);

                        boolean isSuccess = MediaPlayerHelper.getInstance(getActivity().getArQosApplication())
                                .startAudio(recordAudioTaskResult.getAudioRecordingFileName(),
                                        new MediaPlayerHelper.UpdateUI() {
                                            @Override
                                            public void update() {
                                                holder.buttonPlay.setText("Play");
                                            }
                                        });

                        if (!isSuccess) {
                            holder.buttonPlay.setText("Error");
                        }
                    }
                }
            });

        } else {
            holder.buttonPlay.setVisibility(View.GONE);
        }

        holder.llIndicator.setTag(new Integer(position));
        holder.llIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TestsDetailsDialog testDetailsDialog =
                        new TestsDetailsDialog(getActivity(), taskResultArrayList.get((int) v.getTag()));
                testDetailsDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskResultArrayList.size();
    }
}