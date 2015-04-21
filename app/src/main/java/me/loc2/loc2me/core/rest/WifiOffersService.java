package me.loc2.loc2me.core.rest;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import retrofit.http.GET;
import retrofit.http.Path;


public interface WifiOffersService {
    @GET("/find/{name}")
    List<Offer> getWifiOffers(@Path("name") String name);
}
