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

public class OfferStorageService{

    @Inject
    protected Bus eventBus;
    private HashMap<BigInteger, Offer> saved = new HashMap<BigInteger, Offer>();

//    private void saveOffers() {
//        Gson gson = new Gson();
//        String s = gson.toJson(saved);
//        try {
//            FileOutputStream fos = openFileOutput(Constants.Storage.FILE_NAME, Context.MODE_PRIVATE);
//            fos.write(s.getBytes());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

    @Subscribe
    public void onNewOfferEvent(NewOfferEvent newOfferEvent) {
        Offer offer = newOfferEvent.getOffer();
        if (!saved.containsKey(offer.getId())) {
            saved.put(offer.getId(), offer);
        }
    }

    @Subscribe
    public void onGetOffersEvent(GetOffersEvent getOffersEvent) {
        eventBus.post(new LoadedOffersEvent(saved.values()));
    }
}
