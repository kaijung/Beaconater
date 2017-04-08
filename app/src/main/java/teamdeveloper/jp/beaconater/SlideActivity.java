package teamdeveloper.jp.beaconater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SlideActivity extends Activity {
        // Imageリソース
    int[] images = {
            R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
        };

    AdapterViewFlipper filpper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

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

                return imageView;
            }
        };

        filpper = (AdapterViewFlipper) findViewById(R.id.avf);
        filpper.setAdapter(adapter);
    }

    // NEXTボタンクリック
    public void clickNext(View source) {
        filpper.showNext();      // 次のコンポーネント
        //filpper.stopFlipping();  // 自動Flip停止
    }
    // PREVボタンクリック
    public void clickPrev(View source) {
        filpper.showPrevious();  // 前のコンポーネント
        //filpper.stopFlipping();  // 自動Flip停止
    }
}
