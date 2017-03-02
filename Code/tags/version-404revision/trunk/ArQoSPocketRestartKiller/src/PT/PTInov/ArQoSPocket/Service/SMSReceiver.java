package PT.PTInov.ArQoSPocket.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver  {
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		
		SmsMessage[] msgs =null;
		String str="";
		
		if(bundle!=null)
		{
			Object[] pdus =(Object[])bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length ;i++)
			{
				msgs[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
			str+="SMS de"+ msgs[i].getOriginatingAddress()+"\n";
			str+=":"+msgs[i].getMessageBody().toString()+"\n";
			}
				
			
		//Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
		
		}}
		
}