package teamdeveloper.jp.beaconater;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

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
// Todo : MainActivityは登録されたBeaconだけが表示されるクラス
// Todo : BeaconAdapterはListView用
// ToDo : Subtitle用も設定しないと

public class MainActivity extends Activity {
    //TextView mTextview;
    private ListView mListView;
    private BeaconManager mBeaconManager;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private BeaconAdapter mBeaconAdapter;
    //private List<String> beaconlist;
    //Sharedプリファレンス
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;


    private static String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mListView = (ListView) findViewById(R.id.list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */


        // ListViewの設定
        mListView = (ListView) findViewById(R.id.list_view);

        // 保存
        preference = getSharedPreferences("Data", MODE_PRIVATE);
        editor = preference.edit();
        boolean dataBoolean = preference.getBoolean("DataBoolean", false);
        //作成途中 : data.getStringSet()

        if(preference.getBoolean("DataBoolean", false)==false) {
            Log.d("AppLaunchChecker","はじめてアプリを起動した");

            Intent intent = new Intent(this, StartActivity.class);
            // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
            // created when a user launches the activity manually and it gets launched from here.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        }
        else {
            // AppLaunchCheckerはSharedPreferrenceを使った初回起動か否かを取得するもの
            // if(AppLaunchChecker.hasStartedFromLauncher(this)){
            Log.d("AppLaunchChecker", "2回目以降");

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
            //startService(new Intent(MainActivity.this, BeaconService2.class));

        }

        // ListViewをタップしたときの処理
        // 編集画面へ遷移
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
        // 別Activityに飛ばして登録画面を出す予定
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, BeaconActivity.class);
                // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
                // created when a user launches the activity manually and it gets launched from here.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);

            }
        });

        editor.putBoolean("DataBoolean", true);
        editor.commit();

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

    //そのままSetTextするとエラーが起きたのでStackOverflowより
    private void setText(final String name, final String uuid){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                //mTextview.append(value+"\n");
                //mBeacon.add(beacon);

                ///後で有効化するようにする
                //beaconlist.add(uuid);
                //reloadListView();
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
