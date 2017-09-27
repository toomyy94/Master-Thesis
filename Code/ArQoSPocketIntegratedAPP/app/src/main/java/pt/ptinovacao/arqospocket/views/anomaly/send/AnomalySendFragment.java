package pt.ptinovacao.arqospocket.views.anomaly.send;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.AnomalyActivity;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;

public class AnomalySendFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalySendFragment.class);

    private OnAnomalySendListener listener;

    private ImageView ivTitle;

    private TextView tvTitle;

    private EditText etText;

    private LinearLayout llFinish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_anomaly_send, container, false);

        initializeViews(rootView);
        listeners();

        setValues();

        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {
        ivTitle = (ImageView) rootView.findViewById(R.id.iv_title);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);

        llFinish = (LinearLayout) rootView.findViewById(R.id.botao_finalizar);
        etText = (EditText) rootView.findViewById(R.id.textofeedback);
    }

    private void listeners() {
        llFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onAnomalySend(etText.getText().toString());
            }
        });

    }

    private void setValues() {
        AnomalyReport anomalyTypeSelected = ((AnomalyActivity) getActivity()).getAnomalyTypeSelected();

        ivTitle.setBackgroundResource(anomalyTypeSelected.getResourceIcon());
        tvTitle.setText(anomalyTypeSelected.getResourceText());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnomalySendListener) {
            listener = (OnAnomalySendListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnomalySendListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnAnomalySendListener {

        void onAnomalySend(String feedback);
    }
}
