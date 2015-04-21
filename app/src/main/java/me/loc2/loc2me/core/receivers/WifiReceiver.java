package me.loc2.loc2me.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import me.loc2.loc2me.core.events.WifiScannedEvent;


public class WifiReceiver extends BroadcastReceiver {
    @Inject protected Bus eventBus;

    @Override
    public void onReceive(Context context, Intent intent) {
        produceWifiScannedEvent();
    }

    public void produceWifiScannedEvent() {
        eventBus.post(new WifiScannedEvent());
    }
}
