package me.loc2.loc2me.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.loc2.loc2me.core.services.OfferCheckBackgroundService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, OfferCheckBackgroundService.class));
    }
}
