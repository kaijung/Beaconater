package teamdeveloper.jp.beaconater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import static teamdeveloper.jp.beaconater.BeaconNotification.UUID_KEY;

public class BeaconReceiver extends BroadcastReceiver {
    public static Handler handler;

    //UIスレッドで飛んで来るのでHandlerいらない
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String message = bundle.getString(UUID_KEY);

        if(handler !=null){
            Message msg = new Message();

            Bundle data = new Bundle();
            data.putString(UUID_KEY, message);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    }

    /**
     * メイン画面の表示を更新
     */
    public void registerHandler(Handler locationUpdateHandler) {
        handler = locationUpdateHandler;
    }



}
