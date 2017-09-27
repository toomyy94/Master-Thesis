package pt.ptinovacao.arqospocket.views.radiologs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologReport;

/**
 * Created by Tomás Rodrigues on 14/06/2017.
 */

public class RadiologsFragment extends ArQoSBaseFragment {
    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsFragment.class);

    private RadiologReport snapshotTypeSelected = RadiologReport.SNAPSHOT;

    private MobileNetworkManager mobileNetworkManager;
    private MobileInfoData mobileInfoData;

    RadiologsActivity activity;
    private RadiologsFragment.OnRadiologSendListener listener;
    private EditText texto;
    private LinearLayout bot_finalizar;

    private GeoLocationManager geoLocationInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_radiologs_send, container, false);

        initializeViews(rootView);
        onListeners();

        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        activity = (RadiologsActivity) getActivity();
        geoLocationInfo =  GeoLocationManager.getInstance(activity.getArQosApplication());
        texto = (EditText) rootView.findViewById(R.id.textofeedback);
        bot_finalizar = (LinearLayout) rootView.findViewById(R.id.botao_finalizar);
    }

    private void onListeners() {

        bot_finalizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String texto_feedback = texto.getText().toString();
                mobileNetworkManager = MobileNetworkManager.getInstance(getArQosApplication());
                mobileInfoData = MobileNetworkManager.getInstance(getArQosApplication()).getMobileInfoData();
                if (mobileNetworkManager != null && mobileNetworkManager.isMobileAvailable() && mobileNetworkManager.getConnectionMode() != 6 && mobileInfoData.getConnectionMode() != 6 &&
                        mobileNetworkManager.getConnectionMode() != 7 &&  mobileInfoData.getMobileNetworkMode() != MobileNetworkMode.NONE  &&  mobileInfoData.getConnectionMode() != 7 ) {
                    Radiolog radiolog = new Radiolog();
                    RadiologsManager radiologsManager = RadiologsManager.getInstance(getArQosApplication());

                    radiolog.setLatitude(geoLocationInfo.getLocation().getLatitude());
                    radiolog.setLongitude(geoLocationInfo.getLocation().getLongitude());
                    radiolog.setReportType(getArQosApplication().getBaseContext().getResources().getString(snapshotTypeSelected.getResourceText()));
                    radiolog.setRadiologContent(radiologsManager.generateSnapshot(texto_feedback));
                    radiolog.setUserFeedback(texto_feedback);
                    Toast.makeText(activity, "Snapshot registered!", Toast.LENGTH_SHORT).show();

                    listener.onRadiologSend(radiolog);
                }
                else Toast.makeText(activity, "No mobile network data available!", Toast.LENGTH_SHORT).show();



            }
        });

        texto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    activity.hideKeyboard(v);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RadiologsFragment.OnRadiologSendListener) {
            listener = (RadiologsFragment.OnRadiologSendListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRadiologSendListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnRadiologSendListener {
        void onRadiologSend(Radiolog radiolog);
    }

}
