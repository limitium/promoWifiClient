package me.loc2.loc2me.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.util.Ln;
import me.loc2.loc2me.util.SafeAsyncTask;
import retrofit.RetrofitError;


import static me.loc2.loc2me.core.Constants.Notification.INFO_NOTIFICATION_ID;

public class OfferLoaderService extends Service {

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
        loadWifiOffers(wifiNetworkEvent.getWifiInfo());
    }

    private void loadWifiOffers(final WifiInfo wifi) {
        new SafeAsyncTask<List<Offer>>() {
            public List<Offer> call() throws Exception {
                return bootstrapService.getWifiOffers(wifi.getName());
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
            public void onSuccess(final List<Offer> offers) {
                for (Offer offer : offers) {
                    onWifiInfo(offer);
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                //do nothing
            }
        }.execute();
    }

    private void onWifiInfo(Offer offer) {
        long currentTimeMillis = System.currentTimeMillis();
        cleanCache(currentTimeMillis);
        if (!shown.containsKey(offer.getId())) {
            wifis++;
            updateNotification("Wifis found:" + wifis);
            eventBus.post(new NewOfferEvent(offer));
            shown.put(offer.getId(), currentTimeMillis);
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
        final Intent i = new Intent(this, OfferLoaderService.class);

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
