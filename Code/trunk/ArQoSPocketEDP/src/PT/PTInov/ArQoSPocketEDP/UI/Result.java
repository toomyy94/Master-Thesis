package PT.PTInov.ArQoSPocketEDP.UI;

import java.util.ArrayList;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.R;
import PT.PTInov.ArQoSPocketEDP.Culture.CultureAdapter;
import PT.PTInov.ArQoSPocketEDP.Culture.Dictionary;
import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.MoreDetailsListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
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
	
	public WorkFlowBase si = null;
	
	private LayoutInflater factory = null;
	
	private ListAdapter adapter = null;
	private List<WorkFlowBase> data = null;
	
	public static AlertDialog menuAddDialog = null;
	
	private static boolean detailsOpened = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		CultureAdapter.loadResults(this, this);
		
		factory = LayoutInflater.from(this);
		
		myRef = this;
		
		detailsOpened = false;

		/*
		try {
			
			// Set back event
			final ImageView backButton = (ImageView) findViewById(R.id.backImg);
			backButton.setOnTouchListener( new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					
					Logger.v(tag, "backButton.setOnTouchListener", LogType.Debug, "event :" + event);

					switch(event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							backButton.setImageDrawable(myRef.getResources().getDrawable(R.drawable.voltarpressed));
							break;
						case MotionEvent.ACTION_UP:
							backButton.setImageDrawable(myRef.getResources().getDrawable(R.drawable.back));
							myRef.finish();
							break;
					}
					
					return true;
				}
				
			});

			try {

				// Load result information
				si = MainActivity.getAllResult().get(
						ResultList.indexHistory);

			} catch (Exception ex) {
				Logger.v(tag, "onCreate", LogType.Error, "Load result object :"
						+ ex.toString());
			}
			
			// Set mais detalhes event
			final ImageView moreDetailsButton = (ImageView) findViewById(R.id.botaoMaisInfo);
			moreDetailsButton.setOnTouchListener( new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					
					Logger.v(tag, "moreDetailsButton.setOnTouchListener", LogType.Debug, "event :" + event);

					switch(event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							moreDetailsButton.setImageDrawable(myRef.getResources().getDrawable(R.drawable.maisinformacaopressed));
							break;
						case MotionEvent.ACTION_UP:
							moreDetailsButton.setImageDrawable(myRef.getResources().getDrawable(R.drawable.maisinformacao));
							
							if (detailsOpened == false) {
								
								detailsOpened = true;
								
								// start the alertdialog that show all the information
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(myRef);
								final View myView = factory.inflate(R.layout.resultmaisdetalhes, (ViewGroup) getCurrentFocus() );
								alertDialog.setView(myView);
				    		
								final View headerView = (View) factory.inflate(R.layout.moreinfocustomtitle, (ViewGroup) getCurrentFocus());
								alertDialog.setCustomTitle(headerView);
				    		
								data = new ArrayList<StoreInformation>();
								data.add(si);
				    		
								adapter = new MoreDetailsListAdapter(myRef, R.layout.rowdatamoreinfo, data);
								ListView listView = (ListView) myView.findViewById(R.id.resutlTestlist);
								listView.setAdapter(adapter);
							
								menuAddDialog = alertDialog.show();
							
								// Remove padding from parent
								ViewGroup parent = (ViewGroup) myView.getParent();
								parent.setPadding(0, 0, 0, 0);
								
								menuAddDialog.setOnCancelListener(new OnCancelListener() {

									public void onCancel(DialogInterface dialog) {
										detailsOpened = false;
									}
									
								});
							}
	
							break;
					}
					
					return true;
				}
				
			});
			

			if (si != null) {

				// set header information
				TextView textNameHeader = (TextView) findViewById(R.id.logtext1);
				textNameHeader.setText(si.getuserLocationInfo());

				TextView textDateHeader = (TextView) findViewById(R.id.logtext2);
				textDateHeader.setText(si.getRegistryDateFormated());
				
				// set more details
				ImageView sendedState = (ImageView) findViewById(R.id.imgTestState);
				if (si.getSended()) {

					Drawable image = getResources().getDrawable(R.drawable.ok);
					sendedState.setImageDrawable(image);

				} else {

					Drawable image = getResources().getDrawable(
							R.drawable.notok);
					sendedState.setImageDrawable(image);
				}
				
				// set more details
				ImageView imgTestStateLocation = (ImageView) findViewById(R.id.imgTestStateLocation);
				if (si.isHasLocation()) {

					Drawable image = getResources().getDrawable(R.drawable.ok);
					imgTestStateLocation.setImageDrawable(image);

				} else {

					Drawable image = getResources().getDrawable(
							R.drawable.notok);
					imgTestStateLocation.setImageDrawable(image);
				}
				
				// set more details
				ImageView imgTestStateMobileConnection = (ImageView) findViewById(R.id.imgTestStateMobileConnection);
				if (si.isHasMobileInfo()) {

					Drawable image = getResources().getDrawable(R.drawable.ok);
					imgTestStateMobileConnection.setImageDrawable(image);

				} else {

					Drawable image = getResources().getDrawable(
							R.drawable.notok);
					imgTestStateMobileConnection.setImageDrawable(image);
				}
				
				// set more details
				ImageView imgTestStateTestCall = (ImageView) findViewById(R.id.imgTestStateTestCall);
				if (si.isTestCallDone()) {

					Drawable image = getResources().getDrawable(R.drawable.ok);
					imgTestStateTestCall.setImageDrawable(image);

				} else {

					Drawable image = getResources().getDrawable(
							R.drawable.notok);
					imgTestStateTestCall.setImageDrawable(image);
				}
				
				// set more details
				ImageView imgTestStateTestSMS = (ImageView) findViewById(R.id.imgTestStateTestSMS);
				if (si.isTestSMSDone()) {

					Drawable image = getResources().getDrawable(R.drawable.ok);
					imgTestStateTestSMS.setImageDrawable(image);

				} else {

					Drawable image = getResources().getDrawable(
							R.drawable.notok);
					imgTestStateTestSMS.setImageDrawable(image);
				}

				
			}
			
			
		} catch (Exception ex) {
			Logger.v(tag, "onCreate", LogType.Error, ex.toString());
		}
		
		*/
	}

}
