package PT.PTIN.ArQoSPocketPTWiFi.UI;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.R;
import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Store.StoreInformation;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;

public class Result extends Activity {

	private final static String tag = "Result";
	
	public Result myRef = null;
	
	public StoreInformation si = null;
	
	private LayoutInflater factory = null;
	
	private ListAdapter adapter = null;
	private List<NeighboringCellInfo> data = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
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

			try {

				// Load result information
				si = ArQoSPocketPTWiFiActivity.getAllStoreInformation().get(
						ResultList.indexHistory);

			} catch (Exception ex) {
				Logger.v(tag, "onCreate", LogType.Error, "Load result object :"
						+ ex.toString());
			}

			if (si != null) {
				
				// load shared images state
				Drawable imageOK = getResources().getDrawable(R.drawable.ok);
				Drawable imageNOTOK = getResources().getDrawable(R.drawable.notok);

				// set header information
				TextView textNameHeader = (TextView) findViewById(R.id.logtext1);
				textNameHeader.setText(si.getuserLocationInfo());

				TextView textDateHeader = (TextView) findViewById(R.id.logtext2);
				textDateHeader.setText(si.getRegistryDateFormated());
				
				
				// set image test state
				ImageView testState = (ImageView) findViewById(R.id.stateImg);
				
				if (si.getSuccess()) {
					testState.setImageDrawable(imageOK);
				} else {
					testState.setImageDrawable(imageNOTOK);
				}
				
				// set connect pt wifi network state
				ImageView connectPTWiFiState = (ImageView) findViewById(R.id.resultConnectPTWIFI);
				
				if (si.getActionResult().getConnectToPTWiFiSTATE() == ActionState.OK) {
					connectPTWiFiState.setImageDrawable(imageOK);
				} else {
					connectPTWiFiState.setImageDrawable(imageNOTOK);
				}
				
				// set auth PT WiFi state
				ImageView authPTWiFiState = (ImageView) findViewById(R.id.resultAuthPTWIFI);
				
				if (si.getActionResult().getAuthPTWiFiSTATE() == ActionState.OK) {
					authPTWiFiState.setImageDrawable(imageOK);
				} else {
					authPTWiFiState.setImageDrawable(imageNOTOK);
				}
				
				// set test ping state
				ImageView testPingstate = (ImageView) findViewById(R.id.resultPing);
				
				if (si.getActionResult().getDoPingSTATE() == ActionState.OK) {
					testPingstate.setImageDrawable(imageOK);
				} else {
					testPingstate.setImageDrawable(imageNOTOK);
				}
				
				// set test download state
				ImageView testDownloadstate = (ImageView) findViewById(R.id.resultDownload);
				
				if (si.getActionResult().getDoDownloadTestSTATE() == ActionState.OK) {
					testDownloadstate.setImageDrawable(imageOK);
				} else {
					testDownloadstate.setImageDrawable(imageNOTOK);
				}

				// set test upload state
				ImageView testUploadstate = (ImageView) findViewById(R.id.resultUpload);
				
				if (si.getActionResult().getDoDownloadTestSTATE() == ActionState.OK) {
					testUploadstate.setImageDrawable(imageOK);
				} else {
					testUploadstate.setImageDrawable(imageNOTOK);
				}
				
				// set sent results state
				ImageView sendedState = (ImageView) findViewById(R.id.resultSended);

				if (si.getSended()) {

					sendedState.setImageDrawable(imageOK);

				} else {

					sendedState.setImageDrawable(imageNOTOK);
				}
				
				TextView moreDetails = (TextView) findViewById(R.id.moredetails);
				moreDetails.setOnClickListener(new OnClickListener() {
				    public void onClick(View v) {
				    	
				    	try {
				    		
				    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(myRef);
				    		
				    		View textEntryView = (View) factory.inflate(R.layout.resultmaisdetalhes, (ViewGroup) getCurrentFocus());
				    		alertDialog.setView(textEntryView);
				    		alertDialog.setTitle("Detalhes");
				    		
				    		//View headerView = (View) factory.inflate(R.layout.headalertview, (ViewGroup) getCurrentFocus());
				    		//alertDialog.setCustomTitle(headerView);
				    		
				    		TextView associationTime = (TextView) textEntryView.findViewById(R.id.resultAssociationTime);
				    		associationTime.setText(((double)(si.getActionResult().getConnectWiFiState().getAssociationResult().getAssociateTime())/1000)+"");
				    		
				    		TextView authTime = (TextView) textEntryView.findViewById((int) R.id.resultAuthTime);
				    		authTime.setText(((double)(si.getActionResult().getPTWiFiAuthResult().getPostResponseTime())/1000)+"");
				    		
				    		TextView authAccessTime = (TextView) textEntryView.findViewById((int) R.id.resultAuthAccessTime);
				    		authAccessTime.setText(((double)(si.getActionResult().getPTWiFiAuthResult().getGetResponseTime())/1000)+"");
				    		
				    		TextView rttPing = (TextView) textEntryView.findViewById((int) R.id.resultRTT);
				    		String RTT = si.getActionResult().getPingResponse().getMedia();
				    		if (RTT.length() > 7) {
				    			RTT = RTT.substring(0, 7);
				    		}
				    		rttPing.setText(RTT);
				    		
				    		TextView packetLostPing = (TextView) textEntryView.findViewById((int) R.id.resultPacketLost);
				    		packetLostPing.setText(si.getActionResult().getPingResponse().getPercentPacketLost()+"");
				    		
				    		TextView debitoDownload = (TextView) textEntryView.findViewById((int) R.id.resultDownloadDebito);
				    		NumberFormat formatter = new DecimalFormat("#.##");
				    		debitoDownload.setText(formatter.format(si.getActionResult().getHttpDownloadResponse().getDebito())+"");
				    		
				    		TextView debitoUpload = (TextView) textEntryView.findViewById((int) R.id.resultUploadDebito);
				    		debitoUpload.setText(formatter.format(si.getActionResult().getHttpUploadResponse().getDebito())+"");
				    		
				    		
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
			}
		} catch (Exception ex) {
			Logger.v(tag, "onCreate", LogType.Error, ex.toString());
		}
	}

}
