package teamdeveloper.jp.beaconater;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BeaconDB  extends RealmObject implements Serializable {
    private String device; // タイトル
    private String uuid; // 内容
    private Boolean notify; // Notify ON/OFF設定
    private String region; // Region

    // id をプライマリーキーとして設定
    @PrimaryKey
    private int id;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}