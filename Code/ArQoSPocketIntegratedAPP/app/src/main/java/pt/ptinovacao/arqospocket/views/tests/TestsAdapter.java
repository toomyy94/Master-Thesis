package pt.ptinovacao.arqospocket.views.tests;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.persistence.models.BaseEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.views.tests.testsdetails.TestsDetailsActivity;

/**
 * Created by pedro on 17/04/2017.
 */
public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestsAdapter.class);

    private ArrayList<BaseEvent> baseEventArrayList;

    private ArQoSBaseActivity activity;

    public TestsAdapter(ArQoSBaseActivity activity, ArrayList<BaseEvent> testDataArrayList) {
        this.activity = activity;
        this.baseEventArrayList = testDataArrayList;
    }

    @Override
    public TestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_tests, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TestParser testParser = new TestParser();
        final TestData testData = testParser.parseSingleTest(baseEventArrayList.get(position).getTestData());

        final TestsFragment.StateTest stateTest;

        if (baseEventArrayList.get(position) instanceof ExecutingEvent) {
            stateTest = TestsFragment.StateTest.ONGOIONG;
        } else if (baseEventArrayList.get(position) instanceof ScheduledEvent) {
            stateTest = TestsFragment.StateTest.INACTIVE;
        } else {
            stateTest = TestsFragment.StateTest.EVERY;
        }

        if (testData.getTestType() == 7) {
            holder.ivState.setBackgroundResource(R.mipmap.icon_on_demand_litle);
        } else {
            holder.ivState.setBackgroundResource(R.mipmap.icon_agendado);
        }

        CalculateStateTestsToResourceUtils calculateStateTestsToResourceUtils =
                new CalculateStateTestsToResourceUtils(stateTest);

        holder.tvNameTest.setText(testData.getTestName());

        holder.rowLayoutCardView.setTag(position);
        holder.rowLayoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TestsDetailsActivity.class);
                intent.putExtra(TestsDetailsActivity.KEY_TEST_TO_DETAILS,
                        baseEventArrayList.get((Integer) v.getTag()).getId());
                intent.putExtra(TestsTabsTestFragment.KEY_EXTRA_TO_STATE, stateTest);
                activity.startActivity(intent);
            }
        });

        holder.ivPercentage.setImageResource(calculateStateTestsToResourceUtils.getIntResourceImage());
        holder.tvPercentage.setTextColor(ContextCompat
                .getColor(activity.getBaseContext(), calculateStateTestsToResourceUtils.getIntResourceColor()));
        holder.tvPercentage
                .setText(activity.getString(calculateStateTestsToResourceUtils.getIntResourceStringPercentage()));
        holder.tvNameTest.setText(testData.getTestName());
    }

    @Override
    public int getItemCount() {
        return baseEventArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameTest;

        FrameLayout rowLayoutCardView;

        ImageView ivPercentage;

        TextView tvPercentage;

        ImageView ivButton;

        ImageView ivState;

        public ViewHolder(View view) {
            super(view.getRootView());

            tvNameTest = (TextView) view.findViewById(R.id.nome_teste);
            rowLayoutCardView = (FrameLayout) view.findViewById(R.id.row_tests);

            ivPercentage = (ImageView) view.findViewById(R.id.imagePercent);
            tvPercentage = (TextView) view.findViewById(R.id.text_percent);
            ivButton = (ImageView) view.findViewById(R.id.imagebutton);
            ivState = (ImageView) view.findViewById(R.id.imagestate);
        }
    }
}