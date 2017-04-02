package teamdeveloper.jp.beaconater;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

// 新規プロジェクトを作成してバックグラウンドのみの処理にしたものを試す
// didEnterのみしか検出

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    BeaconManager mBeaconManager;

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private BeaconAdapter mBeaconAdapter;

    //任意のBeaconを指定するのに便利
    private Region mRegion;

    //TextView mTextview;
    private ListView mListView;
    //TextView mTextview1;
    //TextView mTextview2;

    private List<String> beaconlist;

    ArrayList<Beacon> mBeacon = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                Log.d("ANDROID", "許可されている");
                // サービス起動
                //startService(new Intent(MainActivity.this, BeaconService.class));
            } else {
                Log.d("ANDROID", "許可されていない");
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
            }
        }

        /**
         * 2017/03/28 書き始め
         */
        // インスタンス化
        mBeaconManager = BeaconManager.getInstanceForApplication(this);


        // AltBeacon以外の端末をBeaconフォーマットに変換
        String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1);
                */
        // ListViewの設定
        mBeaconAdapter = new BeaconAdapter(MainActivity.this);
        mListView = (ListView) findViewById(R.id.list_view);
        beaconlist = new ArrayList<String>();

        reloadListView();

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
            }
        });

        //reloadListView();

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // タスクを削除する

                return true;
            }
        });

        // FloatingActionの処理を入れる。
        // 別Activityに飛ばして検知画面を出す予定
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRegion = new Region("", null, null, null);
        //new Region(null, null, null, null)
        // パーミッションの許可状態を確認する


        //mTextview = (TextView) findViewById(R.id.text_view);
        //setContentView(mTextview);
        mListView = (ListView) findViewById(R.id.list_view);
        mBeaconManager.bind(this);
    }

    private void reloadListView() {

        // 後で他のクラスに変更する
        //List<String> beaconlist = new ArrayList<String>();
        //beaconlist.add("aaa");
        //beaconlist.add("bbb");
        //beaconlist.add("ccc");

        mBeaconAdapter.setBeaconList(beaconlist);
        mListView.setAdapter(mBeaconAdapter);
        mBeaconAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ANDROID", "許可された");
                } else {
                    Log.d("ANDROID", "許可されなかった");
                }
                break;
            default:
                break;
        }
    }


    // Beaconサービスの開始
    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.addMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                // 領域への入場を検知
                // レンジングの開始
                try {
                    mBeaconManager.startRangingBeaconsInRegion(mRegion);
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
        });

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
                    if(beacon.getDistance()<5) {
                        if (mBeacon == null) {
                            setText(beacon, str);
                            //mListView.set(0,str);
                        } else if (mBeacon != null) {
                            Boolean mBoolean = false;
                            for(int i = 0; i < mBeacon.size();i++){
                                if(mBeacon.get(i).getId1().equals(beacon.getId1())==true){
                                    mBoolean = true;
                                }
                            }
                            if(mBoolean == false) {
                                setText(beacon, str);
                            }
                        }
                    }
                }
            }
        });
        reloadListView();

        try {
            // ビーコン情報の監視を開始
            mBeaconManager.startMonitoringBeaconsInRegion(mRegion);
            //mBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //そのままSetTextするとエラーが起きたのでStackOverflowより
    private void setText(final Beacon beacon, final String value){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                //mTextview.append(value+"\n");
                //mBeacon.add(beacon);
                beaconlist.add(value);
                reloadListView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBeaconManager.bind(this); // サービスの開始
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBeaconManager.unbind(this); // サービスの停止
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
