package me.loc2.loc2me.core.events;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.WifiInfo;

public class LoadedOffersEvent {
    private final List<Offer> offers;
    private final WifiInfo wifi;

    public List<Offer> getOffers() {
        return offers;
    }

    public WifiInfo getWifi() {
        return wifi;
    }

    public LoadedOffersEvent(WifiInfo wifi, List<Offer> offers) {
        this.wifi = wifi;
        this.offers = offers;
    }
}
