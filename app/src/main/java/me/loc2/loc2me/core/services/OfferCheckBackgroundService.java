package me.loc2.loc2me.core.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.core.events.LoadedOfferEvent;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.util.Ln;

public class OfferCheckBackgroundService extends Service {
    @Inject
    protected Bus eventBus;

    @Inject
    protected OfferLoaderService offerLoaderService;
    @Inject
    protected WifiScanService wifiScanService;

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

        offerLoaderService.unregister();
        wifiScanService.unregister(this);

        super.onDestroy();
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
