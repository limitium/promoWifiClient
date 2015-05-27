package me.loc2.loc2me.core.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import javax.inject.Inject;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.ui.md.OfferDetailsActivity;
import me.loc2.loc2me.util.Ln;

public class OfferNotificationService {
    @Inject
    protected Context context;
    @Inject
    protected NotificationManager notificationManager;
    @Inject
    protected ImageLoaderService imageLoaderService;

    public void notify(final Offer offer) {
        if (isNotifyDisabled()) {
            return;
        }

        Intent resultIntent = new Intent(context, OfferDetailsActivity.class);
        resultIntent.putExtra(OfferDetailsActivity.OFFER, (Parcelable) offer);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack
        stackBuilder.addParentStack(OfferDetailsActivity.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        context,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_CANCEL_CURRENT
//                );


        int color = context.getResources().getColor(R.color.green);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(offer.getName())
                .setContentText(offer.getOrganization_name())
                .setSmallIcon(R.mipmap.ic_launcher_notify)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(color, 500, 500)
                .setStyle(new NotificationCompat.InboxStyle())
                .setColor(color)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());

        if (withSound()) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(alarmSound);

        }

        if (withVibration()) {
            long[] pattern = {100, 200, 300};
            notificationBuilder.setVibrate(pattern);
        }

        imageLoaderService.loadAvatar(offer.getAvatar(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                notificationBuilder.setLargeIcon(loadedImage);
                notificationManager.notify(offer.getId(), notificationBuilder.build());
            }
        });
    }

    private boolean withVibration() {
        return false;
    }

    private boolean withSound() {
        return false;
    }

    private boolean isNotifyDisabled() {
        return false;
    }

    private void qwe() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Ln.i("Value for checkbox " + key + " has changed to " + sharedPreferences.getBoolean(key, false));
            }
        });

    }
}
