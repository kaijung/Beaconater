package teamdeveloper.jp.beaconater;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeaconService2 extends Service implements BootstrapNotifier {

    private BeaconManager mBeaconManager;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private BeaconAdapter mBeaconAdapter;
    //任意のBeaconを指定するのに便利
    private Region mRegion;
    private RegionBootstrap regionBootstrap;

    //TextView mTextview1;
    //TextView mTextview2;
    private ShowcaseView view;
    private List<String> beaconlist;
    ArrayList<Beacon> mBeacon = new ArrayList<>();
    MainActivity mMain;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BeaconService2","起動しました");

        /**
         * 2017/03/28 書き始め
         */
        // インスタンス化
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        // AltBeacon以外の端末をBeaconフォーマットに変換
        String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        mBeaconManager.setForegroundBetweenScanPeriod(1000);
        mBeaconManager.setBackgroundBetweenScanPeriod(1000);

        mRegion = new Region("", null, null, null);
        //new Region(null, null, null, null)
        //mTextview = (TextView) findViewById(R.id.text_view);
        //setContentView(mTextview);
        //mBeaconManager.bind(BeaconService2.this);

//        mMain = new MainActivity();
//        mBeaconAdapter = new BeaconAdapter(mMain);

        regionBootstrap = new RegionBootstrap(this, mRegion);

        //mBeaconManager.bind(BeaconService2.this);
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // 検出したビーコンの情報を全部Logに書き出す
                for(Beacon beacon : beacons) {
                    // ログの出力
                    String str = ("UUID:" + beacon.getId1() + ", major:"
                            + beacon.getId2()+ ", minor:" + beacon.getId3()
                            + ", Distance:" + beacon.getDistance()+ ",RSSI" + beacon.getRssi());
                    Log.d("Beacon", str);

                    // ServiceとActivityの連携部分をやらないといけない。
                    BeaconActivity mBeacon = new BeaconActivity();
                    if(beacon.getId1()!=null) mBeacon.setBeacon(""+beacon.getId1());


                    /*
                    if(beacon.getDistance()<5) {
                        //Log.d("Beaconater",""+mBeaconAdapter.getCount());
                        if (mBeaconAdapter.getCount() == 0) {
                            setText(str,""+beacon.getId1());
                            //Log.d("Beaconater","Nulldでした");

                            //mListView.set(0,str);
                        } else if (mBeaconAdapter.getCount() > 0) {
                            Boolean mBoolean = false;
                            for(int i = 0; i < mBeaconAdapter.getCount();i++){
                                if(mBeaconAdapter.getItem(i).equals(""+beacon.getId1())==true){
                                    Log.d("Beaconater","ちゃんと見てます");

                                    mBoolean = true;
                                }
                            }
                            if(mBoolean == false) {
                                setText(str,""+beacon.getId1());
                            }
                        }
                    }
                    */
                }
            }
        });
        //reloadListView();
    }

    //そのままSetTextするとエラーが起きたのでStackOverflowより
    private void setText(final String name, final String uuid){
        //mTextview.append(value+"\n");
        //mBeacon.add(beacon);

        ///後で有効化するようにする
        beaconlist.add(uuid);
        mBeaconAdapter.setBeaconList(beaconlist);
        //mListView.setAdapter(mBeaconAdapter);
        mBeaconAdapter.notifyDataSetChanged();
        //mMain.reloadListView(beaconlist);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
    public void didEnterRegion(Region region) {
        // 領域侵入
        Log.d("BeaconService2", "Enter Region");
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        */
        Intent resultIntent = new Intent(getApplicationContext(), BeaconNotification.class);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setSmallIcon(R.mipmap.icon);

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(0, builder.build());

        // 領域への入場を検知
        // レンジングの開始
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        // 領域からの退場を検知
        // レンジングの停止
        try {
            mBeaconManager.stopRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.i("Beaconator", "I have just switched from seeing/not seeing beacons: "+state);
    }


    /*
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

    */
}
