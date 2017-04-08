package teamdeveloper.jp.beaconater;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

// ToDo : Register Activityに飛ばしたりうんぬん
// ToDo : ServiceとのActivity連携
// ToDo : Beaconがない場合の表示

public class BeaconActivity extends AppCompatActivity {
    private ListView mListView;
    //private BeaconManager mBeaconManager;
    //private static final int PERMISSIONS_REQUEST_CODE = 1;
    private BeaconAdapter mBeaconAdapter;
    private List<String> beaconlist;
    //private static String TAG = "MyApp";
    private BeaconDB mBeacon;
    private List<BeaconDB> mBeaconList;
    private Realm mRealm;

    private BeaconReceiver bReceiver;
    private IntentFilter intentFilter;
    private RealmResults<BeaconDB> beaconRealmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // Realmの設定
        mRealm = Realm.getDefaultInstance();
        mBeaconAdapter = new BeaconAdapter(BeaconActivity.this);
        beaconRealmResults = mRealm.where(BeaconDB.class).findAllSorted("id", Sort.DESCENDING);
        // 上記の結果を、TaskList としてセットする
        //mBeaconAdapter.setBeaconList(mRealm.copyFromRealm(beaconRealmResults));
        //mBeaconList = new List<BeaconDB>;
        mBeaconList = new ArrayList<>();
        mBeacon = new BeaconDB();
        mBeacon.setId(0);
        mBeacon.setUuid("");
        mBeacon.setDevice("");
        mBeacon.setRegion("");
        mBeacon.setNotify(false);

        // ListViewの設定
        mListView = (ListView) findViewById(R.id.list_view2);

        //mBeaconAdapter = new BeaconAdapter(this);
        //beaconlist = new ArrayList<String>();

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる

                BeaconDB beacon = (BeaconDB) parent.getAdapter().getItem(position);
                Intent intent = new Intent( BeaconActivity.this, RegisterActivity.class );
                intent.putExtra(MainActivity.EXTRA_TASK+"UUID", beacon.getUuid());
                intent.putExtra(MainActivity.EXTRA_TASK, parent.getCount());

//                intent.putExtra(EXTRA_TASK, bapp.getId());

                startActivity(intent);
            }
        });

        Context context = this;
        Intent update_service = new Intent(context , BeaconService.class);
        startService(update_service);
        bReceiver = new BeaconReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE_ACTION");
        registerReceiver(bReceiver, intentFilter);

        bReceiver.registerHandler(updateHandler);
        //reloadListView();

        startService(new Intent(BeaconActivity.this, BeaconService2.class));
    }

    /*
    public void setBeacon(final String uuid) {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Log.d("run","ここまで動いているよー");
                //mTextview.append(value+"\n");
                //mBeacon.add(beacon);

                if(beaconlist==null){
                    mListView = (ListView) findViewById(R.id.list_view2);
                    mBeaconAdapter = new BeaconAdapter(BeaconActivity.this);
                    beaconlist = new ArrayList<String>();
                }
                ///後で有効化するようにする
                beaconlist.add(uuid);
                reloadListView();
            }
        });
        //beaconlist.add(uuid);
        //reloadListView();
        //return mListView;
    }*/

    // サービスから値を受け取ったら動かしたい内容を書く
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            String message = bundle.getString("message");
            String uuid = bundle.getString("message");

            Log.d("BeaconActivity", "Handler : " + message);
            ///後で有効化するようにする
            Log.d("BeaconActivity",""+beaconRealmResults.size());

            if (mBeaconList.size() == 0){
                 setText("未登録",uuid);
             } else {
                Boolean mBoolean = false;
                for (int i = 0; i < mBeaconList.size(); i++) {
                    if (mBeaconList.get(i).getUuid().equals(uuid)) {
                        Log.d("Beaconater", "ちゃんと見てます1");
                        mBoolean = true;
                    }
                }
                if(mBoolean==false&&beaconRealmResults.size()>0) {
                    for (int j = 0; j < beaconRealmResults.size(); j++) {
                        if (beaconRealmResults.get(j).getUuid().equals(uuid)) {
                            Log.d("Beaconater", "ちゃんと見てます2");
                            mBoolean = true;
                        }
                    }
                }
                if(!mBoolean) {
                    setText("未登録",uuid);
                }
            }
            //mBeacon.setUuid(uuid);
            //mBeaconList.add(mBeacon);
            //reloadListView();
            //message_tv.setText(message);

        }
    };

    public void reloadListView() {
        // 後で他のクラスに変更する
        //List<String> beaconlist = new ArrayList<String>();
        //beaconlist.add("aaa");
        //beaconlist.add();

        Log.d("reloadListView","動いている");

        mBeaconAdapter.setBeaconList(mBeaconList);
        mListView.setAdapter(mBeaconAdapter);
        mBeaconAdapter.notifyDataSetChanged();

    }

    //そのままSetTextするとエラーが起きたのでStackOverflowより
    private void setText(final String name, final String uuid){
        /*
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                //mTextview.append(value+"\n");
                //mBeacon.add(beacon);

*/
                ///後で有効化するようにする
                mBeacon.setDevice(name);
                mBeacon.setUuid(uuid);
                mBeaconList.add(mBeacon);
                //beaconlist.add(uuid);
                reloadListView();
        /*
            }
        });
        */
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBeaconAdapter = new BeaconAdapter(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //unregisterReceiver(bReceiver);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(bReceiver);
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
