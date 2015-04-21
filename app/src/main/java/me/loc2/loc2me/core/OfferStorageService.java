package me.loc2.loc2me.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.events.GetOffersEvent;
import me.loc2.loc2me.core.events.LoadedOffersEvent;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.util.Ln;

import static me.loc2.loc2me.core.Constants.Notification.STORAGE_NOTIFICATION_ID;

public class OfferStorageService extends Service {

    @Inject
    protected Bus eventBus;
    @Inject
    NotificationManager notificationManager;
    private boolean started;
    private HashMap<BigInteger, Offer> saved = new HashMap<BigInteger, Offer>();

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
        loadOffers();
    }

    @Override
    public void onDestroy() {

        // Unregister bus, since its not longer needed as the service is shutting down
        eventBus.unregister(this);

        Ln.d("Service has been destroyed");
        saveOffers();
        notificationManager.cancel(STORAGE_NOTIFICATION_ID);
        super.onDestroy();
    }

    private void saveOffers() {
        Gson gson = new Gson();
        String s = gson.toJson(saved);
        try {
            FileOutputStream fos = openFileOutput(Constants.Storage.FILE_NAME, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOffers() {
        BufferedReader br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(Constants.Storage.FILE_NAME));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append('\n');
                    line = br.readLine();
                }
                String everything = sb.toString();
                if (everything.length() > 0) {
                    Gson gson = new Gson();
                    saved = gson.fromJson(everything, saved.getClass());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!started) {
            started = true;
            // Run as foreground service: http://stackoverflow.com/a/3856940/5210
            // Another example: https://github.com/commonsguy/cw-android/blob/master/Notifications/FakePlayer/src/com/commonsware/android/fakeplayerfg/PlayerService.java
            startForeground(STORAGE_NOTIFICATION_ID, getNotification("Stora"));
        }

        return START_NOT_STICKY;

    }

    @Subscribe
    public void onNewOfferEvent(NewOfferEvent newOfferEvent) {
        if (!saved.containsKey(newOfferEvent.getOffer().getId())) {
            saved.put(newOfferEvent.getOffer().getId(), newOfferEvent.getOffer());
        }
    }

    @Subscribe
    public void onGetOffersEvent(GetOffersEvent getOffersEvent) {
        eventBus.post(new LoadedOffersEvent(saved.values()));
    }

    /**
     * Creates a notification to show in the notification bar
     *
     * @param message the message to display in the notification bar
     * @return a new {@link android.app.Notification}
     */
    private Notification getNotification(String message) {
        final Intent i = new Intent(this, OfferStorageService.class);

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
