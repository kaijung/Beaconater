package teamdeveloper.jp.beaconater;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SlideActivity extends Activity {
        // Imageリソース
    int[] images = {
            R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
        };
    Button prev;
    Button next;

    AdapterViewFlipper filpper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        prev = (Button)findViewById(R.id.prevBtn);
        next = (Button)findViewById(R.id.nextBtn);

        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public Object getItem(int position) {
                return images[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // return ImageView
                ImageView imageView = new ImageView(SlideActivity.this);

                imageView.setImageResource(images[position]);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
                ));

                Log.d("position",""+position);
                Log.d("getCount()",""+getCount());

                if(position==getCount()-1){
                    next.setText("アプリを開始");
                    next.setBackgroundColor(Color.BLUE);
                }
                if(position==0){
                    prev.setText("");

                    //prev.setVisibility(View.INVISIBLE);
                }else{
                    prev.setText("戻る");

                    //prev.setVisibility(View.VISIBLE);
                }
                return imageView;
            }
        };

        filpper = (AdapterViewFlipper) findViewById(R.id.avf);
        filpper.setAdapter(adapter);
    }

    // NEXTボタンクリック
    public void clickNext(View source) {
        if(next.getText().equals("アプリを開始")){
            Intent intent = new Intent(SlideActivity.this, MainActivity.class);
            // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
            // created when a user launches the activity manually and it gets launched from here.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SlideActivity.this.startActivity(intent);
        }
        else {
            filpper.showNext();      // 次のコンポーネント
        }

        //filpper.stopFlipping();  // 自動Flip停止
    }
    // PREVボタンクリック
    public void clickPrev(View source) {
        if(next.getText().equals("アプリを開始")){
            next.setText("進む");
            Resources res = getResources();
            next.setBackgroundColor(res.getColor(android.R.color.darker_gray));
        }
        if(prev.getText()==""){

        }else {
            filpper.showPrevious();  // 前のコンポーネント
        }
        //filpper.stopFlipping();  // 自動Flip停止
    }
}
