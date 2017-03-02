package PTInov.IEX.ArQoSPocketService.CallService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartAtBoot extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.v("StartAtBoot", "StartAtBoot......................................................................................................................................................................................");
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent i = new Intent();
			i.setAction("PTInov.IEX.ArQoSPocketService.StartAtBootService");
			
			context.startService(i);
		}
	}
}
