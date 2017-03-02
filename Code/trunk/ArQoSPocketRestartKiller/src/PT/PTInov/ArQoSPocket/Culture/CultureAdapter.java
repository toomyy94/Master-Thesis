package PT.PTInov.ArQoSPocket.Culture;

import PT.PTInov.ArQoSPocket.R;
import PT.PTInov.ArQoSPocket.UI.MainArQoSPocketRestartKillerActivity;
import PT.PTInov.ArQoSPocket.UI.Result;
import PT.PTInov.ArQoSPocket.UI.ResultList;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class CultureAdapter {

	/*
	 * 
	 * load specific windows
	 * 
	 */
	
	public static void loadMain(Context c, MainArQoSPocketRestartKillerActivity mainArQoSPocketRestartKillerActivity) {
		
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
		
		TextView MenuMedidasRadio = (TextView) v.findViewById(R.id.MenuMedidasRadio);
		TextView headerLink = (TextView) v.findViewById(R.id.headerLink);
		TextView headerREGISTED_OPERATOR_NAME_ID = (TextView) v.findViewById(R.id.headerREGISTED_OPERATOR_NAME_ID);
		TextView headerREGISTED_OPERATOR_NAME = (TextView) v.findViewById(R.id.headerREGISTED_OPERATOR_NAME);
		TextView headerNetwork_Mode = (TextView) v.findViewById(R.id.headerNetwork_Mode);
		
		TextView MenuRedesVizinhas = (TextView) v.findViewById(R.id.MenuRedesVizinhas);
		
		TextView MenuTestAcesso = (TextView) v.findViewById(R.id.MenuTestAcesso);
		TextView headerMinRTT = (TextView) v.findViewById(R.id.headerMinRTT);
		TextView headerMaxRTT = (TextView) v.findViewById(R.id.headerMaxRTT);
		TextView headerMedRTT = (TextView) v.findViewById(R.id.headerMedRTT);
		TextView headerPacketLost = (TextView) v.findViewById(R.id.headerPacketLost);
		
		TextView MenuLarguraDeBanda = (TextView) v.findViewById(R.id.MenuLarguraDeBanda);
		TextView headerDownloadDebito = (TextView) v.findViewById(R.id.headerDownloadDebito);
		TextView headerUploadDebito = (TextView) v.findViewById(R.id.headerUploadDebito);
		TextView headerMaxDownloadDebito = (TextView) v.findViewById(R.id.headerMaxDownloadDebito);
		TextView headerMaxUploadDebito = (TextView) v.findViewById(R.id.headerMaxUploadDebito);
		
		TextView MenuTestFTP = (TextView) v.findViewById(R.id.MenuTestFTP);
		TextView headerDownloadDebitoFTP = (TextView) v.findViewById(R.id.headerDownloadDebitoFTP);
		TextView headerUploadDebitoFTP = (TextView) v.findViewById(R.id.headerUploadDebitoFTP);
		
		
		MenuMedidasRadio.setText(Dictionary.getApp_medidas_radio(c));
		headerLink.setText(Dictionary.getApp_results_connection(c));
		headerREGISTED_OPERATOR_NAME_ID.setText(Dictionary.getApp_results_REGISTED_OPERATOR_NAME_ID(c));
		headerREGISTED_OPERATOR_NAME.setText(Dictionary.getApp_results_REGISTED_OPERATOR_NAME(c));
		headerNetwork_Mode.setText(Dictionary.getApp_results_Network_Mode(c));
		
		MenuRedesVizinhas.setText(Dictionary.getNeighboring_networks(c));
		
		MenuTestAcesso.setText(Dictionary.getApp_teste_acesso(c));
		headerMinRTT.setText(Dictionary.getApp_min_rtt(c));
		headerMaxRTT.setText(Dictionary.getApp_max_rtt(c));
		headerMedRTT.setText(Dictionary.getApp_med_rtt(c));
		headerPacketLost.setText(Dictionary.getApp_packet_lost(c));
		
		MenuLarguraDeBanda.setText(Dictionary.getApp_teste_banda(c));
		headerDownloadDebito.setText(Dictionary.getApp_debito_download(c));
		headerUploadDebito.setText(Dictionary.getApp_debito_upload(c));
		headerMaxDownloadDebito.setText(Dictionary.getApp_max_debito_download(c));
		headerMaxUploadDebito.setText(Dictionary.getApp_max_debito_upload(c));
		
		MenuTestFTP.setText(Dictionary.getApp_teste_ftp(c));
		headerDownloadDebitoFTP.setText(Dictionary.getApp_debito_download(c));
		headerUploadDebitoFTP.setText(Dictionary.getApp_debito_upload(c));
	}
	
	public static void loadResults(Context c, Result v) {
		
		TextView MenuBarInformation = (TextView) v.findViewById(R.id.MenuBarInformation);
		TextView MenuLocalizacao = (TextView) v.findViewById(R.id.MenuLocalizacao);
		TextView headerLatitude = (TextView) v.findViewById(R.id.headerLatitude);
		TextView headerLongitude = (TextView) v.findViewById(R.id.headerLongitude);
		
		TextView MenuDetalhes = (TextView) v.findViewById(R.id.MenuDetalhes);
		TextView headerRadioMeasurements = (TextView) v.findViewById(R.id.headerRadioMeasurements);
		TextView headerAccessTest = (TextView) v.findViewById(R.id.headerAccessTest);
		TextView headerBandwidthTest = (TextView) v.findViewById(R.id.headerBandwidthTest);
		TextView headerFTP = (TextView) v.findViewById(R.id.headerFTP);
		
		TextView headerSended = (TextView) v.findViewById(R.id.headerSended);
		TextView moredetails = (TextView) v.findViewById(R.id.moredetails);
		
		MenuBarInformation.setText(Dictionary.getApp_results_header(c));
		MenuLocalizacao.setText(Dictionary.getApp_results_location_header(c));
		headerLatitude.setText(Dictionary.getApp_results_latitude(c));
		headerLongitude.setText(Dictionary.getApp_results_longitude(c));
		MenuDetalhes.setText(Dictionary.getApp_results_details(c));
		headerRadioMeasurements.setText(Dictionary.getApp_medidas_radio(c));
		headerAccessTest.setText(Dictionary.getApp_teste_acesso(c));
		headerBandwidthTest.setText(Dictionary.getApp_teste_banda(c));
		headerFTP.setText(Dictionary.getApp_teste_ftp(c));
		
		headerSended.setText(Dictionary.getApp_results_send_to_server(c));
		moredetails.setText(Dictionary.getMoreDetails(c));
	}
}
