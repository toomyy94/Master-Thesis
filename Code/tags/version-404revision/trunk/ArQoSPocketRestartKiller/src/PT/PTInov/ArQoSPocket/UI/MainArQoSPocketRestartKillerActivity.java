package PT.PTInov.ArQoSPocket.UI;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import PT.PTInov.ArQoSPocket.R;
import PT.PTInov.ArQoSPocket.Culture.CultureAdapter;
import PT.PTInov.ArQoSPocket.Culture.Dictionary;
import PT.PTInov.ArQoSPocket.Enums.TestEnum;
import PT.PTInov.ArQoSPocket.Service.DoTestWorker;
import PT.PTInov.ArQoSPocket.Service.EngineServiceInterface;
import PT.PTInov.ArQoSPocket.Service.GPSInformation;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.Utils.RowDataTwoLines;
import PT.PTInov.ArQoSPocket.Utils.StoreAllTestInformation;
import PT.PTInov.ArQoSPocket.Utils.StoreInformation;
import PT.PTInov.ArQoSPocket.Utils.TestEnumToInt;
import PT.PTInov.ArQoSPocket.Utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ParserError")
public class MainArQoSPocketRestartKillerActivity extends Activity implements TestResultCallback {
	
	private final static String tag = "MainArQoSPocketRestartKillerActivity";
	
	// Service reference
	private static EngineServiceInterface engineService = null;
	
	public static Context myContect = null;
	private static MainArQoSPocketRestartKillerActivity myRef = null;
	
	private LayoutInflater factory = null;
	
	private boolean click = false;
	
	private static ImageView appStateImageRef = null;
	private static TextView appStateTextRef = null;
	
	private static ImageView testStateImageRef = null;
	private static TextView textStateTextRef = null;
	private static TextView textDateTextRef = null;
	
	private static ImageView iconArQoSPocket = null;
	private static ImageView iconPTInovacao = null;
	private static boolean gotoAdminMenu = false;
	
	private static ImageView resultbar = null;
	
	private static TextView ActionInformation = null;
	
	private static ImageView doTestImg = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        myContect = this;
        myRef = this;
        
        Dictionary.setSystemCulture();
        Logger.v(tag, "onCreate", LogType.Trace, " My getDisplayLanguage :"+Locale.getDefault().getDisplayLanguage()); 
        
        CultureAdapter.loadMain(this, this);
        
        // Load UI objects references
        appStateImageRef = (ImageView) findViewById(R.id.stateImg);
        appStateTextRef = (TextView) findViewById(R.id.testExecutionInformation);
        
        testStateImageRef = (ImageView) findViewById(R.id.iconTestState);
        textStateTextRef = (TextView) findViewById(R.id.testStateInformation);
        textDateTextRef = (TextView) findViewById(R.id.testTimeDateInformation);
        
        iconArQoSPocket = (ImageView) findViewById(R.id.headerImg);
		iconPTInovacao = (ImageView) findViewById(R.id.logoPTn);
	
		ActionInformation = (TextView) findViewById(R.id.ActionInformationText);
        
        // Start service
        Intent i = new Intent();
        i.setAction("PT.PTInov.ArQoSPocket.Service.EngineService");
        startService(i);
        
        factory = LayoutInflater.from(this);
        
        doTestImg = (ImageView) findViewById(R.id.TestsScheduledImg);
        
        doTestImg.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						
						Drawable imgDoTest2 = getResources().getDrawable(R.drawable.efectuarmedicao_sel);
						doTestImg.setImageDrawable(imgDoTest2);
						
						break;
					case MotionEvent.ACTION_UP:
						
						Drawable imgDoTest3 = getResources().getDrawable(R.drawable.efectuarmedicao);
						doTestImg.setImageDrawable(imgDoTest3);
						
