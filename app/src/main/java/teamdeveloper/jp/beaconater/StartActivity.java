package teamdeveloper.jp.beaconater;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // ボタンの取得
        Button skip = (Button)findViewById(R.id.Skip);
        Button tutorial = (Button)findViewById(R.id.Tutorial);

        // FloatingActionの処理を入れる。
        // 別Activityに飛ばして検知画面を出す予定
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "skipします", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
                // created when a user launches the activity manually and it gets launched from here.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                StartActivity.this.startActivity(intent);
            }
        });


        // FloatingActionの処理を入れる。
        // 別Activityに飛ばして検知画面を出す予定
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "tutorialにします", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
                // created when a user launches the activity manually and it gets launched from here.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                StartActivity.this.startActivity(intent);
            }
        });
    }
}
