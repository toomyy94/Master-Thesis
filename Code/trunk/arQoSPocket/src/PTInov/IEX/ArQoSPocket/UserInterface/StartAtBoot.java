package PTInov.IEX.ArQoSPocket.UserInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartAtBoot extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			
			Log.v("tessstttttteeeee",".......................teste arranque.......................");
			
			Intent i = new Intent();
			i.setAction("PTInov.IEX.ArQoSPocket.EngineService.EngineService");
			context.startService(i);
		}
	}
}
