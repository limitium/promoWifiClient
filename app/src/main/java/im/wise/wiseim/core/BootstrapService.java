
package im.wise.wiseim.core;

import java.util.List;

import im.wise.wiseim.core.models.CheckIn;
import im.wise.wiseim.core.models.News;
import im.wise.wiseim.core.models.User;
import im.wise.wiseim.core.models.Wise;
import im.wise.wiseim.core.rest.CheckInService;
import im.wise.wiseim.core.rest.NewsService;
import im.wise.wiseim.core.rest.UserService;
import im.wise.wiseim.core.rest.WifiInfoService;
import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

    private NewsService getNewsService() {
        return getRestAdapter().create(NewsService.class);
    }

    private CheckInService getCheckInService() {
        return getRestAdapter().create(CheckInService.class);
    }

    private WifiInfoService getWifiInfoService() {
        return getRestAdapter().create(WifiInfoService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     */
    public List<News> getNews() {
        return getNewsService().getNews().getResults();
    }

    public List<Wise> getWifiInfos(String name) {
        return getWifiInfoService().getWifiInfos(name);
    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public List<User> getUsers() {
        return getUserService().getUsers().getResults();
    }

    /**
     * Get all bootstrap Checkins that exists on Parse.com
     */
    public List<CheckIn> getCheckIns() {
       return getCheckInService().getCheckIns().getResults();
    }

    public User authenticate(String email, String password) {
        return getUserService().authenticate(email, password);
    }
}