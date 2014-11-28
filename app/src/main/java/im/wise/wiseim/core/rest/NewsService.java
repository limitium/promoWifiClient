package im.wise.wiseim.core.rest;

import im.wise.wiseim.core.Constants;
import retrofit.http.GET;


/**
 * Interface for defining the wise service to communicate with Parse.com
 */
public interface NewsService {

    @GET(Constants.Http.URL_NEWS_FRAG)
    NewsWrapper getNews();

}
