package teamdeveloper.jp.beaconater;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

public class BeaconService2 extends Service implements BootstrapNotifier {

    public static final String TAG = org.altbeacon.beacon.service.BeaconService.class.getSimpleName();

    // iBeaconのデータを認識するためのParserフォーマット
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    // BGで監視するiBeacon領域
    private RegionBootstrap regionBootstrap;
    // iBeacon検知用のマネージャー
    private BeaconManager beaconManager;
    // UUID設定用
    private Identifier identifier;
    // iBeacon領域
    private Region region;
    // 監視するiBeacon領域の名前
    private String beaconName;

    @Override
    public void onCreate() {
        super.onCreate();

        // iBeaconのデータを受信できるようにParserを設定
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        // BGでiBeacon領域を監視(モニタリング)するスキャン間隔を設定
        beaconManager.setBackgroundBetweenScanPeriod(1000);

        // UUIDの作成
        identifier = Identifier.parse("A56BA1E1-C06E-4C08-8467-DB6F5BD04486");
        // Beacon名の作成
        beaconName = "MyBeacon-000206C6";
        // major, minorの指定はしない
        region = new Region("", null, null, null);
        region = new Region(beaconName, identifier, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // 検出したビーコンの情報を全部Logに書き出す
                for(Beacon beacon : beacons) {
                    Log.d(TAG, "UUID:" + beacon.getId1() + ", major:" + beacon.getId2() + ", minor:" + beacon.getId3() + ", Distance:" + beacon.getDistance() + ",RSSI" + beacon.getRssi() + ", TxPower" + beacon.getTxPower());
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void didEnterRegion(Region region) {
        // 領域侵入
        Log.d(TAG, "Enter Region");
        // アプリをFG起動させる
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        try {
            // レンジング開始
            beaconManager.startRangingBeaconsInRegion(region);
        } catch(RemoteException e) {
            // 例外が発生した場合
            e.printStackTrace();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        // 領域退出
        Log.d(TAG, "Exit Region");
        try {
            // レンジング停止
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch(RemoteException e) {
            // 例外が発生した場合
            e.printStackTrace();
        }
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        // 領域に対する状態が変化
        Log.d(TAG, "Determine State: " + i);
    }
}
