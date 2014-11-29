package im.wise.wiseim.core.models;

import android.net.wifi.ScanResult;


public class WifiInfo {
    ScanResult scanResult;

    public WifiInfo(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public String getSSID() {
        return scanResult.SSID;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }
}
