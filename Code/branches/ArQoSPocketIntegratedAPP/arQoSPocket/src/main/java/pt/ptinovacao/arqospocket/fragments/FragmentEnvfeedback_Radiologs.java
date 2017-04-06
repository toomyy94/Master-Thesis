package pt.ptinovacao.arqospocket.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;
import pt.ptinovacao.arqospocket.activities.ActivityRadiologs;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import pt.ptinovacao.arqospocket.util.MenuOption;

public class FragmentEnvfeedback_Radiologs extends Fragment implements OnClickListener{
	private static final String TAG = "Envfeedback_Radiologs";

	EditText texto;
	LinearLayout bot_finalizar;
	String texto_feedback;
	ActivityRadiologs activity;
    IService iService;
	boolean Fed=false;
	MenuOption HomeP;
	Homepage home;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_envfeedback_radiologs, container,
				false);
		
		activity = (ActivityRadiologs) getActivity();
        texto = (EditText)v.findViewById(R.id.textofeedback);
        bot_finalizar= (LinearLayout) v.findViewById(R.id.botao_finalizar);

        bot_finalizar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				texto_feedback = texto.getText().toString();

                JSONObject snapshot = generateSnapshot(getActivity().getApplicationContext(), texto_feedback);

				home = new Homepage(activity);
				HomeP = home.ReadHome();
				home.TesteTipe(HomeP);
				
				Fed=true;
				Log.i(TAG, "Botao finalizar: "+ texto_feedback );
				(activity).setTextFed(texto_feedback);
				(activity).setRadiologFed(snapshot);
				(activity).setEnvFed(Fed);
					
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
 
		return v;

	}

    public JSONObject generateSnapshot(Context context, String user_feedback){

        MyApplication MyApplicationRef = (MyApplication) getActivity().getApplicationContext();
        IService iService = MyApplicationRef.getEngineServiceRef();

        JSONObject snapshot = iService.generate_radiolog(user_feedback);
        return snapshot;
    }

		
	@Override
	public void onClick(DialogInterface dialog, int which) {

	}
}
