package pt.ptinovacao.arqospocket.service.service.utils;


import pt.ptinovacao.arqospocket.service.service.EngineService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, EngineService.class);
        context.startService(startServiceIntent);
    }
}