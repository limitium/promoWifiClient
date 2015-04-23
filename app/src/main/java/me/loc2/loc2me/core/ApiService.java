
package me.loc2.loc2me.core;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.rest.WifiOffersService;
import retrofit.RestAdapter;

/**
 * API service
 */
public class ApiService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public ApiService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }


    private WifiOffersService getWifiOfferService() {
        return getRestAdapter().create(WifiOffersService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public List<Offer> getWifiOffers(String name) {
        return getWifiOfferService().getWifiOffers(name);
    }
}