package me.loc2.loc2me.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.loc2.loc2me.core.OfferLoaderService;
import me.loc2.loc2me.core.OfferStorageService;
import me.loc2.loc2me.core.TimerService;
import me.loc2.loc2me.core.WifiScanService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, OfferLoaderService.class));
        context.startService(new Intent(context, TimerService.class));
        context.startService(new Intent(context, WifiScanService.class));
        context.startService(new Intent(context, OfferStorageService.class));
    }
}
