package im.wise.wiseim.core.rest;

import im.wise.wiseim.core.Constants;
import retrofit.http.GET;

public interface CheckInService {

    @GET(Constants.Http.URL_CHECKINS_FRAG)
    CheckInWrapper getCheckIns();
}
