
package me.loc2.loc2me.core;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.UsedOffer;
import me.loc2.loc2me.core.models.WifiRequest;
import me.loc2.loc2me.core.rest.WifiOffersService;
import me.loc2.loc2me.util.SafeAsyncTask;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * API service
 */
public class ApiService {

    private RestAdapter restAdapter;

    private WifiOffersService wifiOffersService;

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public ApiService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }


    private WifiOffersService getWifiOfferService() {
        if (null == wifiOffersService) {
            wifiOffersService = getRestAdapter().create(WifiOffersService.class);
        }
        return wifiOffersService;
    }


    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public List<Offer> getWifiOffers(final WifiRequest request) {
        List<Offer> wifiOffers = getWifiOfferService().getWifiOffers(request.getWifi().getName(), request.getFilters(), request.getMac());
        return wifiOffers;
    }

    public void sendUsedOffer(final Integer offerId, final UsedOffer usedOffer) {
        new SafeAsyncTask<UsedOffer>() {
            public UsedOffer call() throws Exception {
                getWifiOfferService().sendUsedOffer(offerId, usedOffer, new Callback<UsedOffer>() {
                    @Override
                    public void success(UsedOffer usedOffer, Response response) {
                        //
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //
                    }
                });
                return null;
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
        }.execute();
    }

}