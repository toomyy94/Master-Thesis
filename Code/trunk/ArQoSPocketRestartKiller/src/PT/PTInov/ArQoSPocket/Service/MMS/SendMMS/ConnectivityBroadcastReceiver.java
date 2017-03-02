package PT.PTInov.ArQoSPocket.Service.MMS.SendMMS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
 
import PT.PTInov.ArQoSPocket.Enums.MMSState;
import PT.PTInov.ArQoSPocket.Service.IMyMMSManager;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.IMMConstants;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMContent;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMEncoder;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMMessage;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMResponse;
import PT.PTInov.ArQoSPocket.Service.MMS.Encoder.MMSender;
import PT.PTInov.ArQoSPocket.Service.MMS.SendMMS.APNHelper.APN;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver{
	
	private static final String TAG = "ConnectivityBroadcastReceiver";
	
	private NetworkInfo mNetworkInfo;
	private NetworkInfo mOtherNetworkInfo;
	
	private boolean mListening;
	private boolean mSending;
	private MMSState mState;
	
	private Context appContext = null;
	String filePath = null, destNumber = null;
	
	private IMyMMSManager callbackRef = null;
	
	public ConnectivityBroadcastReceiver(IMyMMSManager pcallbackRef, String filePath, String destNumber) {
		this.filePath = filePath;
		this.destNumber = destNumber;
		callbackRef = pcallbackRef;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		appContext = context;
		
		String action = intent.getAction();

		if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || mListening == false) {
			Log.w(TAG, "onReceived() called with " + mState.toString() + " and " + intent);
			return;
		}

		boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

		if (noConnectivity) {
			mState = MMSState.NOT_CONNECTED;
		} else {
			mState = MMSState.CONNECTED;
		}

		mNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		mOtherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

//		mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
//		mIsFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);


		// Check availability of the mobile network.
		if ((mNetworkInfo == null) || (mNetworkInfo.getType() != ConnectivityManager.TYPE_MOBILE_MMS)) {
			Log.v(TAG, "   type is not TYPE_MOBILE_MMS, bail");
			callbackRef.send_mms_report(false);
			return;
		}

		if (!mNetworkInfo.isConnected()) {
			Log.v(TAG, "    TYPE_MOBILE_MMS Não conectado!");
			callbackRef.send_mms_report(false);
			return;
		}
		else
		{ 
			Log.v(TAG, "connected..");

			if(mSending == false)
			{
				mSending = true;
				Log.v(TAG, "filePath :"+filePath);
				Log.v(TAG, "destNumber :"+destNumber);
				sendMMSUsingNokiaAPI(filePath, destNumber);
			}
		}
	}
	
	
	public void sendMMSUsingNokiaAPI(String filePath, String destNumber)
	{
		// Magic happens here.
		
		MMMessage mm = new MMMessage();
	    SetMessage(mm, destNumber);
	    AddContents(mm, filePath);

	    MMEncoder encoder=new MMEncoder();
	    encoder.setMessage(mm);

	    try {
	      encoder.encodeMessage();
	      byte[] out = encoder.getMessage();
	      
	      MMSender sender = new MMSender();
	      APNHelper apnHelper = new APNHelper(appContext);
	      List<APN> results = apnHelper.getMMSApns();

	      if(results.size() > 0){

	    	  final String MMSCenterUrl = results.get(0).MMSCenterUrl;
	    	  final String MMSProxy = results.get(0).MMSProxy;
	    	  final int MMSPort = Integer.valueOf(results.get(0).MMSPort);
	    	  final Boolean  isProxySet =   (MMSProxy != null) && (MMSProxy.trim().length() != 0);			
	    	  
	    	  sender.setMMSCURL(MMSCenterUrl);
	    	  sender.addHeader("X-NOKIA-MMSC-Charging", "100");

		      MMResponse mmResponse = sender.send(out, isProxySet, MMSProxy, MMSPort);
		      Log.d(TAG, "Mensagem enviada para " + sender.getMMSCURL());
		      Toast.makeText(appContext, "Mensagem Enviada", Toast.LENGTH_SHORT).show();
		      Log.d(TAG, "Codigo de resposta: " + mmResponse.getResponseCode() + " " + mmResponse.getResponseMessage());

		      Enumeration keys = mmResponse.getHeadersList();
		      while (keys.hasMoreElements()){
		        String key = (String) keys.nextElement();
		        String value = (String) mmResponse.getHeaderValue(key);
		        Log.d(TAG, (key + ": " + value));
		      }
		      
		      if(mmResponse.getResponseCode() == 200)
		      {
		    	//  Toast.makeText(getBaseContext(), "Mensagem Entregue", Toast.LENGTH_SHORT).show();
		    	  
		    	  // 200 Successful, disconnect and reset.
		    	  callbackRef.end_connectivity();
		    	  
		    	  mSending = false;
		    	  mListening = false;
		    	  
		    	  callbackRef.send_mms_report(true);
		      }
		      else
		      {
		    	  
		    	  callbackRef.send_mms_report(false);
		      }
	      }	
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	}
	
	private void AddContents(MMMessage mm, String filePath) {
	    /*Path where contents are stored*/

		// You need to have this file in your SD. Otherwise error..
		// "19.png"
		File file = new File(Environment.getExternalStorageDirectory(), filePath); 
	    Uri outputFileUri = Uri.fromFile( file );
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    Bitmap b;

    	try {
			b = Media.getBitmap(appContext.getContentResolver(), outputFileUri);
			b.compress(CompressFormat.PNG, 90, os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    // Adds text content
	    MMContent part1 = new MMContent();
	    byte[] buf1 = os.toByteArray();
	    part1.setContent(buf1, 0, buf1.length);
	    part1.setContentId("<0>");
	    part1.setType(IMMConstants.CT_IMAGE_PNG);
	    mm.addContent(part1);
	  
	  }

	private void SetMessage(MMMessage mm, String destNumber) {
	    mm.setVersion(IMMConstants.MMS_VERSION_10);
	    mm.setMessageType(IMMConstants.MESSAGE_TYPE_M_SEND_REQ);
	    mm.setTransactionId("0000000066");
	    mm.setDate(new Date(System.currentTimeMillis()));
	    mm.setFrom("+351962807260/TYPE=PLMN"); // doesnt work, i wish this worked as it should be
	    // +351925053598
	    mm.addToAddress(destNumber+"/TYPE=PLMN");
	    mm.setDeliveryReport(true);
	    mm.setReadReply(false);
	    mm.setSenderVisibility(IMMConstants.SENDER_VISIBILITY_SHOW);
	    mm.setSubject("This is a nice message!!");
	    mm.setMessageClass(IMMConstants.MESSAGE_CLASS_PERSONAL);
	    mm.setPriority(IMMConstants.PRIORITY_LOW);
	    mm.setContentType(IMMConstants.CT_APPLICATION_MULTIPART_MIXED);
	    

	  }
	
	
	public void setListening(boolean listening) {
		this.mListening = listening;
	}
	
	public void setSending(boolean sending) {
		this.mSending = sending;
	}
}
