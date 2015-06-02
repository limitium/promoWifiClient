package me.loc2.loc2me.core.rest;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.UsedOffer;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;


public interface WifiOffersService {
    @GET("/api/offers/search")
    List<Offer> getWifiOffers(@Query("name") String name, @Query("filter") List<String> filters, @Query("mac") String mac);

    @POST("/api/offers/used-offers")
    UsedOffer sendUsedOffer(@Body UsedOffer usedOffer);
}
