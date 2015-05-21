package me.loc2.loc2me.core.models;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class OfferSerializer {

    ObjectMapper mapper;

    public OfferSerializer() {
        mapper = new ObjectMapper();
    }

    public String serialize(Offer offer) throws IOException {
        return mapper.writeValueAsString(offer);
    }

    public Offer deserialize(String serializedOffer) throws IOException {
        return mapper.readValue(serializedOffer, Offer.class);
    }
}
