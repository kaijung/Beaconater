package teamdeveloper.jp.beaconater;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

// ToDo : Register Activityに飛ばしたりうんぬん
// ToDo : ServiceとのActivity連携

public class BeaconActivity extends AppCompatActivity {
    private ListView mListView;
    //private BeaconManager mBeaconManager;
    //private static final int PERMISSIONS_REQUEST_CODE = 1;
    private BeaconAdapter mBeaconAdapter;
    private List<String> beaconlist;
    //private static String TAG = "MyApp";

    private BeaconReceiver bReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // ListViewの設定
        mListView = (ListView) findViewById(R.id.list_view2);

        mBeaconAdapter = new BeaconAdapter(this);
        beaconlist = new ArrayList<String>();

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
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

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // タスクを削除する

                return true;
            }
        });

        startService(new Intent(BeaconActivity.this, BeaconService2.class));
    }

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
    }

    // サービスから値を受け取ったら動かしたい内容を書く
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            String message = bundle.getString("message");

            Log.d("BeaconActivity", "Handler" + message);
            //message_tv.setText(message);

        }
    };

    public void reloadListView() {
        // 後で他のクラスに変更する
        //List<String> beaconlist = new ArrayList<String>();
        //beaconlist.add("aaa");
        //beaconlist.add();

        Log.d("reloadListView","ここまで動いているよー");

        mBeaconAdapter.setBeaconList(beaconlist);
        mListView.setAdapter(mBeaconAdapter);
        mBeaconAdapter.notifyDataSetChanged();

    }

    //そのままSetTextするとエラーが起きたのでStackOverflowより
    private void setText(final String name, final String uuid){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                //mTextview.append(value+"\n");
                //mBeacon.add(beacon);

                ///後で有効化するようにする
                beaconlist.add(uuid);
                reloadListView();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
