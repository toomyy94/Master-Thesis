package pt.ptinovacao.arqospocket.views.testshistoric.testsdetails;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.serialization.TaskResultFactory;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.utils.FindResourceToDetailsTasks;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;

/**
 * Created by pedro on 17/04/2017.
 */
public class TestsDetailsDialog extends Dialog {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestsDetailsDialog.class);

    private ImageView ivPercentage;

    private TextView tvPercentage, tvName;

    private RecyclerView recyclerView;

    public TestsDetailsDialog(Context context, TaskResult taskResult) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list_tests_details);

        initializeViews();
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new DetailsTasksAdapter(taskResult));

        CalculateStateTestsToResourceUtils colorPercent;
        if (BaseExecutableTask.RESULT_TASK_SUCCESS.equals(taskResult.getStatus())) {
            colorPercent = new CalculateStateTestsToResourceUtils(TestsFragment.StateTest.COMPLETED);
        } else {
            colorPercent = new CalculateStateTestsToResourceUtils(TestsFragment.StateTest.FAILED);
        }

        ivPercentage.setImageResource(colorPercent.getIntResourceImage());
        tvPercentage.setText(colorPercent.getIntResourceStringPercentage());
        tvPercentage.setTextColor(colorPercent.getIntResourceColor());
        tvName.setText(taskResult.getTaskName());
        tvName.setTextColor(ContextCompat.getColor(getContext(), colorPercent.getIntResourceColor()));
    }

    private void initializeViews() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        ivPercentage = (ImageView) findViewById(R.id.image_Percent_deta);
        tvPercentage = (TextView) findViewById(R.id.text_percent_deta);
        tvName = (TextView) findViewById(R.id.nome_teste_deta);
    }

    private class DetailsTasksAdapter extends RecyclerView.Adapter<DetailsTasksAdapter.ViewHolder> {

        private ArrayList<AbstractMap.SimpleEntry> simpleEntryArrayList;

        public DetailsTasksAdapter(TaskResult taskResult) {
            simpleEntryArrayList = TaskResultFactory.decomposeObjectToArrayList(taskResult);
            FindResourceToDetailsTasks.validateRemoveLines(simpleEntryArrayList);
        }

        @Override
        public DetailsTasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_details_test_tasks, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            FindResourceToDetailsTasks findResourceToDetailsTasks = FindResourceToDetailsTasks
                    .getFindResourceToDetailsTasks(simpleEntryArrayList.get(position).getKey().toString());

            holder.tvKey.setText(getContext().getString(findResourceToDetailsTasks.getResource()));
            String text = FindResourceToDetailsTasks
                    .getTextOfType(getContext(), findResourceToDetailsTasks.getSettingType(),
                            simpleEntryArrayList.get(position).getValue().toString());
            holder.tvValue.setText(getHtmlText(text));
        }

        @Override
        public int getItemCount() {
            return simpleEntryArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvKey, tvValue;

            public ViewHolder(View view) {
                super(view.getRootView());

                tvKey = (TextView) view.findViewById(R.id.tv_key_result_task);
                tvValue = (TextView) view.findViewById(R.id.tv_value_result_task);
            }
        }

    }

    private Spanned getHtmlText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        }
        //noinspection deprecation
        return Html.fromHtml(text);
    }

}
