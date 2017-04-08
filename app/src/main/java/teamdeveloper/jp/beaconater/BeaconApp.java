package teamdeveloper.jp.beaconater;


import android.app.Application;

import io.realm.Realm;

public class BeaconApp  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
