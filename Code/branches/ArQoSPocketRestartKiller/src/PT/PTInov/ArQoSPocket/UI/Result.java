package PT.PTInov.ArQoSPocket.UI;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import PT.PTInov.ArQoSPocket.R;
import PT.PTInov.ArQoSPocket.Culture.CultureAdapter;
import PT.PTInov.ArQoSPocket.Culture.Dictionary;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.Utils.ResultCellScanListAdapter;
import PT.PTInov.ArQoSPocket.Utils.StoreAllTestInformation;
import PT.PTInov.ArQoSPocket.Utils.StoreInformation;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Result extends Activity {

	private final static String tag = "Result";
	
	public Result myRef = null;
	
	//public StoreInformation si = null;
	public StoreAllTestInformation sti = null;
	
	private LayoutInflater factory = null;
	
	private ListAdapter adapter = null;
	private List<NeighboringCellInfo> data = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		CultureAdapter.loadResults(this, this);
		
		factory = LayoutInflater.from(this);
		
		myRef = this;

		// Set back event
		ImageView backButton = (ImageView) findViewById(R.id.backImg);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// Start the interactive tests activity
				myRef.finish();
			}
		});

		try {

			// Load result information
			sti = MainArQoSPocketRestartKillerActivity.getAllResult().get(ResultList.indexHistory);
			
			// load shared images state
			Drawable imageOK = getResources().getDrawable(R.drawable.ok);
			Drawable imageNOTOK = getResources().getDrawable(R.drawable.notok);
			
			// set header information
			TextView textNameHeader = (TextView) findViewById(R.id.logtext1);
			textNameHeader.setText(sti.getStoreInformation().getFormatedLocationInfo());
			
			TextView textDateHeader = (TextView) findViewById(R.id.logtext2);
			textDateHeader.setText(sti.getStoreInformation().getRegistryDateFormated());
			
			// set image test state
			ImageView testState = (ImageView) findViewById(R.id.stateImg);
			
			if (sti.allTestState()) {
				testState.setImageDrawable(imageOK);
			} else {
				testState.setImageDrawable(imageNOTOK);
			}
			
			// Set location information
			TextView resultLatitude = (TextView) findViewById(R.id.resultLatitude);
			resultLatitude.setText(sti.getStoreInformation().getLatitude());
			
			TextView resultLongitude = (TextView) findViewById(R.id.resultLongitude);
			resultLongitude.setText(sti.getStoreInformation().getLongitude());
			
			// set radio information state
			ImageView resultRadioMeasurements = (ImageView) findViewById(R.id.resultRadioMeasurements);
			if (sti.getNetworkInformationState()) {
				resultRadioMeasurements.setImageDrawable(imageOK);
			} else {
				resultRadioMeasurements.setImageDrawable(imageNOTOK);
			}
			
			// set access test state
			ImageView resultAccessTest = (ImageView) findViewById(R.id.resultAccessTest);
			if (sti.getPingResponseState()) {
				resultAccessTest.setImageDrawable(imageOK);
			} else {
				resultAccessTest.setImageDrawable(imageNOTOK);
			}
			
			// set Bandwidth test state
			ImageView resultBandwidthTest = (ImageView) findViewById(R.id.resultBandwidthTest);
			if (sti.gethttpInformationState()) {
				resultBandwidthTest.setImageDrawable(imageOK);
			} else {
				resultBandwidthTest.setImageDrawable(imageNOTOK);
			}
			
			// set FTP test state
			ImageView resultFTP = (ImageView) findViewById(R.id.resultFTP);
			if (sti.getFTPInformationState()) {
				resultFTP.setImageDrawable(imageOK);
			} else {
				resultFTP.setImageDrawable(imageNOTOK);
			}
			
			Logger.v(tag, "AlertDialog", LogType.Trace, "sti.getFTPDownloadResponse().toString :"+sti.getFTPDownloadResponse().toString());
			Logger.v(tag, "AlertDialog", LogType.Trace, "sti.getFTPUploadResponse().toString :"+sti.getFTPUploadResponse().toString());
			
			// set sent state
			ImageView resultSended = (ImageView) findViewById(R.id.resultSended);
			if (sti.getSentResultState()) {
				resultSended.setImageDrawable(imageOK);
			} else {
				resultSended.setImageDrawable(imageNOTOK);
			}
			
			TextView moreDetails = (TextView) findViewById(R.id.moredetails);
			moreDetails.setOnClickListener(new OnClickListener() {
			    public void onClick(View v) {
			    	
			    	try {
			    		
			    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(myRef);
			    		
			    		View textEntryView = (View) factory.inflate(R.layout.resultmaisdetalhes, (ViewGroup) getCurrentFocus());
			    		alertDialog.setView(textEntryView);
			    		alertDialog.setTitle(Dictionary.getApp_results_details(myRef));
			    		
			    		CultureAdapter.loadMaisDetalhes(myRef, textEntryView);
			    		
			    		//View headerView = (View) factory.inflate(R.layout.headalertview, (ViewGroup) getCurrentFocus());
			    		//alertDialog.setCustomTitle(headerView);
			    		
						try {
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "1");
							
							// set radio metrics
							TextView resultRSSI = (TextView) textEntryView.findViewById(R.id.resultRSSI);
							resultRSSI.setText(sti.getStoreInformation().get_RXL()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "2");
							
							TextView resultRSRP = (TextView) textEntryView.findViewById(R.id.resultRSRP);
							resultRSRP.setText(sti.getStoreInformation().get_RSRP()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "3");

							TextView resultCID = (TextView) textEntryView.findViewById(R.id.resultCID);
							resultCID.setText(sti.getStoreInformation().get_CID()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "4");
							
							//TextView resultLCID = (TextView) textEntryView.findViewById(R.id.resultLCID);
							//resultLCID.setText(sti.getStoreInformation().get_LCID()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "5");

							TextView resultLac = (TextView) textEntryView.findViewById(R.id.resultLac);
							resultLac.setText(sti.getStoreInformation().get_LAC()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "6");

							TextView resultLink = (TextView) textEntryView.findViewById(R.id.resultLink);
							resultLink.setText(sti.getStoreInformation().getNetworkType()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "7");
							
							TextView resulECIO = (TextView) textEntryView.findViewById(R.id.resulECIO);
							resulECIO.setText(sti.getStoreInformation().get_EVDO_ECIO()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "8");
							
							TextView resulSNR = (TextView) textEntryView.findViewById(R.id.resulSNR);
							resulSNR.setText(sti.getStoreInformation().get_EVDO_SNR()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "9");
							
							TextView resultBER = (TextView) textEntryView.findViewById(R.id.resultBER);
							resultBER.setText(sti.getStoreInformation().get_BER()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "10");
							
							TextView resultPSC = (TextView) textEntryView.findViewById(R.id.resultPSC);
							resultPSC.setText(sti.getStoreInformation().get_PSC()+"");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "11");
							
							TextView resultNetwork_Mode = (TextView) textEntryView.findViewById(R.id.resultNetwork_Mode);
							resultNetwork_Mode.setText(sti.getStoreInformation().get_NETWORK_MANUAL_SELECTION_MODE()?"Manual":"Auto");
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "12");
							
							TextView headerREGISTED_OPERATOR_NAME = (TextView) textEntryView.findViewById(R.id.resultREGISTED_OPERATOR_NAME);
							headerREGISTED_OPERATOR_NAME.setText(sti.getStoreInformation().get_SHORT_REGISTED_OPERATOR_NAME());
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "13");
							
							TextView headerREGISTED_OPERATOR_NAME_ID = (TextView) textEntryView.findViewById(R.id.resultREGISTED_OPERATOR_NAME_ID);
							headerREGISTED_OPERATOR_NAME_ID.setText(sti.getStoreInformation().get_OPERATOR_NUMERIC_ID());
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "14");
							
							TextView resultRoaming = (TextView) textEntryView.findViewById(R.id.resultRoaming);
							resultRoaming.setText(sti.getStoreInformation().getRoaming());
							
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Trace, "15");
							
							
						} catch (Exception ex) {
							Logger.v(tag, "AlertDialog - radio metrics", LogType.Error, ex.toString());
						}
						
						try {
							
							
							// set Neighboring Cells
							List<NeighboringCellInfo> list = sti.getStoreInformation().getNeighboringCellList();
							
							if (list != null) {
								
								TextView MenuRedesVizinhas = (TextView) textEntryView.findViewById(R.id.MenuRedesVizinhas);
								MenuRedesVizinhas.setVisibility(View.GONE);
								
								ImageView limitLinelistTop = (ImageView) textEntryView.findViewById(R.id.limitLinelistTop);
								limitLinelistTop.setVisibility(View.GONE);
								
								ListView redesVizinhaslist = (ListView) textEntryView.findViewById(R.id.redesVizinhaslist);
								redesVizinhaslist.setVisibility(View.GONE);
								
								ImageView limitLinelistBottom = (ImageView) textEntryView.findViewById(R.id.limitLinelistBottom);
								limitLinelistBottom.setVisibility(View.GONE);
								
								TextView MenuTestAcesso = (TextView) textEntryView.findViewById(R.id.MenuTestAcesso);
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.BELOW, R.id.headerRoaming);
								MenuTestAcesso.setLayoutParams(params);
								
							} else {
								
								if (list.size() == 0) {
									
									TextView MenuRedesVizinhas = (TextView) textEntryView.findViewById(R.id.MenuRedesVizinhas);
									MenuRedesVizinhas.setVisibility(View.GONE);
									
									ImageView limitLinelistTop = (ImageView) textEntryView.findViewById(R.id.limitLinelistTop);
									limitLinelistTop.setVisibility(View.GONE);
									
									ListView redesVizinhaslist = (ListView) textEntryView.findViewById(R.id.redesVizinhaslist);
									redesVizinhaslist.setVisibility(View.GONE);
									
									ImageView limitLinelistBottom = (ImageView) textEntryView.findViewById(R.id.limitLinelistBottom);
									limitLinelistBottom.setVisibility(View.GONE);
									
									TextView MenuTestAcesso = (TextView) textEntryView.findViewById(R.id.MenuTestAcesso);
									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
									params.addRule(RelativeLayout.BELOW, R.id.headerRoaming);
									MenuTestAcesso.setLayoutParams(params);
									
								} else {
									
									// TODO: adicionar aqui o codigo de mostrar as redes vizinhas
									adapter = new ResultCellScanListAdapter(myRef, R.layout.rowcellscandata, data);
									ListView listView = (ListView) textEntryView.findViewById(R.id.redesVizinhaslist);
									listView.setAdapter(adapter);
								}
								
							}
							
							
						} catch (Exception ex) {
							Logger.v(tag, "AlertDialog - Neighboring Cells", LogType.Error, ex.toString());
						}
						
						try {
							
							// set access test information
							TextView resultMinRTT = (TextView) textEntryView.findViewById(R.id.resultMinRTT);
							resultMinRTT.setText(rttFormat(sti.getPingResponse().getMin()));
							
							TextView resultMaxRTT = (TextView) textEntryView.findViewById(R.id.resultMaxRTT);
							resultMaxRTT.setText(rttFormat(sti.getPingResponse().getMax()));
							
							TextView resultMedRTT = (TextView) textEntryView.findViewById(R.id.resultMedRTT);
							resultMedRTT.setText(rttFormat(sti.getPingResponse().getMedia()));
							
							TextView resultPacketLost = (TextView) textEntryView.findViewById(R.id.resultPacketLost);
							resultPacketLost.setText(sti.getPingResponse().getPercentPacketLost());
							
						} catch (Exception ex) {
							Logger.v(tag, "AlertDialog - access test", LogType.Error, ex.toString());
						}
						
						try {
							
							// set HTTP download/upload information
							NumberFormat formatter = new DecimalFormat("#.##");
							
							TextView resultDownloadDebito = (TextView) textEntryView.findViewById(R.id.resultDownloadDebito);
							double downloadDebito = sti.getHttpDownloadServiceResponse().getDebito()/1000;
							resultDownloadDebito.setText(formatter.format(downloadDebito)+"");
							
							TextView resultUploadDebito = (TextView) textEntryView.findViewById(R.id.resultUploadDebito);
							double uploadDebito = sti.getHttpUploadServiceResponse().getDebito()/1000;
							resultUploadDebito.setText(formatter.format(uploadDebito)+"");
							
							
							TextView resultMaxDownloadDebito = (TextView) textEntryView.findViewById(R.id.resultMaxDownloadDebito);
							double maxDownloadDebito = sti.getHttpMaxDownloadServiceResponse().getDebito()/1000;
							resultMaxDownloadDebito.setText(formatter.format(downloadDebito>maxDownloadDebito?downloadDebito:maxDownloadDebito)+"");
							
							TextView resultMaxUploadDebito = (TextView) textEntryView.findViewById(R.id.resultMaxUploadDebito);
							double maxUploadDebito = sti.getHttpMaxUploadServiceResponse().getDebito()/1000;
							resultMaxUploadDebito.setText(formatter.format(uploadDebito>maxUploadDebito?uploadDebito:maxUploadDebito)+"");
							
						} catch (Exception ex) {
							Logger.v(tag, "AlertDialog - download/upload", LogType.Error, ex.toString());
						}
						
						try {
							
							// set FTP download/upload information
							NumberFormat formatter = new DecimalFormat("#.##");
							
							TextView resultDownloadDebitoFTP = (TextView) textEntryView.findViewById(R.id.resultDownloadDebitoFTP);
							resultDownloadDebitoFTP.setText(formatter.format(sti.getFTPDownloadResponse().getDebito()/1000)+"");
							
							TextView resultUploadDebitoFTP = (TextView) textEntryView.findViewById(R.id.resultUploadDebitoFTP);
							resultUploadDebitoFTP.setText(formatter.format(sti.getFTPUploadResponse().getDebito()/1000)+"");
							
						} catch (Exception ex) {
							Logger.v(tag, "AlertDialog - download/upload", LogType.Error, ex.toString());
						}
			    		
			    		alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int which) {
			    	 
			    	       //here you can add functions
			    	 
			    			}	 });
			    	
			    		alertDialog.show();
			    		
			    	
			    	} catch(Exception ex) {
			    		Logger.v(tag, "AlertDialog", LogType.Error, ex.toString());
			    	}
			    }
			});
			
			
		} catch (Exception ex) {
			Logger.v(tag, "onCreate", LogType.Error, ex.toString());
		}
	}

	private String rttFormat(String RTT)
	{
		if (RTT.length() > 7) {
			RTT = RTT.substring(0, 7);
		}
		
		return RTT;
	}
}
