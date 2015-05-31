package me.loc2.loc2me.core.events;

import me.loc2.loc2me.core.models.Offer;

public class OfferChangedEvent {
    private final Offer offer;

    public OfferChangedEvent(Offer offer) {
        this.offer= offer;
    }

    public Offer getOffer() {
        return offer;
    }
}
