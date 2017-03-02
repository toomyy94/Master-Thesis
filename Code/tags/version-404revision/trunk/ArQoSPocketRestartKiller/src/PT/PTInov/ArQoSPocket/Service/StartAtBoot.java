package PT.PTInov.ArQoSPocket.Service;

import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartAtBoot extends BroadcastReceiver {
	
	private final static String tag = "StartAtBoot";
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			
			Logger.v(tag, "onReceive", LogType.Trace, "In");
			
			Intent i = new Intent();
			i.setAction("PT.PTInov.ArQoSPocket.Service.EngineService");
			context.startService(i);
		}
	}
}
