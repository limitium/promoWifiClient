
package me.loc2.loc2me.core;

import java.util.List;

import me.loc2.loc2me.core.models.User;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.rest.UserService;
import me.loc2.loc2me.core.rest.WifiOffersService;
import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
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

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public List<User> getUsers() {
        return getUserService().getUsers().getResults();
    }

    public User authenticate(String email, String password) {
        return getUserService().authenticate(email, password);
    }
}