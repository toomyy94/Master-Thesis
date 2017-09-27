package pt.ptinovacao.arqospocket.views.tests.testsdetails;

import android.support.v4.content.ContextCompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.views.TasksBaseAdapter;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;

/**
 * Created by pedro on 17/04/2017.
 */
public class TasksAdapter extends TasksBaseAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TasksAdapter.class);

    private ArrayList<TaskData> testDataArrayList;

    public TasksAdapter(ArQoSBaseActivity activity, TestsFragment.StateTest stateTest,
            ArrayList<TaskData> testDataArrayList) {
        super(activity, stateTest);
        this.testDataArrayList = testDataArrayList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TaskData taskData = testDataArrayList.get(position);

        CalculateStateTestsToResourceUtils colorPercent = new CalculateStateTestsToResourceUtils(getStateTest());

        holder.ivPercentage.setImageResource(colorPercent.getIntResourceImage());
        holder.tvPercentage.setText(colorPercent.getIntResourceStringPercentage());
        holder.tvPercentage.setTextColor(ContextCompat.getColor(getActivity(), colorPercent.getIntResourceColor()));
        holder.tvName.setText(taskData.getTaskName());
    }

    @Override
    public int getItemCount() {
        return testDataArrayList.size();
    }

}