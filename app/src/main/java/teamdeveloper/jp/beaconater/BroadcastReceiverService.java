package teamdeveloper.jp.beaconater;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BroadcastReceiverService extends Service {
    private BeaconNotification beaconNotification;
    public static final String BROADCAST_DIDENTER = "teamdeveloper.jp.beaconater.DID_ENTER_REGION";
    public static final String BROADCAST_DIDEXIT = "teamdeveloper.jp.beaconater.DID_EXIT_REGION";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        beaconNotification = new BeaconNotification();
        registerReceiver(beaconNotification,new IntentFilter(BROADCAST_DIDENTER));
        registerReceiver(beaconNotification,new IntentFilter(BROADCAST_DIDEXIT));
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(beaconNotification);
        super.onDestroy();
    }
}
