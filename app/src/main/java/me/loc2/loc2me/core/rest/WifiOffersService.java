package me.loc2.loc2me.core.rest;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import retrofit.http.GET;
import retrofit.http.Query;


public interface WifiOffersService {
    @GET("/api/offers/search")
    List<Offer> getWifiOffers(@Query("name") String name);
}
