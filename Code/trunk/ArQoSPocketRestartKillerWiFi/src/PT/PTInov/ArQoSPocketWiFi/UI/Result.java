package PT.PTInov.ArQoSPocketWiFi.UI;

import java.util.List;

import PT.PTInov.ArQoSPocketWiFi.R;
import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;
import PT.PTInov.ArQoSPocketWiFi.Utils.ResultScanListAdapterView;
import PT.PTInov.ArQoSPocketWiFi.Utils.ResultTestListView;
import PT.PTInov.ArQoSPocketWiFi.Utils.RowDataTwoLines;
import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Result extends Activity {

	private final static String tag = "Result";
	
	public Result myRef = null;
	
	private ListAdapter adapter = null;
	private List<ScanResult> data = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
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
			StoreInformation si = null;

			try {

				// Load result information
				si = ArQoSPocketRestartKillerWiFiActivity.getAllResult().get(ResultList.indexHistory);

			} catch (Exception ex) {
				Logger.v(tag, "onCreate", LogType.Error, "Load result object :"
						+ ex.toString());
			}

			if (si != null) {

				// set header information
				TextView textNameHeader = (TextView) findViewById(R.id.logtext1);
				textNameHeader.setText(si.getuserLocationInfo());

				TextView textDateHeader = (TextView) findViewById(R.id.logtext2);
				textDateHeader.setText(si.getRegistryDateFormated());

				// set location details
				TextView latDetails = (TextView) findViewById(R.id.resultLatitude);
				latDetails.setText(si.getLatitude());

				TextView longDetails = (TextView) findViewById(R.id.resultLongitude);
				longDetails.setText(si.getLongitude());
				
				
				ImageView iv = null;
				
				// verifica se houve ou não sucesso
				if (!si.getSuccess()) {			
					//mete a seta vermelha
					iv = (ImageView) findViewById(R.id.stateImg);
					iv.setImageResource(R.drawable.notok);
					iv.setVisibility(1);
				} else {
					//mete a seta verde
					iv = (ImageView) findViewById(R.id.stateImg);
					iv.setImageResource(R.drawable.ok);
					iv.setVisibility(1);
				}
				
				
				// update result list
				data = si.getScanWifiNetWorks();
				
				if (data != null) {
					adapter = new ResultScanListAdapterView(this, R.layout.rowwifiscandata, data);
					ListView listView = (ListView) findViewById(R.id.resutlTestlist);
					listView.setAdapter(adapter);
				}

				ImageView sendedState = (ImageView) findViewById(R.id.resultSended);

				if (si.getSended()) {

					Drawable image = getResources().getDrawable(R.drawable.ok);
					sendedState.setImageDrawable(image);

				} else {

					Drawable image = getResources().getDrawable(
							R.drawable.notok);
					sendedState.setImageDrawable(image);
				}
			}
		} catch (Exception ex) {
			Logger.v(tag, "onCreate", LogType.Error, ex.toString());
		}
	}

}
