package im.wise.wiseim.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import im.wise.wiseim.Injector;
import im.wise.wiseim.R;
import im.wise.wiseim.core.events.NewWifiNetworkEvent;
import im.wise.wiseim.core.events.NewWiseEvent;
import im.wise.wiseim.core.models.WifiInfo;
import im.wise.wiseim.core.models.Wise;
import im.wise.wiseim.util.Ln;
import im.wise.wiseim.util.SafeAsyncTask;
import retrofit.RetrofitError;


import static im.wise.wiseim.core.Constants.Notification.INFO_NOTIFICATION_ID;
import static im.wise.wiseim.core.Constants.Notification.SCAN_NOTIFICATION_ID;

public class WiseLoaderService extends Service {

    @Inject
    protected Bus eventBus;
    @Inject
    protected NotificationManager notificationManager;
    @Inject
    protected BootstrapService bootstrapService;
    private boolean started;
    private int wifis;
    private HashMap<BigInteger, Long> shown = new HashMap<BigInteger, Long>();
    private int shownTime = 1000 * 60 * 60 * 24 * 7;


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

    }

    @Override
    public void onDestroy() {

        // Unregister bus, since its not longer needed as the service is shutting down
        eventBus.unregister(this);

        Ln.d("Service has been destroyed");
        notificationManager.cancel(INFO_NOTIFICATION_ID);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!started) {
            started = true;
            // Run as foreground service: http://stackoverflow.com/a/3856940/5210
            // Another example: https://github.com/commonsguy/cw-android/blob/master/Notifications/FakePlayer/src/com/commonsware/android/fakeplayerfg/PlayerService.java
            startForeground(INFO_NOTIFICATION_ID, getNotification("Winter is coming!"));
        }

        return START_NOT_STICKY;
    }

    @Subscribe
    public void onNewWifiNetworkEvent(NewWifiNetworkEvent wifiNetworkEvent) {
        loadWifiNetworkInfo(wifiNetworkEvent.getWifi());
    }

    private void loadWifiNetworkInfo(final WifiInfo wifi) {
        new SafeAsyncTask<List<Wise>>() {
            public List<Wise> call() throws Exception {
                return bootstrapService.getWifiInfos(wifi.getSSID());
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                // Retrofit Errors are handled inside of the {
                if (!(e instanceof RetrofitError)) {
                    final Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if (cause != null) {
                        //do nothing
                    }
                }
            }

            @Override
            public void onSuccess(final List<Wise> wises) {
                for (Wise wise : wises) {
                    onWifiInfo(wise);
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                //do nothing
            }
        }.execute();
    }

    private void onWifiInfo(Wise wise) {
        long currentTimeMillis = System.currentTimeMillis();
        cleanCache(currentTimeMillis);
        if (!shown.containsKey(wise.getId())) {
            wifis++;
            updateNotification("Wifis found:" + wifis);
            eventBus.post(new NewWiseEvent(wise));
            shown.put(wise.getId(), currentTimeMillis);
        }
    }

    private void cleanCache(long currentTimeMillis) {
        Iterator<BigInteger> iterator = shown.keySet().iterator();
        while (iterator.hasNext()) {
            BigInteger key = iterator.next();
            if (currentTimeMillis == 0 || currentTimeMillis - shown.get(key) > shownTime) {
                iterator.remove();
            }
        }
    }

    private void updateNotification(String message) {
        notificationManager.notify(INFO_NOTIFICATION_ID, getNotification(message));
    }

    /**
     * Creates a notification to show in the notification bar
     *
     * @param message the message to display in the notification bar
     * @return a new {@link android.app.Notification}
     */
    private Notification getNotification(String message) {
        final Intent i = new Intent(this, WiseLoaderService.class);

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
