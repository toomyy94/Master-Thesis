package pt.ptinovacao.arqospocket.fragments;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import pt.ptinovacao.arqospocket.util.MenuOption;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentEnvfeedback_Anomaly extends Fragment implements OnClickListener{
	private static final String TAG = "Envfeedback_Anomaly";
	int  selectTip,posselected;
	TextView textlogoFed;
	ImageView logoFed;
	EditText texto;
	LinearLayout bot_finalizar;
	String textoStr;
	ActivityAnomalias activity;
	boolean Fed=false;
	MenuOption HomeP;
	Homepage home;
	public static final Integer[] Voz = { R.string.anomaly_type_dropped_call,
			R.string.anomaly_type_call_not_established, R.string.anomaly_type_lost_call, R.string.anomaly_type_bad_call_audio,
			R.string.anomaly_type_another_anomaly_type};

	public static final Integer[] Internet = { R.string.anomaly_type_no_data_access,
			R.string.anomaly_type_intermittent_access, R.string.anomaly_type_slow_connection, R.string.anomaly_type_another_anomaly_type};

	public static final Integer[] Messaging = { R.string.anomaly_type_message_not_sent,
			R.string.anomaly_type_message_not_received, R.string.anomaly_type_message_slow_dispatch, R.string.anomaly_type_message_delayed_recepetion,
			R.string.anomaly_type_another_anomaly_type};

	public static final Integer[] Cobertura = { R.string.anomaly_type_no_indoor_coverage,
			R.string.anomaly_type_no_outdoor_coverage};

	public static final Integer[] Outra = { R.string.anomaly_type_another_anomaly_type};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_envfeedback_anomaly, container,
				false);

		textlogoFed = (TextView) v.findViewById(R.id.textFed);
		logoFed = (ImageView) v.findViewById(R.id.imageFed);
		posselected = ((I_OnDataPass) getActivity()).getPositionSelected();
		selectTip = ((I_OnDataPass) getActivity()).getSelectTip();
		bot_finalizar = (LinearLayout) v.findViewById(R.id.botao_finalizar);
		texto = (EditText)v.findViewById(R.id.textofeedback);
		LogoImg_Name(posselected, selectTip);
		
		activity = (ActivityAnomalias)getActivity();
		
	
		bot_finalizar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				textoStr=texto.getText().toString();
					//alterar a partir do resultado do config
							
				FragmentIdtlocal.Localizacao();
//				Intent intent = new Intent(getActivity(), ActivityDashboardMain.class);
//					startActivity(intent);
				home = new Homepage (activity);
				HomeP=home.ReadHome();
				home.TesteTipe(HomeP);
				
					Fed=true;
					Log.i(TAG, "Botao finalizar: "+ textoStr );
					((I_OnDataPass) activity).setTextFed(textoStr);
					((I_OnDataPass) activity).setEnvFed(Fed);
					
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

	public void LogoImg_Name(int position, int name) {
		switch (position) {
		case 0:
			logoFed.setImageResource(R.drawable.icon_menu_voz);
			textlogoFed.setText(Voz[name]);
			break;
		case 1:
			logoFed.setImageResource(R.drawable.icon_menu_internet);

			textlogoFed.setText(Internet[name]);

			break;
		case 2:
			logoFed.setImageResource(R.drawable.icon_menu_messaging);

			textlogoFed.setText(Messaging[name]);

			break;
		case 3:
			logoFed.setImageResource(R.drawable.icon_menu_cobertura);

			textlogoFed.setText(Cobertura[name]);

			break;
		case 4:
			logoFed.setImageResource(R.drawable.icon_menu_outra);

			textlogoFed.setText(Outra[name]);

			break;
		default:

			break;			
		}
	}
		
	@Override
	public void onClick(DialogInterface dialog, int which) {

	}	
}
