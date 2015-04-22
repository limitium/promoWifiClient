package me.loc2.loc2me.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Iterator;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.events.TimerTickEvent;
import me.loc2.loc2me.core.events.WifiScannedEvent;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.core.receivers.WifiReceiver;
import me.loc2.loc2me.util.Ln;

import static me.loc2.loc2me.core.Constants.Notification.SCAN_NOTIFICATION_ID;

public class WifiScanService extends Service {

    @Inject
    protected Bus eventBus;
    @Inject
    WifiManager wifiManager;
    @Inject
    NotificationManager notificationManager;
    @Inject
    WifiReceiver wifiReceiver;

    private boolean started;
    private int scans = 0;
    private HashMap<String, Long> checked = new HashMap<String, Long>();
    private int cachedTime = 1000 * 60 * 60;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.inject(this);

        // Register the bus so we can send notifications.
        eventBus.register(this);

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifiManager.startScan();
    }

    @Override
    public void onDestroy() {

        // Unregister bus, since its not longer needed as the service is shutting down
        eventBus.unregister(this);

        unregisterReceiver(wifiReceiver);

        notificationManager.cancel(SCAN_NOTIFICATION_ID);

        Ln.d("Service has been destroyed");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!started) {
            started = true;
            // Run as foreground service: http://stackoverflow.com/a/3856940/5210
            // Another example: https://github.com/commonsguy/cw-android/blob/master/Notifications/FakePlayer/src/com/commonsware/android/fakeplayerfg/PlayerService.java
            startForeground(SCAN_NOTIFICATION_ID, getNotification(getString(R.string.timer_running)));
        }

        return START_NOT_STICKY;
    }


    @Subscribe
    public void onWifiScannedEvent(WifiScannedEvent wifiEvent) {

        long currentTimeMillis = System.currentTimeMillis();
        cleanCache(currentTimeMillis);

        for (ScanResult scanResult : wifiManager.getScanResults()) {
            if (!checked.containsKey(scanResult.SSID)) {
                checked.put(scanResult.SSID, currentTimeMillis);
                scans++;
                eventBus.post(new NewWifiNetworkEvent(new WifiInfo(scanResult)));
            }
        }
        updateNotification("scans:" + scans);
    }

    private void cleanCache(long currentTimeMillis) {
        Iterator<String> iterator = checked.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (currentTimeMillis == 0 || currentTimeMillis - checked.get(key) > cachedTime) {
                iterator.remove();
            }
        }
    }

    private void updateNotification(String message) {
        notificationManager.notify(SCAN_NOTIFICATION_ID, getNotification(message));
    }

    /**
     * Creates a notification to show in the notification bar
     *
     * @param message the message to display in the notification bar
     * @return a new {@link android.app.Notification}
     */
    private Notification getNotification(String message) {
        final Intent i = new Intent(this, WifiScanService.class);

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
}
