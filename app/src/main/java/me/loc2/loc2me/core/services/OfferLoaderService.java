package me.loc2.loc2me.core.services;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.core.ApiService;
import me.loc2.loc2me.core.events.LoadedOfferEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.util.SafeAsyncTask;
import retrofit.RetrofitError;

public class OfferLoaderService {

    @Inject
    protected ApiService apiService;
    @Inject
    protected Bus eventBus;


    public void loadWifiOffers(final WifiInfo wifi) {
        if(wifi.getName().isEmpty()){
            return;
        }
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
        eventBus.post(new LoadedOfferEvent(offer));
    }

    public void register() {
        eventBus.register(this);

    }

    public void unregister() {
        eventBus.unregister(this);
    }
}
