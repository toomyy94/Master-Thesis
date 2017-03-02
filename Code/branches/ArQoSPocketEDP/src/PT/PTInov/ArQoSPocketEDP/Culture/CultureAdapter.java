package PT.PTInov.ArQoSPocketEDP.Culture;

import PT.PTInov.ArQoSPocketEDP.R;
import PT.PTInov.ArQoSPocketEDP.UI.MainActivity;
import PT.PTInov.ArQoSPocketEDP.UI.Result;
import PT.PTInov.ArQoSPocketEDP.UI.ResultList;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class CultureAdapter {

	/*
	 * 
	 * load specific windows
	 * 
	 */
	
	public static void loadMain(Context c, MainActivity mainArQoSPocketRestartKillerActivity) {
		
		TextView appStateTextRef = (TextView) mainArQoSPocketRestartKillerActivity.findViewById(R.id.testExecutionInformation);
		TextView textStateTextRef = (TextView) mainArQoSPocketRestartKillerActivity.findViewById(R.id.testStateInformation);
		//TextView textDoActionRef = (TextView) mainArQoSPocketRestartKillerActivity.findViewById(R.id.TestsScheduledText1);
		
		appStateTextRef.setText(Dictionary.getApp_state_stopped(c));
		textStateTextRef.setText(Dictionary.getApp_registe_state(c));
		//textDoActionRef.setText(Dictionary.getApp_doaction(c));
	}
	
	public static void loadListResults(Context c, ResultList v) {
		
		TextView header = (TextView) v.findViewById(R.id.MenuBarInformation);
		header.setText(Dictionary.getApp_results_header(c));
	}
	
	public static void loadMaisDetalhes(Context c, View v) {
		
		/*
		TextView MenuMoreDetails = (TextView) v.findViewById(R.id.MenuMoreDetails);
		MenuMoreDetails.setText(Dictionary.getMoreDetails(c));
		
		TextView headerOperatorName = (TextView) v.findViewById(R.id.headerOperatorName);
		headerOperatorName.setText(Dictionary.getOperatorName(c));
		
		TextView MenuRedesVizinhas = (TextView) v.findViewById(R.id.MenuRedesVizinhas);
		MenuRedesVizinhas.setText(Dictionary.getNeighboring_networks(c));
		*/
		
	}
	
	public static void loadResults(Context c, Result v) {
		
		/*
		TextView MenuBarInformation = (TextView) v.findViewById(R.id.MenuBarInformation);
		TextView MenuLocalizacao = (TextView) v.findViewById(R.id.MenuLocalizacao);
		TextView headerLatitude = (TextView) v.findViewById(R.id.headerLatitude);
		TextView headerLongitude = (TextView) v.findViewById(R.id.headerLongitude);
		TextView MenuRede = (TextView) v.findViewById(R.id.MenuRede);
		TextView headerForcaSinal = (TextView) v.findViewById(R.id.headerForcaSinal);
		TextView metricaForcaSinal = (TextView) v.findViewById(R.id.metricaForcaSinal);
		TextView headerBiterror = (TextView) v.findViewById(R.id.headerBiterror);
		TextView headerCellID = (TextView) v.findViewById(R.id.headerCellID);
		TextView headerLac = (TextView) v.findViewById(R.id.headerLac);
		TextView headerNetworkType = (TextView) v.findViewById(R.id.headerNetworkType);
		TextView MenuSIMCard = (TextView) v.findViewById(R.id.MenuSIMCard);
		TextView headerSended = (TextView) v.findViewById(R.id.headerSended);
		TextView moredetails = (TextView) v.findViewById(R.id.moredetails);
		
		
		MenuBarInformation.setText(Dictionary.getApp_results_details_header(c));
		MenuLocalizacao.setText(Dictionary.getApp_results_location_header(c));
		headerLatitude.setText(Dictionary.getApp_results_latitude(c));
		headerLongitude.setText(Dictionary.getApp_results_longitude(c));
		MenuRede.setText(Dictionary.getApp_results_Network(c));
		headerForcaSinal.setText(Dictionary.getApp_results_RSSI(c));
		metricaForcaSinal.setText(Dictionary.getApp_results_dBm(c));
		headerBiterror.setText(Dictionary.getApp_results_Bit_Error_Rate(c));
		headerCellID.setText(Dictionary.getApp_results_Celula_ID(c));
		headerLac.setText(Dictionary.getApp_results_Lac_Tac(c));
		headerNetworkType.setText(Dictionary.getApp_results_connection(c));
		MenuSIMCard.setText(Dictionary.getApp_results_details(c));
		headerSended.setText(Dictionary.getApp_results_send_to_server(c));
		moredetails.setText(Dictionary.getMoreDetails(c));
		*/
	}
}
