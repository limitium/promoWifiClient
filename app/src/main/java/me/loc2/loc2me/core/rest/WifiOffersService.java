package me.loc2.loc2me.core.rest;

import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.Usage;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface WifiOffersService {
    @GET("/api/offers/search")
    List<Offer> getWifiOffers(@Query("name") String name, @Query("filter") List<String> filters, @Query("mac") String mac);

    @POST("/api/offers/{id}/usages")
    void postUsage(@Path("id") Integer id, @Body Usage usage, Callback<Usage> callback);
}
 
