package me.loc2.loc2me.events;

import me.loc2.loc2me.core.models.Offer;

public class OfferUsedEvent {

    private final Offer offer;

    public OfferUsedEvent(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }
}
