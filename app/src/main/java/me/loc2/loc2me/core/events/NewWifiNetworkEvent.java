package me.loc2.loc2me.core.events;

import me.loc2.loc2me.core.models.WifiInfo;

public class NewWifiNetworkEvent {
    private WifiInfo wifi;

    public NewWifiNetworkEvent(WifiInfo wifi) {
        this.wifi = wifi;
    }

    public WifiInfo getWifiInfo() {
        return wifi;
    }
}
