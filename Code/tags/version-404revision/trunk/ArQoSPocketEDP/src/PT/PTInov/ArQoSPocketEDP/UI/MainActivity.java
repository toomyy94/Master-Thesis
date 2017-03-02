package PT.PTInov.ArQoSPocketEDP.UI;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import PT.PTInov.ArQoSPocketEDP.R;
import PT.PTInov.ArQoSPocketEDP.Culture.CultureAdapter;
import PT.PTInov.ArQoSPocketEDP.Culture.Dictionary;
import PT.PTInov.ArQoSPocketEDP.DataStructs.DoNetworkTestResultEnum;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.DataStructs.NetworkInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowType;
import PT.PTInov.ArQoSPocketEDP.Service.DoTestE2E;
import PT.PTInov.ArQoSPocketEDP.Service.EngineServiceInterface;
import PT.PTInov.ArQoSPocketEDP.Service.SendInformationTesk;
import PT.PTInov.ArQoSPocketEDP.Utils.APNManager;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumOperator;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumTestE2EInterfaceState;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements MainActivityDoTestTaskInterfaceCallBack {

	private final static String tag = "MainActivity";
	
	private final static int minimRSSI = -95;    

	// Service reference
	private static EngineServiceInterface engineService = null;
	
	private LayoutInflater factory = null;

	public static Context myContext = null;
	public static MainActivity localRef = null;

	private static ImageView iconArQoSPocket = null;
	private static ImageView iconPTInovacao = null;
	private static boolean gotoAdminMenu = false;
	private static int touchCount = 0;

	private static ImageView doTest = null;
	
	public static Boolean blockAllActions = false;
	public static Boolean needNetwrok = false;

	/*
	 * References from main screen
	 */
	private static RelativeLayout layoutMainScreen = null;
	private static ImageView novo_sim = null;
	private static ImageView trocar_sim = null;
	private static ImageView teste_conetividade = null;
	private static ImageView historico_resultados = null;

	/*
	 * References from network information screen
	 */
	private static RelativeLayout layoutNetworkMetrics = null;
	private static ImageView layoutNetworkMetricsHeaderBar_seguinte = null;
	private static ImageView layoutNetworkMetricsHeaderBar_repetir = null;
	private static TextView layoutNetworkMetricsHeaderBar_layout_RSSI_State_Info_text = null;
	private static TextView layoutNetworkMetricsHeaderBar_layout_LAC_State_Info_text = null;
	private static TextView layoutNetworkMetricsHeaderBar_layout_CELLID_State_Info_text = null;
	private static ImageView layoutNetworkMetricsHeaderBar_layout_RSSI_State_img = null;
	private static ImageView layoutNetworkMetricsHeaderBar_layout_LAC_State_img = null;
	private static ImageView layoutNetworkMetricsHeaderBar_layout_CELLID_State_img = null;
	private static TextView layoutNetworkMetricsHeaderBar_layout_OperatorName_State_Info_text = null;
	private static TextView layoutNetworkMetricsHeaderBar_layout_NetworkType_State_Info_text = null;

	/*
	 * References from introdução da informação do modem
	 */
	private static RelativeLayout layoutInsertInformation = null;
	private static ImageView layoutInsertInformation_bottom_seguinte = null;
	private static EditText layoutInsertInformation_Body_MSISDN_editTextMSISDN = null;
	private static EditText layoutInsertInformation_Body_IP_editTextIP = null;
	private static TextView layoutInsertInformation_subHeader_text = null;

	/*
	 * References from teste E2E
	 */
	private static RelativeLayout layoutTestE2E = null;
	private static ImageView layoutTestE2E_BodyStateBottom_seguinte = null;
	private static ImageView layoutTestE2E_BodyStateBottom_repetir = null;
	private static RelativeLayout layoutTestE2E_BodyState = null;
	private static ImageView layoutTestE2E_TesteState = null;
	private static TextView layoutTestE2E_subHeader_text = null;
	private static ImageView layoutTestE2E_BodyState_InfoState_StateImg_img = null;
	private static TextView layoutTestE2E_BodyState_InfoState_Info_text = null;
	private static TextView layoutTestE2EHeaderBarText = null;

	/*
	 * References from Layout informativo
	 */
	private static RelativeLayout layoutUserInformation = null;
	private static ImageView layoutUserInformationBottom_sair = null;
	private static TextView layoutUserInformationTextInformationSubText = null;
	private static EditText layoutUserInformationTextInformationSubTextComment_EditBox = null;
	private static EditText layoutUserInformationTextInformationSubTextModeloSelo_EditBox = null;

	/*
	 * References from Layout informativo with next
	 */
	private static RelativeLayout layoutUserInformationWithNext = null;
	private static ImageView layoutUserInformationWithNextBottom_seguinte = null;
	private static TextView layoutUserInformationWithNextTextInformationSubText = null;
	
	/*
	 * References from layout introdução de informação do serial do contador
	 */
	private static RelativeLayout layoutInsertModemInformation = null;
	private static EditText layoutInsertModemInformation_Body_MSISDN_editTextSerialNumber = null;
	private static ImageView layoutInsertModemInformation_bottom_seguinte = null;
	
	
	/*
	 * Optinus menu 
	 */
	private int group1Id = 1;
	private int ReportMail = Menu.FIRST;
	
	
	private String resultDetails = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_with_workflow);

		myContext = this;
		localRef = this;
		
		// Start service
        Intent i = new Intent();
        i.setAction("PT.PTInov.ArQoSPocketEDP.Service.EngineService");
        startService(i);
        
        factory = LayoutInflater.from(this);

		Dictionary.setSystemCulture();
		Logger.v(tag, "onCreate", LogType.Trace, " My getDisplayLanguage :"
				+ Locale.getDefault().getDisplayLanguage());

		iconArQoSPocket = (ImageView) findViewById(R.id.headerImg);
		iconPTInovacao = (ImageView) findViewById(R.id.barraBaixo);

		loadReferemces();

		loadAnimations();

		loadMainScreen();

		// loadNetworkMetricsScreen("-100","406","35603");
		// loadlayoutInsertInformation();
		// loadTestE2E(EnumTestE2EInterfaceState.TERMINADO_COM_SUCESSO);
		//loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_backOffice));
		// loadlayoutUserInformationWithNext(getResources().getString(R.string.layoutUserInformationTextInformationSubText_backOffice));
		// loadModemSerialNumber();
		
		//APNManager apnManager = new APNManager(this);
		//apnManager.EnumerateAPNs();
		//int returnId = apnManager.addNewAPN("M2M APN", "internet", "26806", "268", "06");
		//apnManager.SetDefaultAPN(returnId);
		//apnManager.EnumerateAPNs();
		
	}

	private void loadMainScreen() {

		try {

			Logger.v(tag, "loadMainScreen", LogType.Trace, "In");

			// set the main screen visivel
			layoutMainScreen.setVisibility(View.VISIBLE);

			// set all other screens invisivel
			layoutNetworkMetrics.setVisibility(View.GONE);
			layoutInsertInformation.setVisibility(View.GONE);
			layoutTestE2E.setVisibility(View.GONE);
			layoutUserInformation.setVisibility(View.GONE);
			layoutUserInformationWithNext.setVisibility(View.GONE);
			layoutInsertModemInformation.setVisibility(View.GONE);

		} catch (Exception ex) {
			Logger.v(tag, "loadMainScreen", LogType.Error, ex.toString());
		}
	}
	
	private void loadModemSerialNumber() {

		try {

			Logger.v(tag, "loadMainScreen", LogType.Trace, "In");

			// set the main screen visivel
			layoutInsertModemInformation.setVisibility(View.VISIBLE);

			// set all other screens invisivel
			layoutNetworkMetrics.setVisibility(View.GONE);
			layoutInsertInformation.setVisibility(View.GONE);
			layoutTestE2E.setVisibility(View.GONE);
			layoutUserInformation.setVisibility(View.GONE);
			layoutUserInformationWithNext.setVisibility(View.GONE);
			layoutMainScreen.setVisibility(View.GONE);

		} catch (Exception ex) {
			Logger.v(tag, "loadMainScreen", LogType.Error, ex.toString());
		}
	}

	@SuppressLint("ParserError")
	private void loadNetworkMetricsScreen() {

		try {

			Logger.v(tag, "loadNetworkMetricsScreen", LogType.Trace, "In");

			// set the Networ kMetrics Screen visivel
			layoutNetworkMetrics.setVisibility(View.VISIBLE);

			// save the network information in the service
			DoNetworkTestResultEnum resultTestNetwork = engineService.doNetworkTest();
			NetworkInformationDataStruct networkInformationDataStruct = engineService.getNetworkInfoFromTest();

			if (networkInformationDataStruct != null) {

				int rssi = networkInformationDataStruct.getGsmSignalStrength();
				String lac = networkInformationDataStruct.getGsmLac();
				String cellID = networkInformationDataStruct.getGsmCellID();
				String operatorName = networkInformationDataStruct.getOperatorName();
				String rede = networkInformationDataStruct.getNetworkType();

				// load screen information from serviço
				layoutNetworkMetricsHeaderBar_layout_RSSI_State_Info_text.setText(rssi + "");
				layoutNetworkMetricsHeaderBar_layout_LAC_State_Info_text.setText(lac);
				layoutNetworkMetricsHeaderBar_layout_CELLID_State_Info_text.setText(cellID);
				layoutNetworkMetricsHeaderBar_layout_OperatorName_State_Info_text.setText(operatorName);
				layoutNetworkMetricsHeaderBar_layout_NetworkType_State_Info_text.setText(rede);
				
				// change images icons
				if (rssi>minimRSSI)
					layoutNetworkMetricsHeaderBar_layout_RSSI_State_img.setImageDrawable(getResources().getDrawable(R.drawable.ok));
				else
					layoutNetworkMetricsHeaderBar_layout_RSSI_State_img.setImageDrawable(getResources().getDrawable(R.drawable.notok));
				
				if (lac.equals("NA"))
					layoutNetworkMetricsHeaderBar_layout_LAC_State_img.setImageDrawable(getResources().getDrawable(R.drawable.notok));
				else
					layoutNetworkMetricsHeaderBar_layout_LAC_State_img.setImageDrawable(getResources().getDrawable(R.drawable.ok));
				
				if (cellID.equals("NA"))
					layoutNetworkMetricsHeaderBar_layout_CELLID_State_img.setImageDrawable(getResources().getDrawable(R.drawable.notok));
				else
					layoutNetworkMetricsHeaderBar_layout_CELLID_State_img.setImageDrawable(getResources().getDrawable(R.drawable.ok));
					
			} else {
				
				// load screen information from serviço
				layoutNetworkMetricsHeaderBar_layout_RSSI_State_Info_text.setText("NA");
				layoutNetworkMetricsHeaderBar_layout_LAC_State_Info_text.setText("NA");
				layoutNetworkMetricsHeaderBar_layout_CELLID_State_Info_text.setText("NA");
				
				// change images icons
				layoutNetworkMetricsHeaderBar_layout_RSSI_State_img.setImageDrawable(getResources().getDrawable(R.drawable.notok));
				layoutNetworkMetricsHeaderBar_layout_LAC_State_img.setImageDrawable(getResources().getDrawable(R.drawable.notok));
				layoutNetworkMetricsHeaderBar_layout_CELLID_State_img.setImageDrawable(getResources().getDrawable(R.drawable.notok));
			}

			// set all other screens invisivel
			layoutMainScreen.setVisibility(View.GONE);
			layoutInsertInformation.setVisibility(View.GONE);
			layoutTestE2E.setVisibility(View.GONE);
			layoutUserInformation.setVisibility(View.GONE);
			layoutUserInformationWithNext.setVisibility(View.GONE);
			layoutInsertModemInformation.setVisibility(View.GONE);

		} catch (Exception ex) {
			Logger.v(tag, "loadNetworkMetricsScreen", LogType.Error,
					ex.toString());
		}
	}

	private void loadlayoutInsertInformation() {
		final String method = "loadlayoutInsertInformation";

		// set the main screen visivel
		layoutInsertInformation.setVisibility(View.VISIBLE);
		
		// escreve do atual sim ou do novo sim, conforme o teste a executar no workflow
		try {
			if (engineService.firstTestDone()) {
				// text -> novo sim
				layoutInsertInformation_subHeader_text.setText(getResources().getString(R.string.layoutInsertInformation_subHeader_text));
			} else {
				// text -> atual sim
				layoutInsertInformation_subHeader_text.setText(getResources().getString(R.string.layoutInsertInformation_subHeader_text_Actual));
			}
		} catch(Exception ex) {
			//text -> novo sim
			layoutInsertInformation_subHeader_text.setText(getResources().getString(R.string.layoutInsertInformation_subHeader_text));
			Logger.v(tag, method, LogType.Error, "Não consegui verificar se o primeiro teste tinha sido feito ou nao!!");
		}

		// set all other screens invisivel
		layoutNetworkMetrics.setVisibility(View.GONE);
		layoutMainScreen.setVisibility(View.GONE);
		layoutTestE2E.setVisibility(View.GONE);
		layoutUserInformation.setVisibility(View.GONE);
		layoutUserInformationWithNext.setVisibility(View.GONE);
		layoutInsertModemInformation.setVisibility(View.GONE);
	}

	@SuppressLint({ "ParserError", "ParserError" })
	private void loadTestE2E(EnumTestE2EInterfaceState interfaceState) {
		String method = "loadTestE2E";

		Logger.v(tag, method, LogType.Trace, "In");
		
		// set the main screen visivel
		layoutTestE2E.setVisibility(View.VISIBLE);

		// Load screen information
		switch (interfaceState) {
		case EM_EXECUCAO:
			
			Logger.v(tag, method, LogType.Trace, "EM_EXECUCAO");

			layoutTestE2E_TesteState.setImageDrawable(getResources()
					.getDrawable(R.drawable.testeemexecucao));
			layoutTestE2E_subHeader_text.setText(getResources().getString(
					R.string.layoutTestE2E_subHeader_text_exec));
			layoutTestE2E_BodyState.setVisibility(View.INVISIBLE);
			layoutTestE2E_BodyStateBottom_seguinte
					.setImageDrawable(getResources().getDrawable(
							R.drawable.seguinte3));
			layoutTestE2E_BodyStateBottom_repetir
					.setImageDrawable(getResources().getDrawable(
							R.drawable.repetir3));
			layoutTestE2E_BodyStateBottom_seguinte.setEnabled(false);
			layoutTestE2E_BodyStateBottom_repetir.setEnabled(false);

			Logger.v(tag, method, LogType.Trace, "EM_EXECUCAO Out");
			
			break;
		case TERMINADO_COM_SUCESSO:
			
			Logger.v(tag, method, LogType.Trace, "TERMINADO_COM_SUCESSO");

			layoutTestE2E_TesteState.setImageDrawable(getResources()
					.getDrawable(R.drawable.completo));
			layoutTestE2E_subHeader_text.setText(getResources().getString(
					R.string.layoutTestE2E_subHeader_text));
			layoutTestE2E_BodyState.setVisibility(View.VISIBLE);
			layoutTestE2E_BodyState_InfoState_StateImg_img
					.setImageDrawable(getResources().getDrawable(R.drawable.ok));
			layoutTestE2E_BodyState_InfoState_Info_text
					.setText(getResources()
							.getString(
									R.string.layoutTestE2E_BodyState_InfoState_Info_text));
			layoutTestE2E_BodyStateBottom_seguinte
					.setImageDrawable(getResources().getDrawable(
							R.drawable.seguinte));
			layoutTestE2E_BodyStateBottom_repetir
					.setImageDrawable(getResources().getDrawable(
							R.drawable.repetir));
			layoutTestE2E_BodyStateBottom_seguinte.setEnabled(true);
			layoutTestE2E_BodyStateBottom_repetir.setEnabled(true);

			Logger.v(tag, method, LogType.Trace, "TERMINADO_COM_SUCESSO Out");
			
			break;
		case TERMINADO_SEM_SUCESSO:

			Logger.v(tag, method, LogType.Trace, "TERMINADO_SEM_SUCESSO");
			
			layoutTestE2E_TesteState.setImageDrawable(getResources()
					.getDrawable(R.drawable.completo));
			layoutTestE2E_subHeader_text.setText(getResources().getString(
					R.string.layoutTestE2E_subHeader_text));
			layoutTestE2E_BodyState.setVisibility(View.VISIBLE);
			layoutTestE2E_BodyState_InfoState_StateImg_img
					.setImageDrawable(getResources().getDrawable(
							R.drawable.notok));
			layoutTestE2E_BodyState_InfoState_Info_text
					.setText(getResources()
							.getString(
									R.string.layoutTestE2E_BodyState_InfoState_Info_text_notok));
			layoutTestE2E_BodyStateBottom_seguinte
					.setImageDrawable(getResources().getDrawable(
							R.drawable.seguinte));
			layoutTestE2E_BodyStateBottom_repetir
					.setImageDrawable(getResources().getDrawable(
							R.drawable.repetir));
			layoutTestE2E_BodyStateBottom_seguinte.setEnabled(true);
			layoutTestE2E_BodyStateBottom_repetir.setEnabled(true);

			Logger.v(tag, method, LogType.Trace, "TERMINADO_SEM_SUCESSO Out");
			
			break;
		case MODULO_INDISPONIVEL:
			
			Logger.v(tag, method, LogType.Trace, "MODULO_INDISPONIVEL");
			
			layoutTestE2E_TesteState.setImageDrawable(getResources()
					.getDrawable(R.drawable.completo));
			layoutTestE2E_subHeader_text.setText(getResources().getString(
					R.string.layoutTestE2E_subHeader_text));
			layoutTestE2E_BodyState.setVisibility(View.VISIBLE);
			layoutTestE2E_BodyState_InfoState_StateImg_img
					.setImageDrawable(getResources().getDrawable(
							R.drawable.notok));
			layoutTestE2E_BodyState_InfoState_Info_text
					.setText(getResources()
							.getString(
									R.string.modulo_indisponivel));
			layoutTestE2E_BodyStateBottom_seguinte
					.setImageDrawable(getResources().getDrawable(
							R.drawable.seguinte));
			layoutTestE2E_BodyStateBottom_repetir
					.setImageDrawable(getResources().getDrawable(
							R.drawable.repetir));
			layoutTestE2E_BodyStateBottom_seguinte.setEnabled(true);
			layoutTestE2E_BodyStateBottom_repetir.setEnabled(true);
			
			Logger.v(tag, method, LogType.Trace, "MODULO_INDISPONIVEL Out");
			
			break;
			
		case ERRO_REDE:
			
			Logger.v(tag, method, LogType.Trace, "ERRO_REDE");
			
			layoutTestE2E_TesteState.setImageDrawable(getResources()
					.getDrawable(R.drawable.completo));
			layoutTestE2E_subHeader_text.setText(getResources().getString(
					R.string.layoutTestE2E_subHeader_text));
			layoutTestE2E_BodyState.setVisibility(View.VISIBLE);
			layoutTestE2E_BodyState_InfoState_StateImg_img
					.setImageDrawable(getResources().getDrawable(
							R.drawable.notok));
			layoutTestE2E_BodyState_InfoState_Info_text
					.setText(getResources()
							.getString(
									R.string.network_error));
			layoutTestE2E_BodyStateBottom_seguinte
					.setImageDrawable(getResources().getDrawable(
							R.drawable.seguinte));
			layoutTestE2E_BodyStateBottom_repetir
					.setImageDrawable(getResources().getDrawable(
							R.drawable.repetir));
			layoutTestE2E_BodyStateBottom_seguinte.setEnabled(true);
			layoutTestE2E_BodyStateBottom_repetir.setEnabled(true);
			
			Logger.v(tag, method, LogType.Trace, "ERRO_REDE Out");
			
			break;
		case ERRO_PROBE:
			
			Logger.v(tag, method, LogType.Trace, "ERRO_PROBE");
			
			layoutTestE2E_TesteState.setImageDrawable(getResources()
					.getDrawable(R.drawable.completo));
			layoutTestE2E_subHeader_text.setText(getResources().getString(
					R.string.layoutTestE2E_subHeader_text));
			layoutTestE2E_BodyState.setVisibility(View.VISIBLE);
			layoutTestE2E_BodyState_InfoState_StateImg_img
					.setImageDrawable(getResources().getDrawable(
							R.drawable.notok));
			layoutTestE2E_BodyState_InfoState_Info_text
					.setText(getResources()
							.getString(
									R.string.probe_error));
			layoutTestE2E_BodyStateBottom_seguinte
					.setImageDrawable(getResources().getDrawable(
							R.drawable.seguinte));
			layoutTestE2E_BodyStateBottom_repetir
					.setImageDrawable(getResources().getDrawable(
							R.drawable.repetir));
			layoutTestE2E_BodyStateBottom_seguinte.setEnabled(true);
			layoutTestE2E_BodyStateBottom_repetir.setEnabled(true);
			
			Logger.v(tag, method, LogType.Trace, "ERRO_PROBE Out");
			
			break;
		}
		
		String IP = layoutInsertInformation_Body_IP_editTextIP.getText().toString();
		if (IP.equals("")) {
			// E2E_CSD
			layoutTestE2EHeaderBarText.setText(getResources().getString(R.string.TEST_E2E_CSD));
		} else {
			//E2E_IP
			layoutTestE2EHeaderBarText.setText(getResources().getString(R.string.TEST_E2E_IP));
		}
			

		// set all other screens invisivel
		layoutNetworkMetrics.setVisibility(View.GONE);
		layoutMainScreen.setVisibility(View.GONE);
		layoutInsertInformation.setVisibility(View.GONE);
		layoutUserInformation.setVisibility(View.GONE);
		layoutUserInformationWithNext.setVisibility(View.GONE);
		layoutInsertModemInformation.setVisibility(View.GONE);
	}

	private void loadlayoutUserInformation(String infoString) {

		// set the main screen visivel
		layoutUserInformation.setVisibility(View.VISIBLE);

		// Load screen information
		layoutUserInformationTextInformationSubText.setText(infoString);

		// set all other screens invisivel
		layoutNetworkMetrics.setVisibility(View.GONE);
		layoutMainScreen.setVisibility(View.GONE);
		layoutInsertInformation.setVisibility(View.GONE);
		layoutTestE2E.setVisibility(View.GONE);
		layoutUserInformationWithNext.setVisibility(View.GONE);
		layoutInsertModemInformation.setVisibility(View.GONE);
	}

	private void loadlayoutUserInformationWithNext(String infoString) {

		// set the main screen visivel
		layoutUserInformationWithNext.setVisibility(View.VISIBLE);

		// Load screen information
		layoutUserInformationWithNextTextInformationSubText.setText(infoString);

		// set all other screens invisivel
		layoutNetworkMetrics.setVisibility(View.GONE);
		layoutMainScreen.setVisibility(View.GONE);
		layoutInsertInformation.setVisibility(View.GONE);
		layoutTestE2E.setVisibility(View.GONE);
		layoutUserInformation.setVisibility(View.GONE);
		layoutInsertModemInformation.setVisibility(View.GONE);
	}
	
	public void loadAlertDialogWarningClient(String text) {
		
		Logger.v(tag, "loadAlertDialogWarningClient", LogType.Debug, "In");
		
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(localRef);
		//final View myView2 = factory.inflate(R.layout.clientwarning, (ViewGroup) getCurrentFocus() );
		final View myView2 = factory.inflate(R.layout.clientwarning, null);
		alertDialog2.setView(myView2);
		
		//final View headerView2 = (View) factory.inflate(R.layout.clientwarningcustomtitle, (ViewGroup) getCurrentFocus());
		//alertDialog2.setCustomTitle(headerView2);
		
		TextView infoText = (TextView) myView2.findViewById(R.id.headerIP);
		infoText.setText(text);
		
		alertDialog2.setNeutralButton("OK", new DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int whichButton) {}
        });
		
		final AlertDialog menuAddDialog2 = alertDialog2.show();
		
		// Remove padding from parent
		ViewGroup parent2 = (ViewGroup) myView2.getParent();
		parent2.setPadding(0, 0, 0, 0);
		
		Logger.v(tag, "loadAlertDialogWarningClient", LogType.Debug, "Out");
	}
	
	private boolean verifyTheOperatorSIMCardInPhone(EnumOperator operator) {
		
		try {
			
			EnumOperator actualOperatorOnPhone = engineService.getActualOperatorOnPhone();
			
			if (actualOperatorOnPhone == operator)
				return true;

		} catch(Exception ex) {
			Logger.v(tag, "verifyTheOperatorSIMCardInPhone", LogType.Error, ex.toString());
		}
		
		return false;
	}
	
	private void resetUserInformation() {
		layoutInsertInformation_Body_IP_editTextIP.setText("");
		layoutInsertInformation_Body_MSISDN_editTextMSISDN.setText("");
		layoutInsertModemInformation_Body_MSISDN_editTextSerialNumber.setText("");
		layoutUserInformationTextInformationSubTextComment_EditBox.setText("");
		layoutUserInformationTextInformationSubTextModeloSelo_EditBox.setText("");
	}

	@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError" })
	private void loadAnimations() {

		try {
			
			Logger.v(tag, "loadAnimations", LogType.Trace, "In");

			/*
			 * 
			 * load animations from main screen
			 */

			novo_sim.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					try {
						
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							
							// reset extra menu counter
							touchCount = 0;

							novo_sim.setImageDrawable(getResources().getDrawable(R.drawable.novo_sim2));
							
							resetUserInformation();
							
							break;
						case MotionEvent.ACTION_UP:
							
							// restart resultdetails
							resultDetails = "";

							// back to the normal img
							novo_sim.setImageDrawable(getResources()
									.getDrawable(R.drawable.novo_sim));
							
							// verificar se a aplicação está bloqueada
							if (blockAllActions) {
								loadAlertDialogWarningClient(getResources().getString(R.string.warning_access));
								return true;
							}
							
							// need network connection
							if (needNetwrok) {
								loadAlertDialogWarningClient(getResources().getString(R.string.warning_networkFail));
								return true;
							}

							// clear last workflow
							engineService.restartWorkFlow();

							// define this test in the service
							engineService.setWorkFlow(WorkFlowType.WORKFLOW_INSERT_SIM_CARD);

							// ask for modem serial number
							loadModemSerialNumber();
		

							break;
						}

					} catch (Exception ex) {
						Logger.v(tag, "loadAnimations", LogType.Error, "novo_sim_tmn.setOnTouchListener :: ERROR :" + ex.toString());
					}
					return true;
				}

			});

			trocar_sim.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					try {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							
							// reset extra menu counter
							touchCount = 0;

							trocar_sim.setImageDrawable(getResources()
									.getDrawable(R.drawable.trocar_sim2));
							
							resetUserInformation();
							
							break;
						case MotionEvent.ACTION_UP:
							
							// restart resultdetails
							resultDetails = "";

							// back to the normal img
							trocar_sim.setImageDrawable(getResources()
									.getDrawable(R.drawable.trocar_sim));
							
							// verificar se a aplicação está bloqueada
							if (blockAllActions) {
								loadAlertDialogWarningClient(getResources().getString(R.string.warning_access));
								return true;
							}
							
							// need network connection
							if (needNetwrok) {
								loadAlertDialogWarningClient(getResources().getString(R.string.warning_networkFail));
								return true;
							}

							if (verifyTheOperatorSIMCardInPhone(EnumOperator.TMN)) {
								
								// clear last workflow
								engineService.restartWorkFlow();

								// define this test in the service
								engineService.setWorkFlow(WorkFlowType.WORKFLOW_CHANGE_SIM_CARD);

								// start layout to load the serial number
								loadModemSerialNumber();
								
							} else {
								loadAlertDialogWarningClient(getResources().getString(R.string.Warning_TMN_SIM_ON_PHONE));
							}

							break;
						}
					} catch (Exception ex) {
					}
					return true;
				}
			});
			
			teste_conetividade.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					try {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							
							// reset extra menu counter
							touchCount = 0;

							teste_conetividade.setImageDrawable(getResources()
									.getDrawable(R.drawable.teste_conetividade2));
							
							resetUserInformation();
							
							break;
						case MotionEvent.ACTION_UP:
							
							// restart resultdetails
							resultDetails = "";

							// back to the normal img
							teste_conetividade.setImageDrawable(getResources()
									.getDrawable(R.drawable.teste_conetividade));

							// verificar se a aplicação está bloqueada
							if (blockAllActions) {
								loadAlertDialogWarningClient(getResources().getString(R.string.warning_access));
								return true;
							}
							
							// need network connection
							if (needNetwrok) {
								loadAlertDialogWarningClient(getResources().getString(R.string.warning_networkFail));
								return true;
							}
								
							// clear last workflow
							engineService.restartWorkFlow();

							// define this test in the service
							engineService.setWorkFlow(WorkFlowType.WORKFLOW_INSERT_TEST_CONNECTIVITY);

							// start layout to load the serial number
							loadModemSerialNumber();

							break;
						}
					} catch (Exception ex) {
					}
					return true;
				}
			});

			historico_resultados.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					try {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							
							// reset extra menu counter
							touchCount = 0;

							historico_resultados
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.historico_resultados2));
							
							resetUserInformation();
							
							break;
						case MotionEvent.ACTION_UP:
							
							// restart resultdetails
							resultDetails = "";

							historico_resultados
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.historico_resultados));

							// Start the interactive tests activity
							//startActivity(new Intent(myContext, ResultList.class));

							break;
						}
					} catch (Exception ex) {
					}
					return true;
				}
			});

			/*
			 * 
			 * load animations from network information screen
			 */

			layoutNetworkMetricsHeaderBar_seguinte
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutNetworkMetricsHeaderBar_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte2));
									break;
								case MotionEvent.ACTION_UP:

									layoutNetworkMetricsHeaderBar_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte));

									// verificar se o rssi é superior a -95
									NetworkInformationDataStruct networkInformationDataStruct = engineService.getNetworkInfoFromTest();

									if (networkInformationDataStruct != null) {

										int rssi = networkInformationDataStruct.getGsmSignalStrength();
										
										// TODO: apagar isto................................. apenas para testes.......................................
										//rssi = -92;
										
										if (rssi>minimRSSI) {
											
											// Verificar qual o cartão que tenho no tlm e pedir para colocar esse cartão no modem
											Logger.v(tag, "loadAnimations", LogType.Debug, "layoutNetworkMetricsHeaderBar_seguinte.setOnTouchListener :: OperatorName :" + networkInformationDataStruct.getOperatorName());
											
											if (verifyTheOperatorSIMCardInPhone(EnumOperator.TMN))
												loadlayoutUserInformationWithNext(getResources().getString(R.string.layoutUserInformationTextInformationSubText_TmnModem));
											else if (verifyTheOperatorSIMCardInPhone(EnumOperator.VODAFONE))
												loadlayoutUserInformationWithNext(getResources().getString(R.string.layoutUserInformationTextInformationSubText_VodafoneModem));
											else if (verifyTheOperatorSIMCardInPhone(EnumOperator.OPTIMUS))
												loadlayoutUserInformationWithNext(getResources().getString(R.string.layoutUserInformationTextInformationSubText_OptimusModem));
											
										} else {
											
											loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_OtherOperator));
										}
										
									} else {
										
										loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_OtherOperator));
									}									

									break;
								}
							} catch (Exception ex) {
							}
							return true;
						}
					});

			layoutNetworkMetricsHeaderBar_repetir
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutNetworkMetricsHeaderBar_repetir
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.repetir2));
									break;
								case MotionEvent.ACTION_UP:

									layoutNetworkMetricsHeaderBar_repetir
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.repetir));
									
									// read network information
									loadNetworkMetricsScreen();
									
									break;
								}
							} catch (Exception ex) {
							}
							return true;
						}
					});

			/*
			 * 
			 * load animations from introdução da informação do modem
			 */

			layoutInsertInformation_bottom_seguinte
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutInsertInformation_bottom_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte2));
									break;
								case MotionEvent.ACTION_UP:

									layoutInsertInformation_bottom_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte));
									
									// le os dados introduzidos pelo utilizador
									String MSISDN = layoutInsertInformation_Body_MSISDN_editTextMSISDN.getText().toString();
									String IP = layoutInsertInformation_Body_IP_editTextIP.getText().toString();
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutInsertInformation_bottom_seguinte.setOnTouchListener :: MSISDN :" + MSISDN);
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutInsertInformation_bottom_seguinte.setOnTouchListener :: IP :" + IP);
									
									// verify if the MSISDN e IP have the default value
									if (MSISDN.equals(getResources().getString(R.string.defaultMSISDN)))
										MSISDN = "";
									
									if (IP.equals(getResources().getString(R.string.defaultIP)))
										IP="";
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutInsertInformation_bottom_seguinte.setOnTouchListener :: MSISDN :" + MSISDN);
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutInsertInformation_bottom_seguinte.setOnTouchListener :: IP :" + IP);
									
									if (MSISDN.equals("")) {
										
										// call alertdialog
										loadAlertDialogWarningClient(getResources().getString(R.string.layoutInsertInformation_MSISDN));
										
									} else {
									
										// inicia a execução do teste
										Date testExecDate = new Date();
										if (engineService.firstTestDone())
											(new DoTestE2E(MSISDN, IP, testExecDate, 2, localRef, engineService)).execute(null,null,null);
										else
											(new DoTestE2E(MSISDN, IP, testExecDate, 1, localRef, engineService)).execute(null,null,null);
									
										// passa para o ecra de execução do teste
										loadTestE2E(EnumTestE2EInterfaceState.EM_EXECUCAO);
									}
									
									break;
								}
							} catch (Exception ex) {
								Logger.v(tag, "loadAnimations", LogType.Error, "layoutInsertInformation_bottom_seguinte.setOnTouchListener :: ERROR :" + ex.toString());
							}
							return true;
						}
					});

			/*
			 * load animations from teste E2E
			 */

			layoutTestE2E_BodyStateBottom_seguinte
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutTestE2E_BodyStateBottom_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte2));
									break;
								case MotionEvent.ACTION_UP:

									layoutTestE2E_BodyStateBottom_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte));
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutTestE2E_BodyStateBottom_seguinte.setOnTouchListener :: engineService.getActualWorkFlow() :" + engineService.getActualWorkFlow());
									
									// Verifica o estado do ultimo teste
									EnumTestE2EState lastTestState = engineService.getStateOfLastTest();
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "lastTestState :" + lastTestState);
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "engineService.getActualWorkFlow() :" + engineService.getActualWorkFlow());
									
									// verificar qual é o teste a ser executado
									switch(engineService.getActualWorkFlow()) {
										case WORKFLOW_CHANGE_SIM_CARD:
											
											if (engineService.secoundTestDone()) {
												
												if (lastTestState == EnumTestE2EState.DONE_WITH_SUCCESS) {
												
													// arrancar a interface a avisar que o teste terminou com sucesso
													loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_done));
												
												} else {
												
													// arrancar a interface a avisar para contactar o backoffice
													loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_backOffice));
												}
												
											} else {
												
												if (lastTestState == EnumTestE2EState.DONE_WITH_SUCCESS) {
													
													loadNetworkMetricsScreen();
												
												} else {
												
													// arrancar a interface a avisar para contactar o backoffice
													loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_backOffice));
												}
											}
											
											break;
										default:
											
											if (lastTestState == EnumTestE2EState.DONE_WITH_SUCCESS) {
											
												// arrancar a interface a avisar que o teste terminou com sucesso
												loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_done));
											
											} else {
											
												// arrancar a interface a avisar para contactar o backoffice
												loadlayoutUserInformation(getResources().getString(R.string.layoutUserInformationTextInformationSubText_backOffice));
											}
											
											break;
									}
									
									
									break;
								}
							} catch (Exception ex) {
								Logger.v(tag, "loadAnimations", LogType.Error, "layoutTestE2E_BodyStateBottom_seguinte.setOnTouchListener :: ERROR :" + ex.toString());
							}
							return true;
						}
					});

			layoutTestE2E_BodyStateBottom_repetir
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutTestE2E_BodyStateBottom_repetir
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.repetir2));
									break;
								case MotionEvent.ACTION_UP:

									layoutTestE2E_BodyStateBottom_repetir
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.repetir));
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutTestE2E_BodyStateBottom_repetir.setOnTouchListener : 1");
									
									// apaga o resultado do ultimo teste
									engineService.clearLastTest();
									
									Logger.v(tag, "loadAnimations", LogType.Debug, "layoutTestE2E_BodyStateBottom_repetir.setOnTouchListener : 2");
									
									loadlayoutInsertInformation();
									
									break;
								}
							} catch (Exception ex) {
								Logger.v(tag, "loadAnimations", LogType.Error, "layoutTestE2E_BodyStateBottom_repetir.setOnTouchListener :: ERROR :" + ex.toString());
							}
							return true;
						}
					});

			/*
			 * load animations from Layout informativo
			 */

			layoutUserInformationBottom_sair
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutUserInformationBottom_sair
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.terminar2));
									break;
								case MotionEvent.ACTION_UP:

									layoutUserInformationBottom_sair
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.terminar));
									
									// obter o comentario d tecnico
									String comment = layoutUserInformationTextInformationSubTextComment_EditBox.getText().toString();
									String Modelo_Selo = layoutUserInformationTextInformationSubTextModeloSelo_EditBox.getText().toString();
									
									// grava a informação localmente e envia para o servidor
									(new SendInformationTesk(engineService, comment, Modelo_Selo, resultDetails)).execute(null,null,null);
									
									// volta ao menu inicial
									loadMainScreen();
									
									break;
								}
							} catch (Exception ex) {
							}
							return true;
						}
					});

			/*
			 * load animations from Layout informativo with next
			 */

			layoutUserInformationWithNextBottom_seguinte
					.setOnTouchListener(new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							try {
								switch (event.getAction()) {
								case MotionEvent.ACTION_DOWN:

									layoutUserInformationWithNextBottom_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte2));
									break;
								case MotionEvent.ACTION_UP:

									layoutUserInformationWithNextBottom_seguinte
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.seguinte));
									
									// ask for modem information
									loadlayoutInsertInformation();
									
									break;
								}
							} catch (Exception ex) {
							}
							return true;
						}
					});
			
			/*
			 * load animations from layout introdução de informação do serial do contador
			 */
			
			layoutInsertModemInformation_bottom_seguinte.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					try {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:

							layoutInsertModemInformation_bottom_seguinte
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.seguinte2));
							break;
						case MotionEvent.ACTION_UP:

							layoutInsertModemInformation_bottom_seguinte
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.seguinte));
							
							// ir buscar a informação da editbox e guardar no serviço
							String modemSerialNumber = layoutInsertModemInformation_Body_MSISDN_editTextSerialNumber.getText().toString();
							
							if (modemSerialNumber.equals("")) {
								
								// call alertdialog
								loadAlertDialogWarningClient(getResources().getString(R.string.layoutInsertModemInformation_serial_number));
								
							} else {
								engineService.insertModemSerialNumber(modemSerialNumber);
							
								// verificar qual é o teste a ser executado
								switch(engineService.getActualWorkFlow()) {
									case WORKFLOW_INSERT_SIM_CARD:
										loadNetworkMetricsScreen();
										break;
									default:
										loadlayoutInsertInformation();
										break;
								}
							}
							
							break;
						}
					} catch (Exception ex) {
					}
					return true;
				}
			});
			
			/*
			layoutInsertInformation_Body_MSISDN_editTextMSISDN.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					layoutInsertInformation_Body_MSISDN_editTextMSISDN.setText("");
				}
			});
			
			layoutInsertInformation_Body_IP_editTextIP.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					layoutInsertInformation_Body_IP_editTextIP.setText("");
				}
			});
			*/
    
			/*
			layoutInsertInformation_Body_IP_editTextIP.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					try {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:

							layoutInsertInformation_Body_IP_editTextIP.setText("");
							
							break;
						}
					} catch (Exception ex) {
					}
					return true;
				}
			});*/

		} catch (Exception ex) {
		}
	}

	private void loadReferemces() {

		/*
		 * 
		 * load References from main screen
		 */

		layoutMainScreen = (RelativeLayout) findViewById(R.id.layoutMainScreen);
		novo_sim = (ImageView) findViewById(R.id.novo_sim);
		trocar_sim = (ImageView) findViewById(R.id.trocar_sim);
		teste_conetividade = (ImageView) findViewById(R.id.teste_conetividade);
		historico_resultados = (ImageView) findViewById(R.id.historico_resultados);

		/*
		 * 
		 * load References from network information screen
		 */

		layoutNetworkMetrics = (RelativeLayout) findViewById(R.id.layoutNetworkMetrics);
		layoutNetworkMetricsHeaderBar_seguinte = (ImageView) findViewById(R.id.layoutNetworkMetricsHeaderBar_seguinte);
		layoutNetworkMetricsHeaderBar_repetir = (ImageView) findViewById(R.id.layoutNetworkMetricsHeaderBar_repetir);
		layoutNetworkMetricsHeaderBar_layout_RSSI_State_Info_text = (TextView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_RSSI_State_Info_text);
		layoutNetworkMetricsHeaderBar_layout_LAC_State_Info_text = (TextView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_LAC_State_Info_text);
		layoutNetworkMetricsHeaderBar_layout_CELLID_State_Info_text = (TextView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_CELLID_State_Info_text);
		layoutNetworkMetricsHeaderBar_layout_RSSI_State_img = (ImageView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_RSSI_State_img);
		layoutNetworkMetricsHeaderBar_layout_LAC_State_img = (ImageView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_LAC_State_img);
		layoutNetworkMetricsHeaderBar_layout_CELLID_State_img = (ImageView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_CELLID_State_img);
		layoutNetworkMetricsHeaderBar_layout_OperatorName_State_Info_text = (TextView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_OperatorName_State_Info_text);
		layoutNetworkMetricsHeaderBar_layout_NetworkType_State_Info_text = (TextView) findViewById(R.id.layoutNetworkMetricsHeaderBar_layout_NetworkType_State_Info_text);

		/*
		 * References from introdução da informação do modem
		 */

		layoutInsertInformation = (RelativeLayout) findViewById(R.id.layoutInsertInformation);
		layoutInsertInformation_bottom_seguinte = (ImageView) findViewById(R.id.layoutInsertInformation_bottom_seguinte);
		layoutInsertInformation_Body_MSISDN_editTextMSISDN = (EditText) findViewById(R.id.layoutInsertInformation_Body_MSISDN_editTextMSISDN);
		layoutInsertInformation_Body_IP_editTextIP = (EditText) findViewById(R.id.layoutInsertInformation_Body_IP_editTextIP);
		layoutInsertInformation_subHeader_text = (TextView) findViewById(R.id.layoutInsertInformation_subHeader_text);

		/*
		 * References from teste E2E
		 */

		layoutTestE2E = (RelativeLayout) findViewById(R.id.layoutTestE2E);
		layoutTestE2E_TesteState = (ImageView) findViewById(R.id.layoutTestE2E_TesteState);
		layoutTestE2E_subHeader_text = (TextView) findViewById(R.id.layoutTestE2E_subHeader_text);
		layoutTestE2E_BodyState = (RelativeLayout) findViewById(R.id.layoutTestE2E_BodyState);
		layoutTestE2E_BodyState_InfoState_StateImg_img = (ImageView) findViewById(R.id.layoutTestE2E_BodyState_InfoState_StateImg_img);
		layoutTestE2E_BodyState_InfoState_Info_text = (TextView) findViewById(R.id.layoutTestE2E_BodyState_InfoState_Info_text);
		layoutTestE2E_BodyStateBottom_seguinte = (ImageView) findViewById(R.id.layoutTestE2E_BodyStateBottom_seguinte);
		layoutTestE2E_BodyStateBottom_repetir = (ImageView) findViewById(R.id.layoutTestE2E_BodyStateBottom_repetir);
		layoutTestE2EHeaderBarText = (TextView) findViewById(R.id.layoutTestE2EHeaderBarText);

		/*
		 * References from Layout informativo
		 */

		layoutUserInformation = (RelativeLayout) findViewById(R.id.layoutUserInformation);
		layoutUserInformationBottom_sair = (ImageView) findViewById(R.id.layoutUserInformationBottom_sair);
		layoutUserInformationTextInformationSubText = (TextView) findViewById(R.id.layoutUserInformationTextInformationSubText);
		layoutUserInformationTextInformationSubTextComment_EditBox = (EditText) findViewById(R.id.layoutUserInformationTextInformationSubTextComment_EditBox);
		layoutUserInformationTextInformationSubTextModeloSelo_EditBox = (EditText) findViewById(R.id.layoutUserInformationTextInformationSubTextModeloSelo_EditBox);

		/*
		 * References from Layout informativo with next
		 */
		layoutUserInformationWithNext = (RelativeLayout) findViewById(R.id.layoutUserInformationWithNext);
		layoutUserInformationWithNextBottom_seguinte = (ImageView) findViewById(R.id.layoutUserInformationWithNextBottom_seguinte);
		layoutUserInformationWithNextTextInformationSubText = (TextView) findViewById(R.id.layoutUserInformationWithNextTextInformationSubText);
		
		/*
		 * References from layout introdução de informação do serial do contador
		 */
		layoutInsertModemInformation = (RelativeLayout) findViewById(R.id.layoutInsertModemInformation);
		layoutInsertModemInformation_Body_MSISDN_editTextSerialNumber = (EditText) findViewById(R.id.layoutInsertModemInformation_Body_MSISDN_editTextSerialNumber);
		layoutInsertModemInformation_bottom_seguinte = (ImageView) findViewById(R.id.layoutInsertModemInformation_bottom_seguinte);

		/*
		 * 
		 * Menu extra
		 */

		iconArQoSPocket.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (gotoAdminMenu) {
					
					touchCount++;
					
					if (touchCount == 10) {
						Logger.v(tag, " iconArQoSPocket.setOnClickListener",
								LogType.Trace, "tou akiiiiiiiiiiiiiiiiiiiii!");

						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								myContext);
						final View myView = getLayoutInflater().inflate(
								R.layout.changeipport, null);
						alertDialog.setView(myView);
						alertDialog.setTitle("Administrador");

						// set actual ip, port
						if (engineService != null) {
							EditText ip = (EditText) myView
									.findViewById(R.id.ipAddress);
							EditText port = (EditText) myView
									.findViewById(R.id.port);
							ip.setText(engineService.getIP());
							port.setText(engineService.getPort());
						}

						alertDialog.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										try {
											EditText ip = (EditText) myView
													.findViewById(R.id.ipAddress);
											EditText port = (EditText) myView
													.findViewById(R.id.port);

											if (engineService != null) {
												engineService.setServerHost(ip
														.getText().toString(),
														port.getText()
																.toString());
											}

										} catch (Exception ex) {
											Logger.v(tag,
													"AlertDialog.Builder",
													LogType.Debug,
													ex.toString());
										}
									}
								});

						alertDialog.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										return;
									}
								});

						AlertDialog menuAddDialog = alertDialog.show();
					}
				} else {
					touchCount = 0;
				}

				// restart admin menu access
				gotoAdminMenu = false;
			}
		});

		iconPTInovacao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// restart admin menu access	
				if (!gotoAdminMenu) {
					gotoAdminMenu = true;
					touchCount++;
				} else {
					touchCount = 0;
				}
			}
		});
	}

	public static List<WorkFlowBase> getAllResult() {

		try {

			return engineService.getAllResult();

		} catch (Exception ex) {
			Logger.v(tag, "getAllResultByIndex", LogType.Trace, ex.toString());
		}

		return null;
	}

	public static void serviceAlreadyStart(EngineServiceInterface es) {

		try {

			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, "In");

			// Set the service reference, in the future we will use this
			// reference to call methods
			engineService = es;

			// limpa o workflow da ultima sessão
			engineService.restartWorkFlow();

		} catch (Exception ex) {
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, ex.toString());
		}
	}

	public void testDone(EnumTestE2EState testResultState) {       
		
		Logger.v(tag, "testDone", LogType.Trace, "testResultState :"+testResultState);
		
		switch(testResultState) {
		case DONE_WITH_SUCCESS:
			loadTestE2E(EnumTestE2EInterfaceState.TERMINADO_COM_SUCESSO);
			Logger.v(tag, "testDone", LogType.Trace, "testDone 1");
			resultDetails = " - " + getStringInterface(EnumTestE2EInterfaceState.TERMINADO_COM_SUCESSO);
			break;
		case DONE_WITHOUT_SUCCESS:
			loadTestE2E(EnumTestE2EInterfaceState.TERMINADO_SEM_SUCESSO);
			Logger.v(tag, "testDone", LogType.Trace, "testDone 2");
			resultDetails = " - " + getStringInterface(EnumTestE2EInterfaceState.TERMINADO_SEM_SUCESSO);
			break;
		case MODULE_UNAVAILABLE:
			loadTestE2E(EnumTestE2EInterfaceState.MODULO_INDISPONIVEL);
			Logger.v(tag, "testDone", LogType.Trace, "testDone 3");
			resultDetails = " - " + getStringInterface(EnumTestE2EInterfaceState.MODULO_INDISPONIVEL);
			break; 
		case NETWORK_ERROR:
			loadTestE2E(EnumTestE2EInterfaceState.ERRO_REDE);  
			Logger.v(tag, "testDone", LogType.Trace, "testDone 4");
			resultDetails = " - " + getStringInterface(EnumTestE2EInterfaceState.ERRO_REDE); 
			break;
		case PROBE_ERROR:
			loadTestE2E(EnumTestE2EInterfaceState.ERRO_PROBE);
			Logger.v(tag, "testDone", LogType.Trace, "testDone 5");
			resultDetails = " - " + getStringInterface(EnumTestE2EInterfaceState.ERRO_PROBE);
			break;
		}
		
		Logger.v(tag, "testDone", LogType.Trace, "Out - resultDetails :"+resultDetails);
	}
	
	private String getStringInterface(EnumTestE2EInterfaceState interfaceState) {
		
		switch(interfaceState) {
		case TERMINADO_COM_SUCESSO:
			return getResources().getString(R.string.layoutTestE2E_BodyState_InfoState_Info_text);
		case TERMINADO_SEM_SUCESSO:
			return getResources().getString(R.string.layoutTestE2E_BodyState_InfoState_Info_text_notok);
		case MODULO_INDISPONIVEL:
			return getResources().getString(R.string.modulo_indisponivel);
		case ERRO_REDE:
			return getResources().getString(R.string.network_error);
		case ERRO_PROBE:
			return getResources().getString(R.string.probe_error);
		}
		
		return "NA";
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(group1Id, ReportMail, ReportMail, "Configurações");

    	return super.onCreateOptionsMenu(menu); 
    }
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 
		 switch (item.getItemId()) {
		 	case 1:
		 		
		 		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						myContext);
				final View myView = getLayoutInflater().inflate(
						R.layout.changereportmail, null);
				alertDialog.setView(myView);
				alertDialog.setTitle("Configurações de gestão");

				// set actual ip, port
				if (engineService != null) {
					
					EditText mail = (EditText) myView.findViewById(R.id.mailAddress);
					mail.setText(engineService.getEmailToSend());
				}

				alertDialog.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									
									EditText mail = (EditText) myView.findViewById(R.id.mailAddress);
									engineService.setEmailToSend(mail.getText().toString());

								} catch (Exception ex) {
									Logger.v(tag,
											"AlertDialog.Builder",
											LogType.Debug,
											ex.toString());
								}
							}
						});

				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});

				AlertDialog menuAddDialog = alertDialog.show();
		 		
			 return true;
		 }
		 
		 return false;
	 }
}
