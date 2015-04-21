package me.loc2.loc2me.core.events;

import java.util.Collection;

import me.loc2.loc2me.core.models.Offer;

public class LoadedOffersEvent {
    Collection<Offer> offers;

    public LoadedOffersEvent(Collection<Offer> offers) {
        this.offers = offers;
    }

    public Collection<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Collection<Offer> offers) {
        this.offers = offers;
    }
}
