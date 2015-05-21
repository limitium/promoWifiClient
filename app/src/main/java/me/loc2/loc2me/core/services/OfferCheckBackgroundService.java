package me.loc2.loc2me.core.services;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.events.LoadedOfferEvent;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.util.Ln;

import static me.loc2.loc2me.core.Constants.Notification.SCAN_NOTIFICATION_ID;
import static me.loc2.loc2me.core.Constants.Notification.TIMER_NOTIFICATION_ID;

public class OfferCheckBackgroundService extends Service {
    @Inject
    protected Bus eventBus;
    @Inject
    NotificationManager notificationManager;

    @Inject
    protected OfferLoaderService offerLoaderService;
    @Inject
    protected WifiScanService wifiScanService;
    private boolean started;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.inject(this);

        eventBus.register(this);

        wifiScanService.register(this);
        offerLoaderService.register();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);

        notificationManager.cancel(SCAN_NOTIFICATION_ID);

        offerLoaderService.unregister();
        wifiScanService.unregister(this);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!started) {

            started = true;

            // Run as foreground service: http://stackoverflow.com/a/3856940/5210
            // Another example: https://github.com/commonsguy/cw-android/blob/master/Notifications/FakePlayer/src/com/commonsware/android/fakeplayerfg/PlayerService.java
            startForeground(TIMER_NOTIFICATION_ID, getNotification(getString(R.string.bg_offer_checker)));
            notificationManager.cancel(TIMER_NOTIFICATION_ID);
        }

        return START_NOT_STICKY;
    }

    /**
     * Creates a notification to show in the notification bar
     *
     * @param message the message to display in the notification bar
     * @return a new {@link android.app.Notification}
     */
    private Notification getNotification(String message) {
        final Intent i = new Intent(this, OfferCheckBackgroundService.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_stat_ab_notification)
                .setContentText(message)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .getNotification();
    }

    @Subscribe
    public void onLoadedOfferEvent(LoadedOfferEvent loadedOfferEvent) {
        Ln.i("New offer: " + loadedOfferEvent.getOffer().getId());
        //@todo: check offer in cache or deleted
        eventBus.post(new NewOfferEvent(loadedOfferEvent.getOffer()));
    }

    @Subscribe
    public void onNewWifiNetworkEvent(NewWifiNetworkEvent wifiNetworkEvent) {
        Ln.i("New network: " + wifiNetworkEvent.getWifiInfo().getName());
        //@todo: check wifi in cache
        offerLoaderService.loadWifiOffers(wifiNetworkEvent.getWifiInfo());
    }
}
