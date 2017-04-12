package teamdeveloper.jp.beaconater;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

// ここが止まるとReceiveできなくなってしまう。


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
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return START_STICKY;
        // START STCIKYで何らかのシステムが止まってもシステムに再起動してもらう。

    }

    @Override
    public void onDestroy(){
        unregisterReceiver(beaconNotification);
        super.onDestroy();
    }
}
