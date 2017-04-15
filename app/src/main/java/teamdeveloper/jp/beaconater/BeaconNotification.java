package teamdeveloper.jp.beaconater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static teamdeveloper.jp.beaconater.BroadcastReceiverService.BROADCAST_DIDENTER;

// ToDo : BroadcastReceiverはAlarmManager用だったので何かしらSendReceiverしてあげる必要がある。
// Beaconが見つかったときに出すような形にしてしまえばいい。
// ToDo : Notificationが作れていない
// ToDo : Register後はMainActivityに飛ばす
// ToDO : 位置情報などを検討中
// ToDo : 画像差し替え

public class BeaconNotification extends BroadcastReceiver{

    private Realm mRealm;
    private RealmResults<BeaconDB> beaconRealmResults;
    public static final String UUID_KEY = "message";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String uuid = bundle.getString(UUID_KEY);
        //String uuid = bundle.getString("uuid");

        mRealm = Realm.getDefaultInstance();
        beaconRealmResults = mRealm.where(BeaconDB.class).findAllSorted("id", Sort.DESCENDING);

        if (beaconRealmResults.size() == 0){
            //Log.d("BeaconNotification","Notification生成しない");

        }
        else{
            for (int j = 0; j < beaconRealmResults.size(); j++) {
                //Log.d("Realm : ",beaconRealmResults.get(j).getNotify()+"");
                //Log.d("Realm : ",beaconRealmResults.get(j).getUuid());
                //Log.d("UUID : ", uuid+"");

                if (beaconRealmResults.get(j).getNotify()==true&&beaconRealmResults.get(j).getUuid().equals(uuid)==true){
                    Log.d("BeaconNotification","Notification");

                    // 通知の設定を行う
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setSmallIcon(R.mipmap.icon_trans);
                    builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
                    builder.setWhen(System.currentTimeMillis());
                    builder.setDefaults(Notification.DEFAULT_ALL);
                    builder.setAutoCancel(true);

                    //Beacon関係の情報をここにいれる
                    // タスクの情報を設定する
                    builder.setTicker(beaconRealmResults.get(j).getDevice()+"からのメッセージ"); // 5.0以降は表示されない
                    builder.setContentTitle(beaconRealmResults.get(j).getDevice()+"からのメッセージ");
                    builder.setContentText("鍵を閉め忘れていませんか");

                    // 通知をタップしたらアプリを起動するようにする
                    Intent startAppIntent = new Intent(context, MainActivity.class);
                    startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
                    builder.setContentIntent(pendingIntent);

                    // 通知を表示する
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());
                }
            }
        }
    }
}
