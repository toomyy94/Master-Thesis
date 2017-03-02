package PT.PTInov.ArQoSPocket.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.IMMConstants;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMContent;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMEncoder;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMMessage;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMResponse;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMSender;
import PT.PTInov.ArQoSPocket.Service.MMS.SendMMS.APNHelper;
import PT.PTInov.ArQoSPocket.Service.MMS.SendMMS.ConnectivityBroadcastReceiver;
import PT.PTInov.ArQoSPocket.Service.MMS.SendMMS.PhoneEx;
import PT.PTInov.ArQoSPocket.Service.MMS.SendMMS.APNHelper.APN;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

public class MyMMSManager implements IMyMMSManager {
	
	private static final String TAG = "MyMMSManager";
	
	private Context appContext = null;
	
	private ConnectivityManager mConnMgr = null;
	private PowerManager.WakeLock mWakeLock = null;
	private ConnectivityBroadcastReceiver mReceiver = null;
	
	private IMyMMSManager callbackRef = null;

	public MyMMSManager(Context appContext) {
		final String method = "MyMMSManager";
		
		try {
			
			this.callbackRef = callbackRef;
			this.appContext = appContext;
			
		} catch(Exception ex) {
			Logger.v(TAG, LogType.Error, method+":: Error :"+ex.toString());
		}
	}
	
	public boolean sendMMSSync(String filePath, String destNumber) {
		final String method = "sendMMSSync";
		
		boolean returnValue =  false;
		
		try {
			
			mReceiver = new ConnectivityBroadcastReceiver(this, filePath, destNumber);
			mReceiver.setListening(true);
			mReceiver.setSending(false);
			
			mConnMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			appContext.registerReceiver(mReceiver, filter);
		
			// Ask to start the connection to the APN. Pulled from Android source code.
			int result = beginMmsConnectivity();
			
			if (result != PhoneEx.APN_ALREADY_ACTIVE) {
				Log.v(TAG, "Extending MMS connectivity returned " + result + " instead of APN_ALREADY_ACTIVE");
				// Just wait for connectivity startup without
				// any new request of APN switch.
				return false;
			}
			
			//7.....
			
			//mReceiver.sendMMSUsingNokiaAPI(filePath, destNumber);
			
		} catch(Exception ex) {
			Logger.v(TAG, LogType.Error, method+":: Error :"+ex.toString());
		}
		
		return returnValue;
	}
	
	public void sendMMSAsync(IMyMMSManager pcallbackRef, String filePath, String destNumber) {
		final String method = "sendMMSAsync";
		
		try {
			
			callbackRef = pcallbackRef;
			
			mReceiver = new ConnectivityBroadcastReceiver(this, filePath, destNumber);
			mReceiver.setListening(true);
			mReceiver.setSending(false);
			
			mConnMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			appContext.registerReceiver(mReceiver, filter);
		
			// Ask to start the connection to the APN. Pulled from Android source code.
			int result = beginMmsConnectivity();
			
			if (result != PhoneEx.APN_ALREADY_ACTIVE) {
				Log.v(TAG, "Extending MMS connectivity returned " + result + " instead of APN_ALREADY_ACTIVE");
				// Just wait for connectivity startup without
				// any new request of APN switch.
				callbackRef.send_mms_report(false);
			}
		
			
		} catch(Exception ex) {
			Logger.v(TAG, LogType.Error, method+":: Error :"+ex.toString());
		}
	}
	
	protected int beginMmsConnectivity() throws IOException {
		// Take a wake lock so we don't fall asleep before the message is downloaded.
		createWakeLock();

		int result = mConnMgr.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, PhoneEx.FEATURE_ENABLE_MMS);

		Log.v(TAG, "beginMmsConnectivity: result=" + result);

		switch (result) {
		case PhoneEx.APN_ALREADY_ACTIVE:
		case PhoneEx.APN_REQUEST_STARTED:
			acquireWakeLock();
			return result;
		}

		throw new IOException("Impossivel estabelecer conectividade MMS");
	}
	
	private synchronized void createWakeLock() {
		// Create a new wake lock if we haven't made one yet.
		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MMS Connectivity");
			mWakeLock.setReferenceCounted(false);
		}
	}
	
	private void acquireWakeLock() {
		// It's okay to double-acquire this because we are not using it
		// in reference-counted mode.
		mWakeLock.acquire();
	}

	private void releaseWakeLock() {
		// Don't release the wake lock if it hasn't been created and acquired.
		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
	}
	
	protected void endMmsConnectivity() {
		// End the connectivity
		try {
			Log.v(TAG, "endMmsConnectivity");
			if (mConnMgr != null) {
				mConnMgr.stopUsingNetworkFeature(
						ConnectivityManager.TYPE_MOBILE,
						PhoneEx.FEATURE_ENABLE_MMS);
			}
		} finally {
			releaseWakeLock();
		}
	}

	public void send_mms_report(boolean state) {
		final String method = "send_mms_report";
		
		Logger.v(TAG, LogType.Debug, method+":: send_mms_report :"+state);
		
		
		if (callbackRef != null)
			callbackRef.send_mms_report(state);
	}

	public void end_connectivity() {
		
		final String method = "end_connectivity";
		
		Logger.v(TAG, LogType.Debug, method+":: end_connectivity");
		
		endMmsConnectivity();
	}
}
