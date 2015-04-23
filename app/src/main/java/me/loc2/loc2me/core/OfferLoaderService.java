package me.loc2.loc2me.core;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.NewWifiNetworkEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.util.SafeAsyncTask;
import retrofit.RetrofitError;

public class OfferLoaderService {

    @Inject
    protected Bus eventBus;
    @Inject
    protected ApiService apiService;
    private int wifis;
    private HashMap<BigInteger, Long> shown = new HashMap<BigInteger, Long>();
    private int shownTime = 1000 * 60 * 60 * 24 * 7;


    @Subscribe
    public void onNewWifiNetworkEvent(NewWifiNetworkEvent wifiNetworkEvent) {
        loadWifiOffers(wifiNetworkEvent.getWifiInfo());
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

}
