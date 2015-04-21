package me.loc2.loc2me.core.events;

import me.loc2.loc2me.core.models.Offer;

public class NewOfferEvent {
    private Offer offer;

    public NewOfferEvent(Offer offer) {
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
