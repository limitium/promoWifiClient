package me.loc2.loc2me.core.models;

import android.net.wifi.ScanResult;

import me.loc2.loc2me.util.Strings;


public class WifiInfo {
    ScanResult scanResult;

    public WifiInfo(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public String getName() {
        return scanResult.SSID;
    }

    public String getMAC() {
        return scanResult.BSSID;
    }

    public ScanResult getScanResult() {

        return scanResult;
    }
}
