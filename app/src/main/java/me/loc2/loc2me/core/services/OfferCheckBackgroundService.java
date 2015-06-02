package me.loc2.loc2me.core.services;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.google.common.base.Optional;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.core.ApiService;
import me.loc2.loc2me.core.dao.OfferPersistService;
import me.loc2.loc2me.core.events.LoadedOffersEvent;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.UsedOffer;
import me.loc2.loc2me.core.models.WifiRequest;
import me.loc2.loc2me.events.OfferUsedEvent;
import me.loc2.loc2me.util.Ln;

import static me.loc2.loc2me.core.Constants.Notification.SCAN_NOTIFICATION_ID;

public class OfferCheckBackgroundService extends Service {
    @Inject
    protected Bus eventBus;
    @Inject
    protected NotificationManager notificationManager;
    @Inject
    protected OfferLoaderService offerLoaderService;
    @Inject
    protected WifiScanService wifiScanService;
    @Inject
    protected OfferPersistService offerPersistService;
    @Inject
    protected OfferNotificationService offerNotificationService;
    @Inject
    protected ImageLoaderService imageLoaderService;
    @Inject
    protected FilterService filterService;
    @Inject
    protected ApiService apiService;

    private String mac;

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

        final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        mac = connectionInfo.getMacAddress();
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
        wifiScanService.scan();
        return START_STICKY;
    }

    @Subscribe
    public void onLoadedOffersEvent(LoadedOffersEvent loadedOffersEvent) {
        List<Offer> offers = loadedOffersEvent.getOffers();
//TODO: remove or mark deleted offers
        for (Offer offer : offers) {
            Ln.i("New offer: " + offer);
            if (!offerPersistService.isDeleted(offer.getId())) {
                Ln.i("Offer wasn't deleted");
                Optional<Offer> saved = offerPersistService.findOneReceived(offer.getId());
                Ln.i("Offer was saved before: " + String.valueOf(saved.isPresent()));
                if (!saved.isPresent() || !saved.get().getUpdated_at().equals(offer.getUpdated_at())) {
                    if (saved.isPresent()) {
                        offerPersistService.deleteReceived(offer.getId());
                    } else {
                        offer.setAdded_at(System.currentTimeMillis());
                    }
                    setOfferColors(offer);
                }
            }
        }
    }


    public void setOfferColors(final Offer offer) {
        imageLoaderService.loadImage(offer, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Palette palette = Palette.from(loadedImage).generate();
                offer.setBackgroundColor(palette.getDarkVibrantColor(Color.DKGRAY));
                offer.setTextColor(palette.getLightMutedColor(Color.WHITE));
                saveAndPost(offer);
            }
        });
    }

    private void saveAndPost(Offer offer) {
        offerPersistService.saveReceived(offer);
        eventBus.post(new NewOfferEvent(offer));
    }

    @Subscribe
    public void onNewWifiNetworkEvent(NewWifiNetworkEvent wifiNetworkEvent) {
        Ln.i("New network: " + wifiNetworkEvent.getWifiInfo().getName());
        WifiRequest request = new WifiRequest(wifiNetworkEvent.getWifiInfo(), filterService.findAllActive(), mac);
        offerLoaderService.loadWifiOffers(request);
    }

    @Subscribe
    public void onUsedEvent(OfferUsedEvent offerUsedEvent) {
        Offer offer = offerUsedEvent.getOffer();
        Ln.i("Offer:" + offer + " is marked as used");
        apiService.sendUsedOffer(new UsedOffer(offer.getId(), mac));
    }

    @Subscribe
    public void onNewOfferEvent(NewOfferEvent newOfferEvent) {
        offerNotificationService.notify(newOfferEvent.getOffer());
    }
}
