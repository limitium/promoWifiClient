package me.loc2.loc2me.core.models;

import com.google.common.collect.ImmutableList;

public class WifiRequest {

    final WifiInfo wifi;
    final ImmutableList<String> filters;
    final String mac;

    public WifiRequest(WifiInfo wifi, ImmutableList<String> filters, String mac) {
        this.wifi = wifi;
        this.filters = filters;
        this.mac = mac;
    }

    public WifiInfo getWifi() {
        return wifi;
    }

    public ImmutableList<String> getFilters() {
        return filters;
    }

    public String getMac() {
        return mac;
    }
}
