package teamdeveloper.jp.beaconater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BeaconAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<String> mBeaconList;

    public BeaconAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBeaconList(List<String> beaconList) {
        mBeaconList = beaconList;
    }

    @Override
    public int getCount() {
        return mBeaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeaconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        // 後でBeaconクラスから情報を取得するように変更する
        textView1.setText(mBeaconList.get(position));
        textView2.setText(mBeaconList.get(position));

        return convertView;
    }
}
