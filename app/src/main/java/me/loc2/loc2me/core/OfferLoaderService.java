package me.loc2.loc2me.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.events.OfferRemoveEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.util.Ln;
import me.loc2.loc2me.util.SafeAsyncTask;
import retrofit.RetrofitError;

public class OfferLoaderService extends Service {

    @Inject
    protected Bus eventBus;
    @Inject
    protected ApiService apiService;
    @Inject
    protected OfferEventService offerService;
    private int wifis;
    private ConcurrentHashMap<BigInteger, Long> shown = new ConcurrentHashMap<BigInteger, Long>();
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

    @Subscribe
    public void onNewWifiNetworkEvent(NewWifiNetworkEvent wifiNetworkEvent) {
        Ln.i("New network: " + wifiNetworkEvent.getWifiInfo().getName());
        loadWifiOffers(wifiNetworkEvent.getWifiInfo());
    }

    @Subscribe
    public void removeOfferFromCache(OfferRemoveEvent offerRemoveEvent) {
        Offer offer = offerRemoveEvent.getOffer();
        shown.remove(offer.getId());
    }

    private void loadWifiOffers(final WifiInfo wifi) {
        new SafeAsyncTask<List<Offer>>() {
            public List<Offer> call() throws Exception {
                return apiService.getWifiOffers(wifi.getName());
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
            offerService.add(offer);
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
}
