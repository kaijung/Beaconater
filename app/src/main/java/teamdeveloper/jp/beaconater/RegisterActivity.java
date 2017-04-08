package teamdeveloper.jp.beaconater;

/**
 * 追加するBeaconのActivity用
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmResults;


// ToDo: Realm使ってRegister
// ToDo: 入力が足りないときの禁則処理

public class RegisterActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    TextView mTextview;
    EditText mEdittext;
    Button mRegister;
    Button mCancel;
    RadioButton mInRadio;
    RadioButton mOutRadio;
    RadioGroup mRadioGroup;
    Switch mSwitch;

    String str_radio;
    Boolean b_switch;

    private BeaconDB mBeacon;

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Log.d("AddTtask","登録が押されました");
            if(mEdittext.getText().toString().equals("")==true){
                Snackbar.make(v, "デバイス名を入力して下さい", Snackbar.LENGTH_LONG).show();

            }else {
                addTask();
                finish();
            }
        }
    };

    private View.OnClickListener mOnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //addTask();
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI部品の設定
        mTextview = (TextView) findViewById(R.id.textUUID);
        mEdittext = (EditText) findViewById(R.id.editName);
        mRegister = (Button) findViewById(R.id.Register);
        mRegister.setOnClickListener(mOnDoneClickListener);
        mCancel = (Button) findViewById(R.id.Cancel);
        mCancel.setOnClickListener(mOnCancelClickListener);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch.setOnCheckedChangeListener(this);

        //mSwitch.setOnClickListener(onSwitchClicked);

        //radioGroupとリソースのradioGroupを結び付ける
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        mInRadio = (RadioButton) findViewById(R.id.radioIn);
        mOutRadio = (RadioButton) findViewById(R.id.radioOut);
        //ラジオグループ内のチェックが変更された時に呼び出すコールバックリスナーを登録
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /**
             * onCheckedChanged
             * ラジオボタンがチェックされた時の処理
             */
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //mRadioButtonとチェックが変更された時のチェックボタンを結び付ける
                Log.d("CheckRadioButton",""+checkedId);
                switch(checkedId) {
                    case R.id.radioIn:
                        mInRadio.setChecked(true);
                        Log.d("CheckRadioButton","IN");

                        str_radio="IN";
                        break;
                    case R.id.radioOut:
                        mOutRadio.setChecked(true);
                        Log.d("CheckRadioButton","OUT");
                        str_radio="OUT";
                        break;
                }
                //トースト表示
                //Toast.makeText(MainActivity.this, mRadioButton.getText()+"が選択されました。", Toast.LENGTH_SHORT).show();
            }
        });

        //findViewById(R.id.Register).setOnClickListener(mOnDoneClickListener);
        //findViewById(R.id.Cancel).setOnClickListener(mOnDoneClickListener);


        // EXTRA_TASK から Beacon の id を取得して、 id から Beacon のインスタンスを取得する
        Intent intent = getIntent();
        int beaconId = intent.getIntExtra(MainActivity.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBeacon = realm.where(BeaconDB.class).equalTo("id", beaconId).findFirst();
        realm.close();

        if (mBeacon == null) {
            // 新規作成の場合
/*            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);*/

            //mSwitch.setChecked(true);
            str_radio = "OUT";
            b_switch = true;

        } else {
            // 更新の場合
            mTextview.setText(mBeacon.getUuid());
            mEdittext.setText(mBeacon.getDevice());
            str_radio = mBeacon.getRegion();
            if(str_radio.equals("OUT")){
                mOutRadio.setChecked(true);
                mInRadio.setChecked(false);
            }else{
                mOutRadio.setChecked(false);
                mInRadio.setChecked(true);
            }
            b_switch = mBeacon.getNotify();
            mSwitch.setChecked(b_switch);

            /*
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mBeacon.getDate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            String dateString = mYear + "/" + String.format("%02d",(mMonth + 1)) + "/" + String.format("%02d", mDay);
            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
            mRegister.setText(dateString);
            mTimeButton.setText(timeString);
            */
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("SwtichOnOff",""+isChecked);
        if (isChecked == true) { // ON状態になったとき
            b_switch=true;
            //Toast.makeText(getApplicationContext(), "SwitchがONになりました。", Toast.LENGTH_SHORT).show();
        }else{
            b_switch=false;
        }
    }


    private void addTask() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        if (mBeacon == null) {
            // 新規作成の場合
            mBeacon = new BeaconDB();

            RealmResults<BeaconDB> beaconRealmResults = realm.where(BeaconDB.class).findAll();

            int identifier;
            if (beaconRealmResults.max("id") != null) {
                identifier = beaconRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mBeacon.setId(identifier);
        }

        String uuid = mTextview.getText().toString();
        String device = mEdittext.getText().toString();

        mBeacon.setUuid(uuid);
        mBeacon.setDevice(device);
        mBeacon.setNotify(b_switch);
        mBeacon.setRegion(str_radio);
        Log.d("RealmRegister",uuid+" "+device+" "+b_switch+" "+str_radio+" ");

        realm.copyToRealmOrUpdate(mBeacon);
        realm.commitTransaction();

        realm.close();

        /* 今回はアラームマネージャ関係なしなのでOK
        Intent resultIntent = new Intent(getApplicationContext(), BroadcastReceiverService.class);
        resultIntent.putExtra(MainActivity.EXTRA_TASK, mBeacon.getId());
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                this,
                mBeacon.getId(),
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), resultPendingIntent);
        */
    }
}
