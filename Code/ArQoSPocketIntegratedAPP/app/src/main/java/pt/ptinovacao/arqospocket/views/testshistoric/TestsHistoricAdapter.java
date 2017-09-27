package pt.ptinovacao.arqospocket.views.testshistoric;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.BaseTaskEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.views.AddSwipeInRecyclerView;
import pt.ptinovacao.arqospocket.views.tests.testsdetails.TestsDetailsActivity;
import pt.ptinovacao.arqospocket.views.testshistoric.testsdetails.TaskDetailsActivity;

/**
 * AnomalyHistoricAdapter
 * <p>
 * Created by pedro on 17/04/2017.
 */
public class TestsHistoricAdapter extends RecyclerView.Adapter<TestsHistoricAdapter.ViewHolder>
        implements AddSwipeInRecyclerView.OnClickOfSwipe {

    private ArrayList<ExecutingEvent> testDataArrayList;

    private ArQoSBaseActivity activity;

    final private TestParser testParser = new TestParser();

    final private AddSwipeInRecyclerView recyclerViewSwipeUtils = new AddSwipeInRecyclerView();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerViewSwipeUtils.setUpItemTouchHelper(recyclerView, this);
        recyclerViewSwipeUtils.setUpAnimationDecoratorHelper(recyclerView);
    }

    TestsHistoricAdapter(ArQoSBaseActivity activity, ArrayList<ExecutingEvent> myDataset) {
        this.activity = activity;
        this.testDataArrayList = myDataset;
    }

    @Override
    public TestsHistoricAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_tests_historico, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ExecutingEvent executingEvent = testDataArrayList.get(position);
        final TestData testData = testParser.parseSingleTest(executingEvent.getTestData());
        final TestResult testResult = testParser.parseSingleResult(executingEvent.getResultData());

        holder.tvHistoricTestType.setText(testData.getTestName());

        holder.rowLayoutCardView.setTag(position);
        holder.rowLayoutCardView.setOnClickListener(new OnClickListenerToHistoric());

        if (executingEvent.isTestFailed() || (testResult != null && isTaskDataWithSuccess(testResult))) {
            holder.ivResult.setBackgroundResource(R.mipmap.icon_erro_big);
        } else {
            holder.ivResult.setBackgroundResource(R.mipmap.icon_sucesso_big);
        }

        if (testResult != null) {
            holder.tvTestInHistoric.setText(DateUtils.convertDateToString(testResult.getEndDate()));
        }

        ExecutingEventDao executingEventDao =
                activity.getArQosApplication().getDatabaseHelper().createExecutingEventDao();

        BaseTaskEvent baseTaskEvent = executingEventDao.readExecutingEventFirstOrNull(executingEvent.getId());

        if (baseTaskEvent != null) {
            ConnectionTechnology connectionTechnology =
                    ConnectionTechnology.fromConnectionTechnology(baseTaskEvent.getConnectionTechnology());

            holder.ivTechnologiesUsed.setBackgroundResource(CalculateStateTestsToResourceUtils
                    .getTestBigRowIconResource(connectionTechnology, !isTaskDataWithSuccess(testResult)));
        }
    }

    private class OnClickListenerToHistoric implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TestsHistoricAdapter.this.onClick(((Integer) v.getTag()));
        }
    }

    @Override
    public void onClick(Integer position) {

        if (position == null) {
            return;
        }

        Intent intent = new Intent(activity, TaskDetailsActivity.class);
        intent.putExtra(TestsDetailsActivity.KEY_TEST_TO_DETAILS, testDataArrayList.get(position).getId());
        notifyDataSetChanged();

        activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return testDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHistoricTestType;

        LinearLayout rowLayoutCardView;

        ImageView ivResult;

        ImageView ivTechnologiesUsed;

        TextView tvTestInHistoric;

        public ViewHolder(View view) {
            super(view.getRootView());
            ivResult = (ImageView) view.findViewById(R.id.img_historico_teste_result);
            tvHistoricTestType = (TextView) view.findViewById(R.id.tv_historico_teste_type);
            rowLayoutCardView = (LinearLayout) view.findViewById(R.id.schedule_item_layout);
            ivTechnologiesUsed = (ImageView) view.findViewById(R.id.img_historico_teste_tecnologia);
            tvTestInHistoric = (TextView) view.findViewById(R.id.tv_historico_teste_date);
        }
    }

    public static boolean isTaskDataWithSuccess(TestResult taskResults) {
        if (taskResults == null) {
            return true;
        }

        for (TaskResult taskResult : taskResults.getTaskResults()) {
            if (Strings.nullToEmpty(taskResult.getStatus()).contains(BaseExecutableTask.RESULT_TASK_FAILED)) {
                return true;
            }
        }
        return false;
    }
}