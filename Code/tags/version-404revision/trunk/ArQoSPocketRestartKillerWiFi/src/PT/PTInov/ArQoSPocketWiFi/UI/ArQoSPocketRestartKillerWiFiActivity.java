package PT.PTInov.ArQoSPocketWiFi.UI;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketWiFi.R;
import PT.PTInov.ArQoSPocketWiFi.Service.EngineServiceInterface;
import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;
import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;
import PT.PTInov.ArQoSPocketWiFi.Utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class ArQoSPocketRestartKillerWiFiActivity extends Activity {

	private final static String tag = "ArQoSPocketRestartKillerWiFiActivity";

	private static final String STRING_EXECUTION = "Em execução";
	private static final String STRING_STOPPED = "Medição Parada";

	private static final String SUCCESS_EXECUTION = "Medição com Sucesso";
	private static final String FAIL_EXECUTION = "Medição sem Sucesso";

	// Service reference
	private static EngineServiceInterface engineService = null;

	private Spinner mySpinner = null;

	public static Context myContect = null;

	private boolean click = false;

	private static ImageView appStateImageRef = null;
	private static TextView appStateTextRef = null;

	private static ImageView testStateImageRef = null;
	private static TextView textStateTextRef = null;
	private static TextView textDateTextRef = null;
	
	private static ImageView iconArQoSPocket = null;
	private static ImageView iconPTInovacao = null;
	private static boolean gotoAdminMenu = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Load UI objects references
		appStateImageRef = (ImageView) findViewById(R.id.stateImg);
		appStateTextRef = (TextView) findViewById(R.id.testExecutionInformation);

		testStateImageRef = (ImageView) findViewById(R.id.iconTestState);
		textStateTextRef = (TextView) findViewById(R.id.testStateInformation);
		textDateTextRef = (TextView) findViewById(R.id.testTimeDateInformation);
		
		iconArQoSPocket = (ImageView) findViewById(R.id.headerImg);
		iconPTInovacao = (ImageView) findViewById(R.id.logoPTn);
		
		// Start service
        Intent i = new Intent();
        i.setAction("PT.PTInov.ArQoSPocketWiFi.Service.EngineService");
        startService(i);

		myContect = this;

		mySpinner = (Spinner) findViewById(R.id.mySpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.places_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner.setAdapter(adapter);
		mySpinner.setPrompt(getString(R.string.local));
		
		ImageView doTest = (ImageView) findViewById(R.id.TestsScheduledImg);
        doTest.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	
		    	// change head test information
		    	// change the icon
				Drawable image2 = getResources().getDrawable(R.drawable.testeemexecucao);
				appStateImageRef.setImageDrawable(image2);
				appStateTextRef.setText(STRING_EXECUTION);
		    	
		    	mySpinner.performClick();
		    	
		    	click = true;
		    	
		    	//restart admin menu access
		    	gotoAdminMenu = false;
		    }
		});
        
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if (click) {
					Logger.v(tag, "onItemSelected", LogType.Trace, "Append to list!");
				
					String selectedLocal = (String) mySpinner.getSelectedItem();	
					Date testExecDate = new Date();
					Boolean result = engineService.saveActualInformation(selectedLocal, testExecDate);
					
					// test if the information are saved with success
					if (result) {
						
						Drawable image = getResources().getDrawable(R.drawable.ok);
						testStateImageRef.setImageDrawable(image);
						
						textStateTextRef.setText(SUCCESS_EXECUTION);
						textDateTextRef.setText(Utils.buildDateOfNextTest(testExecDate));

					} else {
						
						Drawable image = getResources().getDrawable(R.drawable.notok);
						testStateImageRef.setImageDrawable(image);
						
						textStateTextRef.setText(FAIL_EXECUTION);
						textDateTextRef.setText(Utils.buildDateOfNextTest(testExecDate));
					}
										
					click = false;
					
					// change head test information
					Drawable image4 = getResources().getDrawable(R.drawable.agendamentoparado);
					appStateImageRef.setImageDrawable(image4);
					appStateTextRef.setText(STRING_STOPPED);
					
					//restart admin menu access
					gotoAdminMenu = false;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
    		
		});
        
        ImageView resultbar = (ImageView) findViewById(R.id.goToResults);
        resultbar.setOnClickListener(new OnClickListener() {
        	
		    public void onClick(View v) {
		    	
		        // Start the interactive tests activity
		    	startActivity(new Intent(myContect, ResultList.class));
		    	
		    	//restart admin menu access
		    	gotoAdminMenu = false;
		    }
		});
        
        iconArQoSPocket.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	
		    	if (gotoAdminMenu) {
		    		Logger.v(tag, " iconArQoSPocket.setOnClickListener", LogType.Trace, "tou akiiiiiiiiiiiiiiiiiiiii!");
		    		
		    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContect);
        			final View myView = getLayoutInflater().inflate(R.layout.changeipport, null );  
		    		alertDialog.setView(myView);
		    		alertDialog.setTitle("Administrador");
		    		
		    		// set actual ip, port
		    		if (engineService != null) {
		    			EditText ip = (EditText) myView.findViewById(R.id.ipAddress);
		    			EditText port = (EditText) myView.findViewById(R.id.port);
		    			ip.setText(engineService.getIP());
		    			port.setText(engineService.getPort());
		    		}
		    		
		    		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
		    			public void onClick(DialogInterface dialog, int whichButton) {
		    				try {
		    		    		EditText ip = (EditText) myView.findViewById(R.id.ipAddress);
		    		    		EditText port = (EditText) myView.findViewById(R.id.port);
		    		    		
		    		    		if (engineService != null) {
		    		    			engineService.setServerHost(ip.getText().toString(), port.getText().toString());
		    		    		}
		    		    		
		    		    	} catch(Exception ex) {
		    		    		Logger.v(tag, "AlertDialog.Builder", LogType.Debug, ex.toString());
		    		    	}
		    			}  
		    		});  

		    		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

		    		        public void onClick(DialogInterface dialog, int which) {
		    		            return;   
		    		        }
		    		    });
		    		
		    		AlertDialog menuAddDialog = alertDialog.show();
		    	}
		    	
		    	//restart admin menu access
		    	gotoAdminMenu = false;
		    }
		});
        
        iconPTInovacao.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	//restart admin menu access
		    	gotoAdminMenu = true;
		    }
		});
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	
    	if (click && hasFocus) {
			Logger.v(tag, "onWindowFocusChanged", LogType.Trace, "Append to list!");
		
			String selectedLocal = (String) mySpinner.getSelectedItem();	
			Date testExecDate = new Date();
			Boolean result = engineService.saveActualInformation(selectedLocal, testExecDate);
			
			// test if the information are saved with success
			if (result) {
				
				Drawable image = getResources().getDrawable(R.drawable.ok);
				testStateImageRef.setImageDrawable(image);
				
				textStateTextRef.setText(SUCCESS_EXECUTION);
				textDateTextRef.setText(Utils.buildDateOfNextTest(testExecDate));

			} else {
				
				Drawable image = getResources().getDrawable(R.drawable.notok);
				testStateImageRef.setImageDrawable(image);
				
				textStateTextRef.setText(FAIL_EXECUTION);
				textDateTextRef.setText(Utils.buildDateOfNextTest(testExecDate));
			}
								
			click = false;
			
			// change head test information
			Drawable image4 = getResources().getDrawable(R.drawable.agendamentoparado);
			appStateImageRef.setImageDrawable(image4);
			appStateTextRef.setText(STRING_STOPPED);
		}
    }
	
	public static void serviceAlreadyStart(EngineServiceInterface es) {

		try {
			
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, "In");

			// Set the service reference, in the future we will use this
			// reference to call methods
			engineService = es;
			
			
			// checks to see if exist previous results
			List<StoreInformation> allResults = engineService.getAllResult();
			
			if (allResults != null) {
				
				StoreInformation si = allResults.get(0);
				
				if (si != null) {
					
					if (si.getSuccess()) {
						
						Drawable image = myContect.getResources().getDrawable(R.drawable.ok);
						testStateImageRef.setImageDrawable(image);
						
						textStateTextRef.setText(SUCCESS_EXECUTION);
						textDateTextRef.setText(Utils.buildDateOfNextTest(si.getDate()));
						
					} else {
						
						Drawable image = myContect.getResources().getDrawable(R.drawable.notok);
						testStateImageRef.setImageDrawable(image);
						
						textStateTextRef.setText(FAIL_EXECUTION);
						textDateTextRef.setText(Utils.buildDateOfNextTest(si.getDate()));
						
					}
				}
				
			}
			
			// Força a atualiza da informação da wifi
			engineService.refreshInformation();

		} catch (Exception ex) {
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, ex.toString());
		}
	}

	
	public static List<StoreInformation> getAllResult() {
    	
    	try {
    		
    		return engineService.getAllResult();
    		
    	}catch (Exception ex) {
			Logger.v(tag, "getAllResultByIndex", LogType.Trace, ex.toString());
		}
    	
    	return null;
    }
}