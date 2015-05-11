
package me.loc2.loc2me.core;

import java.util.List;
import java.util.Random;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.OfferImage;
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

    public List<Offer> getWifiOffers(String name) {
        List<Offer> wifiOffers = getWifiOfferService().getWifiOffers(name);
        addImageStubs(wifiOffers);
        return wifiOffers;
    }

    private void addImageStubs(List<Offer> offers) {
        Random random = new Random();
        for (Offer offer: offers) {
            int index = random.nextInt(10);
            offer.setImage(new OfferImage("http://lorempixel.com/", 1080, index * 100 + 900));
        }
    }
}