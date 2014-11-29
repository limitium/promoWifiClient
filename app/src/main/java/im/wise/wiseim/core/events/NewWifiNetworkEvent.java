package im.wise.wiseim.core.events;

import android.net.wifi.ScanResult;

import im.wise.wiseim.core.models.WifiInfo;

public class NewWifiNetworkEvent {
    private WifiInfo wifi;

    public NewWifiNetworkEvent(WifiInfo wifi) {
        this.wifi = wifi;
    }

    public WifiInfo getWifi() {
        return wifi;
    }
}
