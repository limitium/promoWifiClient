package me.loc2.loc2me.core.events;

import me.loc2.loc2me.core.models.Offer;

public class LoadedOfferEvent {
    private final Offer offer;

    public Offer getOffer() {
        return offer;
    }

    public LoadedOfferEvent(Offer offer) {
        this.offer = offer;
    }
}
