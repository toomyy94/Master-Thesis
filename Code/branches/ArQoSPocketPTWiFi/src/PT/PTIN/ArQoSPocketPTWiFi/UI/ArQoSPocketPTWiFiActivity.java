package PT.PTIN.ArQoSPocketPTWiFi.UI;

import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.R;
import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Services.EngineServiceInterface;
import PT.PTIN.ArQoSPocketPTWiFi.Store.StoreInformation;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.ActionResult;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.Utils;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ParserError")
public class ArQoSPocketPTWiFiActivity extends Activity {

	private final static String tag = "ArQoSPocketPTWiFiActivity";

	
	private static final String STRING_EXECUTION = "Em execução";
	private static final String STRING_STOPPED = "Parado";

	private static final String SUCCESS_EXECUTION = "Medição com Sucesso";
	private static final String FAIL_EXECUTION = "Medição sem Sucesso";
	
	private static final String VERIFYWIFI_TEXT = "A verificar a ligação WiFi...";
	private static final String SCANWIFINETWORKS_TEXT = "A procurar redes PT WiFi...";
	private static final String ASSOCIATEPTWIFI_TEXT = "A ligar à rede PT WiFi...";
	private static final String AUTHPTWIFI_TEXT = "A autenticar na PT WiFi...";
	private static final String DOPING_TEXT = "A efetuar teste de PING...";
	private static final String DODOWNLOAD_TEXT = "A efetuar teste de download...";
	private static final String DOUPLOAD_TEXT = "A efetuar teste de upload...";
	private static final String DOSENDTEST_TEXT = "A enviar o resultado do teste...";
	
	public static final int TURN_ON_WIFI = 0;
	public static final int DO_WIFI_SCAN = 1;
	public static final int ASSOCIATE_PTWIFI = 2;
	public static final int AUTH_PTWIFI = 3;
	public static final int PING = 4;
	public static final int DOWNLOAD = 5;
	public static final int UPLOAD = 6;
	public static final int SEND_TEST = 7;

	// Service reference
	private static EngineServiceInterface engineService = null;

	public static Context myContect = null;
	private static boolean clicked = false;

	private static ImageView appStateImageRef = null;
	private static TextView appStateTextRef = null;
	
	private static TextView appActionInformationTextRef = null;
	private static ImageView appGoToResults = null;

	private static ImageView testStateImageRef = null;
	private static TextView textStateTextRef = null;
	private static TextView textDateTextRef = null;
	
	private static ImageView startTestImageRef = null;
	
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
		
		appActionInformationTextRef = (TextView) findViewById(R.id.ActionInformation);
		
		appGoToResults = (ImageView) findViewById(R.id.goToResults);

		testStateImageRef = (ImageView) findViewById(R.id.iconTestState);
		textStateTextRef = (TextView) findViewById(R.id.testStateInformation);
		textDateTextRef = (TextView) findViewById(R.id.testTimeDateInformation);
		
		startTestImageRef = (ImageView) findViewById(R.id.TestsScheduledImg);
		
		iconArQoSPocket = (ImageView) findViewById(R.id.headerImg);
		iconPTInovacao = (ImageView) findViewById(R.id.logoPTn);
		
        Intent i = new Intent();
        i.setAction("PT.PTInov.ArQoSPocketPTWiFi.Services.EngineService");
        startService(i);

		myContect = this;
		
