package teamdeveloper.jp.beaconater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BeaconAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<BeaconDB> mBeaconList;

    public BeaconAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBeaconList(List<BeaconDB> beaconList) {
        mBeaconList = beaconList;
    }

    @Override
    public int getCount() {
        //return mBeaconList.size();
        return mBeaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeaconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mBeaconList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        Log.d("Adapter",mBeaconList.get(position).getDevice());
        Log.d("Adapter",mBeaconList.get(position).getUuid());

        // 後でBeaconクラスから情報を取得するように変更する
        textView1.setText(mBeaconList.get(position).getDevice());

        String notify;
        String range;
        if(mBeaconList.get(position).getNotify()){
            notify = "ON";
        }else{
            notify = "OFF";
        }
        if(mBeaconList.get(position).getRegion().equals("")){
            textView2.setText(mBeaconList.get(position).getUuid());
        }else{
            textView2.setText(mBeaconList.get(position).getUuid()+" 通知:"+notify+" "+mBeaconList.get(position).getRegion()+"時");
        }


        return convertView;
    }
}
