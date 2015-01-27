package im.wise.wiseim;

import android.accounts.AccountManager;
import android.content.Context;

import im.wise.wiseim.authenticator.ApiKeyProvider;
import im.wise.wiseim.authenticator.BootstrapAuthenticatorActivity;
import im.wise.wiseim.authenticator.LogoutService;
import im.wise.wiseim.core.BootstrapService;
import im.wise.wiseim.core.PostFromAnyThreadBus;
import im.wise.wiseim.core.RestAdapterRequestInterceptor;
import im.wise.wiseim.core.RestErrorHandler;
import im.wise.wiseim.core.WiseLoaderService;
import im.wise.wiseim.core.WifiScanService;
import im.wise.wiseim.core.TimerService;
import im.wise.wiseim.core.UserAgentProvider;
import im.wise.wiseim.core.WiseStorageService;
import im.wise.wiseim.ui.BootstrapTimerActivity;
import im.wise.wiseim.ui.CheckInsListFragment;
import im.wise.wiseim.ui.FilterFragment;
import im.wise.wiseim.ui.MainActivity;
import im.wise.wiseim.ui.NavigationDrawerFragment;
import im.wise.wiseim.ui.WiseActivity;
import im.wise.wiseim.ui.WisesListFragment;
import im.wise.wiseim.ui.UserActivity;
import im.wise.wiseim.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                WiseActivity.class,
                WisesListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                FilterFragment.class,
                TimerService.class,
                WifiScanService.class,
                WiseStorageService.class,
                WiseLoaderService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
//                .setEndpoint(Constants.Http.URL_BASE)
                .setEndpoint("http://promowifi.herokuapp.com")
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

//    @Provides
//    WifiReceiver provideWifiReceiver(){
//        return new WifiReceiver();
//    }
}
