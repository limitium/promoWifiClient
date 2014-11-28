package im.wise.wiseim.core.events;

import android.net.wifi.ScanResult;

public class NewWifiNetworkEvent {
    private ScanResult wifi;

    public NewWifiNetworkEvent(ScanResult wifi) {
        this.wifi = wifi;
    }

    public ScanResult getWifi() {
        return wifi;
    }
}
