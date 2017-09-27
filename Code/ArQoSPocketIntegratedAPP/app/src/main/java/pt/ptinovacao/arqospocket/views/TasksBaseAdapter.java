package pt.ptinovacao.arqospocket.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;

/**
 * Created by pedro on 17/04/2017.
 */
public abstract class TasksBaseAdapter extends RecyclerView.Adapter<TasksBaseAdapter.ViewHolder>  {
    private final static Logger LOGGER = LoggerFactory.getLogger(TasksBaseAdapter.class);

    private ArQoSBaseActivity activity;
    private TestsFragment.StateTest stateTest;

    public TasksBaseAdapter(ArQoSBaseActivity activity, TestsFragment.StateTest stateTest) {
        this.activity = activity;
        this.stateTest = stateTest;
    }

    @Override
    public TasksBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_tasks, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPercentage;
        public TextView tvPercentage;
        public TextView tvName;
        public ImageView ivButton;

        public Button buttonPlay;

        public LinearLayout llIndicator;

        public ViewHolder(View view) {
            super(view.getRootView());

            ivPercentage = (ImageView) view.findViewById(R.id.image_Percent_deta);
            tvPercentage = (TextView) view.findViewById(R.id.text_percent_deta);
            tvName = (TextView) view.findViewById(R.id.nome_teste_deta);
            ivButton = (ImageView) view.findViewById(R.id.imagebutton_deta);
            buttonPlay = (Button)view.findViewById(R.id.button_play);

            llIndicator = (LinearLayout)view.findViewById(R.id.linearLayoutLinhaTipdetalhes);
        }
    }

    public ArQoSBaseActivity getActivity() {
        return activity;
    }

    public void setActivity(ArQoSBaseActivity activity) {
        this.activity = activity;
    }

    public TestsFragment.StateTest getStateTest() {
        return stateTest;
    }

    public void setStateTest(TestsFragment.StateTest stateTest) {
        this.stateTest = stateTest;
    }
}