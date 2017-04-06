package pt.ptinovacao.arqospocket.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.CurrentConfiguration;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailWifi;
import pt.ptinovacao.arqospocket.service.enums.EEvent;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IServiceNetworksInfo;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.service.EngineService;
import pt.ptinovacao.arqospocket.service.tasks.Mobile;
import pt.ptinovacao.arqospocket.service.tasks.Wifi;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IMobileCallback;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IWifiCallback;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WiFiAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WifiBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.utils.ApnEditor;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;

public class Activity_Development extends ArqosActivity {

    private final static String TAG = Activity_Development.class.getSimpleName();
    private final static Logger logger = LoggerFactory.getLogger(Activity_Development.class);

	MenuOption HomeP;
	Homepage home;

	RadioGroup development_tests, development_logs, development_apn, development_radiologs;
	int idxlogs;

    ApnEditor apn_editor = new ApnEditor();
    //public static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
    //public static final Uri APN_PREFER_URI = Uri.parse("content://telephony/carriers/preferapn");

    private IMobileCallback IMobileCallback;
    private IWifiCallback IWifiCallback;
	public JSONObject radiolog = new JSONObject();
	public JSONArray radiolog_String = new JSONArray();


    protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.development));
		super.setMenuOption(MenuOption.Desenvolvimento);
		setContentView(R.layout.activity_development);
		home = new Homepage (this);

		addListenerOnButton();

	}

	public void addListenerOnButton() {

		//Listener Group-Buttons
		development_tests = (RadioGroup) findViewById(R.id.development_tests_general);
		development_apn = (RadioGroup) findViewById(R.id.development_tests_apn);
		development_logs = (RadioGroup) findViewById(R.id.development_logs_page);
		development_radiologs = (RadioGroup) findViewById(R.id.development_radiologs_page);

        development_tests.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				View auxlogs = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(auxlogs);

				switch(idxlogs){
					case 0:
						//Send DTMF tone
                        dialDTMF("200,4,,3,,1");
						break;
				}
			}
		});

		development_apn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				View auxlogs = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(auxlogs);

////                Debug
//				Log.d("idxLogs",""+idxlogs);

                switch(idxlogs){
                    case 0:
                        //System App ONLY!!!!
                        //change default APN

                        apn_editor.EnumerateAPNs(getApplicationContext());

                        //320-> tmn internet, 321->mms tmn
                        boolean changed = apn_editor.SetDefaultAPN(getApplicationContext(), 321);
                        Log.d("APN prefered changed =",""+changed);
                        break;
                    case 2:
                        //System App ONLY!!!!
                        //insert APN
                        apn_editor.InsertAPN(getApplicationContext(), "apn_name", "apn_addr");
                        break;
                }

			}
		});

		development_logs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {


				View auxlogs = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(auxlogs);

//                Debug
				Log.d("idxLogs",""+idxlogs);

				switch(idxlogs){
                    case 0:
                        //Escrever logcat para logfile.txt

                        //Root Permissions to READ_ALL_LOG
                        new RootReadLogsPermission().execute();
						new WriteLogFile().execute();
                        break;
                    case 2:
                        //Clear logcat
                        clearLog();
                        break;
				}
			}
		});

		development_radiologs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {


				View auxlogs = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(auxlogs);

//                Debug
				Log.d("idxLogs",""+idxlogs);

				switch(idxlogs){
					case 0:
						//Generate radioLog
                        generateRadiolog(getApplicationContext());

						break;
					case 2:
						//Send radiolog to Management System
                        /*
                        ------->DO STUFF HERE!!!
                         */
						break;
				}
			}
		});
	}




	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Desenvolvimento){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
		}
		}
	}

    /**
     * Dials a number with DTMF chars
     * Note: When the number is dialed, only the initial number is displayed on the device dialer
     * For example: dial("*6900,,1") will display only *6900 on the device dialer (but the rest will also be processed)
     * @param number
     */
    public void dialDTMF(String number) {
        try {
            number = new String(number.trim().replace(" ", "%20").replace("&", "%26")
                    .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
                    .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
                    .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
                    .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
                    .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
                    .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
                    .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
                    .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
                    .replace("|", "%7C").replace("}", "%7D"));

            Uri uri = Uri.parse("tel:"+ number);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(intent);

        } catch (Exception e) {
            //getAlertDialog().setMessage("Invalid number");
            e.printStackTrace();
        }
    }

//    public static boolean changeDefaultAPN(Context context, String name) {
//
//        boolean changed = false;
//        String columns[] = new String[] { Carriers._ID, Carriers.NAME };
//        String where = "name = ?";
//        String wargs[] = new String[] {name};
//        String sortOrder = null;
//        Cursor cur = context.getContentResolver().query(APN_TABLE_URI, columns, where, wargs, sortOrder);
//        if (cur != null) {
//            if (cur.moveToFirst()) {
//                ContentValues values = new ContentValues(1);
//                values.put("apn_id", cur.getLong(0));
//                if (context.getContentResolver().update(APN_PREFER_URI, values, null, null) == 1)
//                    changed = true;
//            }
//            cur.close();
//
//        }
//        return changed;
//
//    }

	public static void printLog(String logData) {

		try {
			File logFile = new File(Environment.getExternalStorageDirectory(),
					"arQoSPocketLog.txt");
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
						true));
				buf.append(logData);
				buf.newLine();
				buf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void clearLog(){
		try {
			Process process = new ProcessBuilder()
					.command("logcat", "-c")
					.redirectErrorStream(true)
					.start();
		} catch (IOException e) {
		}
	}

    public void generateRadiolog(Context context){

        MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
        IService iService = MyApplicationRef.getEngineServiceRef();

		iService.generate_radiolog();
        //return radiolog;
    }

	private class WriteLogFile extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				Process process = Runtime.getRuntime().exec("su -c logcat V");
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));

				StringBuilder log = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					log.append(line);
					printLog(line);

				}

			} catch (IOException e) {
			}
			return "";
		}

		protected void onPostExecute(Boolean result) {

		}

	}

	private class RootReadLogsPermission extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... urls) {

			String pname = getPackageName();
			String[] CMDLINE_GRANTPERMS = { "su", "-c", null };
			if (getPackageManager().checkPermission(android.Manifest.permission.READ_LOGS, pname) != 0) {
				Log.d(TAG, "we do not have the READ_LOGS permission!");
				if (android.os.Build.VERSION.SDK_INT >= 16) {
					Log.d(TAG, "Working around JellyBeans 'feature'...");
					try {
						// format the commandline parameter
						CMDLINE_GRANTPERMS[2] = String.format("pm grant %s android.permission.READ_LOGS", pname);
						java.lang.Process p = Runtime.getRuntime().exec(CMDLINE_GRANTPERMS);
						int res = p.waitFor();
						Log.d(TAG, "exec returned: " + res);
						if (res != 0)
							throw new Exception("failed to become root");
					} catch (Exception e) {
						Log.d(TAG, "exec(): " + e);
						e.printStackTrace();
//						Toast.makeText(getApplicationContext(), "Failed to obtain READ_LOGS permission", Toast.LENGTH_LONG).show();
					}
				}
			} else
				Log.d(TAG, "we have the READ_LOGS permission already!");

			return "";
		}

		protected void onPostExecute(Boolean result) {

		}

	}

    public void send_pending_tests_ack(ENetworkAction action_state) {

    }
}