						break;
				}
				
				
				return false;
			}
        	
        });
        
        doTestImg.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	
		    	// change head test information
		    	// change the icon
				Drawable image2 = getResources().getDrawable(R.drawable.testeemexecucao);
				appStateImageRef.setImageDrawable(image2);
				appStateTextRef.setText(Dictionary.getApp_state_exec(myContect));
				
				click = true;
				
				Logger.v(tag, " doTestImg.setOnClickListener", LogType.Trace, "doTestImg 1");
				
				String selectedLocal = "";
				DoTestWorker testWorker = new DoTestWorker(selectedLocal, myRef, engineService);
				testWorker.execute(null,null,null);
				
				Logger.v(tag, " doTestImg.setOnClickListener", LogType.Trace, "doTestImg 2");
		    	
		    	//restart admin menu access
		    	gotoAdminMenu = false;
		    }
		});
      
        resultbar = (ImageView) findViewById(R.id.goToResults);
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
    
    public static void serviceAlreadyStart(EngineServiceInterface es) {

		try {
			
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, "In");

			// Set the service reference, in the future we will use this
			// reference to call methods
			engineService = es;
			
			// checks to see if exist previous results
			List<StoreAllTestInformation> allResults = engineService.getAllResult();
			
			if (allResults != null) {
				
				StoreAllTestInformation si = allResults.get(0);
				
				if (si != null) {
					
					if (si.getStoreInformation().getSuccess()) {
						
						Drawable image = myContect.getResources().getDrawable(R.drawable.ok);
						testStateImageRef.setImageDrawable(image);
						
						textStateTextRef.setText(Dictionary.getApp_state_exec_success(myContect));
						textDateTextRef.setText(Utils.buildDateOfNextTest(si.getRegistryDate()));
						
						showTestResultStateInformation();
						
					} else {
						
						Drawable image = myContect.getResources().getDrawable(R.drawable.notok);
						testStateImageRef.setImageDrawable(image);
						
						textStateTextRef.setText(Dictionary.getApp_state_exec_fail(myContect));
						textDateTextRef.setText(Utils.buildDateOfNextTest(si.getRegistryDate()));
						
						showTestResultStateInformation();
					}
				}
				
			}

		} catch (Exception ex) {
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, ex.toString());
		}
	}
    
    public static List<StoreAllTestInformation> getAllResult() {
    	
    	try {
    		
    		return engineService.getAllResult();
    		
    	}catch (Exception ex) {
			Logger.v(tag, "getAllResultByIndex", LogType.Trace, ex.toString());
		}
    	
    	return null;
    }
    
    public void testResultCallBack(Boolean result, Date testExecDate ) {
		// test if the information are saved with success
    	
    	Logger.v(tag, "testResultCallBack", LogType.Trace, "testResultCallBack 1");
    	
		if (result) {

			Drawable image = myRef.getResources().getDrawable(R.drawable.ok);
			testStateImageRef.setImageDrawable(image);

			textStateTextRef.setText(Dictionary.getApp_state_exec_success(myContect));
			textDateTextRef.setText(Utils.buildDateOfNextTest(testExecDate));
			
			showTestResultStateInformation();

		} else {

			Drawable image = myRef.getResources().getDrawable(R.drawable.notok);
			testStateImageRef.setImageDrawable(image);

			textStateTextRef.setText(Dictionary.getApp_state_exec_fail(myContect));
			textDateTextRef.setText(Utils.buildDateOfNextTest(testExecDate));
			
			showTestResultStateInformation();
		}

		click = false;

		// change head test information
		Drawable image4 = myRef.getResources().getDrawable(R.drawable.agendamentoparado);
		appStateImageRef.setImageDrawable(image4);
		appStateTextRef.setText(Dictionary.getApp_state_stopped(myContect));
		
		Logger.v(tag, "testResultCallBack", LogType.Trace, "testResultCallBack out");
    }
    
    public void reportTestState(TestEnum state) {
    	
    	try {
			
			String text = "Action Description";
			
    		switch(state) {
    		case RadioMeasurements:
    			//text = myContect.getResources().getString(R.string.app_testar_medidas_radio_pt);
    			text = Dictionary.getApp_testar_medidas_radio(myContect);
    			Logger.v(tag, "handlerReportTestAction", LogType.Trace, "RadioMeasurements :"+text);
    			ActionInformation.setText(text);
    			break;
    		case AccessTest:
    			//text = myContect.getResources().getString(R.string.app_testar_teste_acesso_pt);
    			text = Dictionary.getApp_testar_teste_acesso(myContect);
    			Logger.v(tag, "handlerReportTestAction", LogType.Trace, "AccessTest :"+text);
    			ActionInformation.setText(text);
    			break;
    		case BandwidthTest:
    			//text = myContect.getResources().getString(R.string.app_testar_teste_banda_pt);
    			text = Dictionary.getApp_testar_teste_banda(myContect);
    			Logger.v(tag, "handlerReportTestAction", LogType.Trace, "BandwidthTest :"+text);
    			ActionInformation.setText(text);
    			break;
    		case FTPTest:
    			//text = myContect.getResources().getString(R.string.app_testar_teste_ftp_pt);
    			text = Dictionary.getApp_testar_teste_ftp(myContect);
    			Logger.v(tag, "handlerReportTestAction", LogType.Trace, "FTPTest :"+text);
    			ActionInformation.setText(text);
    			break;
    		case SendInformation:
    			//text = myContect.getResources().getString(R.string.app_sending_info_to_server_pt);
    			text = Dictionary.getApp_sending_info_to_server(myContect);
    			Logger.v(tag, "handlerReportTestAction", LogType.Trace, "SendInformation :"+text);
    			ActionInformation.setText(text);
    			break;
    		}
    		
    		Logger.v(tag, "handlerReportTestAction", LogType.Trace, "Done :"+text);
    		
    		showTestStateInformation();
    		
    		Logger.v(tag, "handlerReportTestAction", LogType.Trace, "Done 2");
    		
    	}catch (Exception ex) {
			Logger.v(tag, "handlerReportTestAction", LogType.Error, ex.toString());
		}
    }
    
    private static void showTestStateInformation() 
    {
    	ActionInformation.setVisibility(View.VISIBLE);
    	
    	// iconTestState
    	testStateImageRef.setVisibility(View.INVISIBLE);
    	// testStateInformation
    	textStateTextRef.setVisibility(View.INVISIBLE);
    	// testTimeDateInformation
    	textDateTextRef.setVisibility(View.INVISIBLE);
    	// goToResults
    	resultbar.setVisibility(View.INVISIBLE);
    }
    
    private static void showTestResultStateInformation() {
    	
    	ActionInformation.setVisibility(View.INVISIBLE);
    	
    	// iconTestState
    	testStateImageRef.setVisibility(View.VISIBLE);
    	// testStateInformation
    	textStateTextRef.setVisibility(View.VISIBLE);
    	// testTimeDateInformation
    	textDateTextRef.setVisibility(View.VISIBLE);
    	// goToResults
    	resultbar.setVisibility(View.VISIBLE);
    }
    
}