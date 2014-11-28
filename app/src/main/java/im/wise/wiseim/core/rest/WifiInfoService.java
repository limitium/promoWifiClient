package im.wise.wiseim.core.rest;

import java.util.List;

import im.wise.wiseim.core.models.Wise;
import retrofit.http.GET;
import retrofit.http.Path;


public interface WifiInfoService {
    @GET("/find/{name}")
    List<Wise> getWifiInfos(@Path("name") String name);
}
