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

// ToDo : BroadcastReceiverはAlarmManager用だったので何かしらSendReceiverしてあげる必要がある。
// Beaconが見つかったときに出すような形にしてしまえばいい。


public class BeaconNotification extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BeaconNotification","Notification生成");
        Bundle bundle = intent.getExtras();
        String message = bundle.getString("Registered");

        // 通知の設定を行う
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        //Beacon関係の情報をここにいれる
        // タスクの情報を設定する
        builder.setTicker(message+"検出"); // 5.0以降は表示されない
        builder.setContentTitle(message+"検出");
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
