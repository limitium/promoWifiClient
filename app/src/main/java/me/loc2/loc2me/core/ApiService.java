
package me.loc2.loc2me.core;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.UsedOffer;
import me.loc2.loc2me.core.models.WifiRequest;
import me.loc2.loc2me.core.rest.WifiOffersService;
import retrofit.RestAdapter;

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

    public void sendUsedOffer(UsedOffer usedOffer) {
        getWifiOfferService().sendUsedOffer(usedOffer);
    }

}