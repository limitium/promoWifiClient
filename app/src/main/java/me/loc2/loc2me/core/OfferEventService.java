package me.loc2.loc2me.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.OfferRemoveEvent;
import me.loc2.loc2me.core.models.Offer;

public class OfferEventService extends Service {

    @Inject
    protected Bus eventBus;

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

    public void remove(Offer offer) {
        eventBus.post(new OfferRemoveEvent(offer));
    }

    public void add(Offer offer) {
        eventBus.post(new NewOfferEvent(offer));
    }
}
