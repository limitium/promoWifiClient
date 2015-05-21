package me.loc2.loc2me.core.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.models.WifiInfo;

public class WifiScanService extends BroadcastReceiver {

    @Inject
    protected Bus eventBus;
    @Inject
    WifiManager wifiManager;


    public void register(OfferCheckBackgroundService offerCheckBackgroundService) {
        offerCheckBackgroundService.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void unregister(OfferCheckBackgroundService offerCheckBackgroundService) {
        offerCheckBackgroundService.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (ScanResult scanResult : wifiManager.getScanResults()) {
            eventBus.post(new NewWifiNetworkEvent(new WifiInfo(scanResult)));
        }
    }
}
