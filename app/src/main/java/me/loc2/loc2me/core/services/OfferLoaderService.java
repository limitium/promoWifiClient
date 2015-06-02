package me.loc2.loc2me.core.services;

import com.google.common.collect.ImmutableList;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.core.ApiService;
import me.loc2.loc2me.core.events.LoadedOffersEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.util.SafeAsyncTask;
import retrofit.RetrofitError;

public class OfferLoaderService {

    @Inject
    protected ApiService apiService;
    @Inject
    protected Bus eventBus;


    public void loadWifiOffers(final WifiInfo wifi, final ImmutableList<String> filters) {
        if(wifi.getName().isEmpty()){
            return;
        }
        new SafeAsyncTask<List<Offer>>() {
            public List<Offer> call() throws Exception {
                return apiService.getWifiOffers(wifi.getName(), filters);
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
                    onWifiInfo(wifi,offers);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                //do nothing
            }
        }.execute();
    }

    private void onWifiInfo(WifiInfo wifi, List<Offer> offers) {
        eventBus.post(new LoadedOffersEvent(wifi,offers));
    }

    public void register() {
        eventBus.register(this);

    }

    public void unregister() {
        eventBus.unregister(this);
    }
}
