package me.loc2.loc2me.core.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import javax.inject.Inject;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.ui.md.OfferDetailsActivity;

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
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(offer.getName())
                .setContentText(offer.getOrganization_name())
                .setSmallIcon(R.drawable.ic_stat_ab_notification)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(Color.BLUE, 500, 500)
                .setStyle(new NotificationCompat.InboxStyle())
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
        return true;
    }

    private boolean withSound() {
        return true;
    }

    private boolean isNotifyDisabled() {
        return false;
    }
}
