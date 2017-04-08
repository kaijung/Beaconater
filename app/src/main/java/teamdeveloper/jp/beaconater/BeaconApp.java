package teamdeveloper.jp.beaconater;


import android.app.Application;
import android.content.Intent;

import io.realm.Realm;

public class BeaconApp  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        startService(new Intent(this,BroadcastReceiverService.class));
    }
}