		ImageView doTest = (ImageView) findViewById(R.id.TestsScheduledImg);
        doTest.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	
		    	if (clicked == false) {
		    	
		    		clicked = true;
		    	
		    		// change the UI to execution state
		    		changeUItoExecutionState();
		    		
		    		engineService.doTest();
		    		
		    		//restart admin menu access
			    	gotoAdminMenu = false;
		    		
		    	}
		    }
		});
        
        appGoToResults.setOnClickListener(new OnClickListener() {
        	
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
	
	
	private static Handler handlerTestDone = new Handler() {

		public void handleMessage(Message msg) {
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, "handlerTestDone - In");
			
			ActionResult actionResult = (ActionResult) msg.obj;
			
			if (actionResult != null) {
				
				if (actionResult.getGeralActionState() == ActionState.OK) {
				
					Drawable image2 = myContect.getResources().getDrawable(R.drawable.ok);
					testStateImageRef.setImageDrawable(image2);
				
					textStateTextRef.setText(SUCCESS_EXECUTION);
					textDateTextRef.setText(Utils.buildDateOfNextTest(actionResult.getTestDate()));
				
				} else {
				
					Drawable image2 = myContect.getResources().getDrawable(R.drawable.notok);
					testStateImageRef.setImageDrawable(image2);
				
					textStateTextRef.setText(FAIL_EXECUTION);
					textDateTextRef.setText(Utils.buildDateOfNextTest(actionResult.getTestDate()));
				
				}
			}
			
			// restore the state off the button
			clicked = false;
			
			// change UI to stopped state
			changeUItoStoppedState();
			
		}
	};
	
	private static Handler handlerReportTestAction = new Handler() {

		public void handleMessage(Message msg) {
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, "handlerTestDone - In");
			
			int testAction = msg.arg1;
			
			switch(testAction) {
				case TURN_ON_WIFI:
					
					showVerifyWiFi();
					
					break;
				case DO_WIFI_SCAN:
					
					showScanWiFINetworks();
					
					break;
				case ASSOCIATE_PTWIFI:
					
					showAssociatePTWIFI(); 
					
					break;
				case AUTH_PTWIFI:
					
					showAuthPTWIFI();
					
					break;
				case PING:
					
					showPING();
					
					break;
				case DOWNLOAD:
					
					showDownload();
					
					break;
				case UPLOAD:
					
					showUpload();
					
					break;
				case SEND_TEST:
					
					showSendResult(); 
					
					break;
			}
		}
	};
	
	
	public static void serviceAlreadyStart(EngineServiceInterface es) {

		try {
			
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, "In");

			// Set the service reference, in the future we will use this
			// reference to call methods
			engineService = es;
			
			// set the handler reference
			engineService.setHandlerTestDone(handlerTestDone);
			engineService.setHandlerReportTestAction(handlerReportTestAction);
			
			// load the state that are saved in the service
			clicked = engineService.isRunningTest();
			
			// refresh UI
			if (clicked) {
				
				// change image to disable
	    		Drawable image = myContect.getResources().getDrawable(R.drawable.executarinativo);
	    		startTestImageRef.setImageDrawable(image);
	    		
			} else {
				
				// change image to disable
	    		Drawable image = myContect.getResources().getDrawable(R.drawable.executar);
	    		startTestImageRef.setImageDrawable(image);
	    		
	    		// checks to see if exist previous results
				List<StoreInformation> allStoreInformation = engineService.getAllStoreInformation();
				
				if (allStoreInformation != null) {
					
					if (allStoreInformation.size()>0) {
						StoreInformation si = allStoreInformation.get(0);
					
						if (si != null) {
						
							if (si.getSuccess()) {
							
								Drawable image2 = myContect.getResources().getDrawable(R.drawable.ok);
								testStateImageRef.setImageDrawable(image2);
							
								textStateTextRef.setText(SUCCESS_EXECUTION);
								textDateTextRef.setText(Utils.buildDateOfNextTest(si.getDate()));
							
							} else {
							
								Drawable image2 = myContect.getResources().getDrawable(R.drawable.notok);
								testStateImageRef.setImageDrawable(image2);
							
								textStateTextRef.setText(FAIL_EXECUTION);
								textDateTextRef.setText(Utils.buildDateOfNextTest(si.getDate()));
							
							}
						}
					}
				}
			}


		} catch (Exception ex) {
			Logger.v(tag, "serviceAlreadyStart", LogType.Trace, ex.toString());
		}
	}
	
	
	private static void makeActionVisivel() {
		
		// show my object
		appActionInformationTextRef.setVisibility(View.VISIBLE);
						
		//hide action information objects
		textStateTextRef.setVisibility(View.INVISIBLE);
		textDateTextRef.setVisibility(View.INVISIBLE);
		testStateImageRef.setVisibility(View.INVISIBLE);
		appGoToResults.setVisibility(View.INVISIBLE);		
	}
	
	private static void makeResultsVisivel() {
		
		// show my object
		textStateTextRef.setVisibility(View.VISIBLE);
		textDateTextRef.setVisibility(View.VISIBLE);
		testStateImageRef.setVisibility(View.VISIBLE);
		appGoToResults.setVisibility(View.VISIBLE);
				
		//hide action information objects
		appActionInformationTextRef.setVisibility(View.INVISIBLE);
	}
	
	
	
	private static void showSendResult() {
		// show my object and hide result objects
		makeActionVisivel();
						
		appActionInformationTextRef.setText(DOSENDTEST_TEXT);
	}
	
	private static void showUpload() {
		// show my object and hide result objects
		makeActionVisivel();
						
		appActionInformationTextRef.setText(DOUPLOAD_TEXT);
	}
	
	private static void showDownload() {
		// show my object and hide result objects
		makeActionVisivel();
						
		appActionInformationTextRef.setText(DODOWNLOAD_TEXT);
	}
	
	private static void showPING() {
		// show my object and hide result objects
		makeActionVisivel();
						
		appActionInformationTextRef.setText(DOPING_TEXT);
	}
	
	private static void showAuthPTWIFI() {
		// show my object and hide result objects
		makeActionVisivel();
						
		appActionInformationTextRef.setText(AUTHPTWIFI_TEXT);
	}
	
	private static void showAssociatePTWIFI() {
		// show my object and hide result objects
		makeActionVisivel();
						
		appActionInformationTextRef.setText(ASSOCIATEPTWIFI_TEXT);
	}
	
	private static void showScanWiFINetworks() {
		
		// show my object and hide result objects
		makeActionVisivel();
				
		appActionInformationTextRef.setText(SCANWIFINETWORKS_TEXT);
	}
	
	private static void showVerifyWiFi() {
		
		// show my object and hide result objects
		makeActionVisivel();
		
		appActionInformationTextRef.setText(VERIFYWIFI_TEXT);
	}
	
	private static void changeUItoExecutionState() {
		
		// show my object and hide action information objects
		makeResultsVisivel();
		
		// change image to disable
		Drawable image = myContect.getResources().getDrawable(R.drawable.executarinativo);
		startTestImageRef.setImageDrawable(image);
		
		Drawable image2 = myContect.getResources().getDrawable(R.drawable.testeemexecucao);
		appStateImageRef.setImageDrawable(image2);
		
		appStateTextRef.setText(STRING_EXECUTION);
	}
	
	private static void changeUItoStoppedState() {
		
		// show my object and hide action information objects
		makeResultsVisivel();
		
		// change image to disable
		Drawable image = myContect.getResources().getDrawable(R.drawable.executar);
		startTestImageRef.setImageDrawable(image);
		
		Drawable image2 = myContect.getResources().getDrawable(R.drawable.agendamentoparado);
		appStateImageRef.setImageDrawable(image2);
		
		appStateTextRef.setText(STRING_STOPPED);
	}
	
	public static List<StoreInformation> getAllStoreInformation() {
    	
    	try {
    		
    		return engineService.getAllStoreInformation();
    		
    	}catch (Exception ex) {
			Logger.v(tag, "getAllResultByIndex", LogType.Trace, ex.toString());
		}
    	
    	return null;
    }
}