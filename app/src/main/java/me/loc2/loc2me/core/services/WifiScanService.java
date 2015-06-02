package me.loc2.loc2me.core.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Iterator;

import javax.inject.Inject;

import me.loc2.loc2me.core.events.ForceReloadEvent;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.util.Ln;

public class WifiScanService extends BroadcastReceiver {

    @Inject
    protected Bus eventBus;
    @Inject
    WifiManager wifiManager;
    private HashMap<String, Long> checked = new HashMap<String, Long>();
    private long cachedTime = 1000 * 60 * 60 * 3;
    private boolean force = false;

    public boolean scan() {
        return wifiManager.startScan();
    }

    public void register(OfferCheckBackgroundService offerCheckBackgroundService) {
        offerCheckBackgroundService.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        eventBus.register(this);
    }

    public void unregister(OfferCheckBackgroundService offerCheckBackgroundService) {
        offerCheckBackgroundService.unregisterReceiver(this);
        eventBus.unregister(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long currentTimeMillis = System.currentTimeMillis();
        cleanCache(currentTimeMillis);
        for (ScanResult scanResult : wifiManager.getScanResults()) {
            String wifiKey = scanResult.SSID + scanResult.BSSID;
            if (!checked.containsKey(wifiKey)) {
                checked.put(wifiKey, currentTimeMillis);
                eventBus.post(new NewWifiNetworkEvent(new WifiInfo(scanResult)));
            }
        }
    }

    private void cleanCache(long currentTimeMillis) {
        Iterator<String> iterator = checked.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (force || currentTimeMillis == 0 || currentTimeMillis - checked.get(key) > cachedTime) {
                iterator.remove();
            }
        }
        force = false;
    }

    @Subscribe
    public void onForceReloadEvent(ForceReloadEvent forceReloadEvent) {
        force = true;
        scan();
    }
}
