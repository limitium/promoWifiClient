package me.loc2.loc2me;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.loc2.loc2me.core.ApiService;
import me.loc2.loc2me.core.Constants;
import me.loc2.loc2me.core.services.OfferCheckBackgroundService;
import me.loc2.loc2me.core.services.OfferEventService;
import me.loc2.loc2me.core.services.OfferLoaderService;
import me.loc2.loc2me.core.PostFromAnyThreadBus;
import me.loc2.loc2me.core.RestAdapterRequestInterceptor;
import me.loc2.loc2me.core.RestErrorHandler;
import me.loc2.loc2me.core.UserAgentProvider;
import me.loc2.loc2me.core.services.WifiScanService;
import me.loc2.loc2me.ui.FilterFragment;
import me.loc2.loc2me.ui.MainActivity;
import me.loc2.loc2me.ui.md.OfferListFragment;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                Loc2meApplication.class,
                MainActivity.class,
                FilterFragment.class,
                OfferListFragment.class,
                WifiScanService.class,
                OfferLoaderService.class,
                OfferCheckBackgroundService.class,
                OfferEventService.class
        }
)
public class Loc2meModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }


    @Provides
    ApiService provideBootstrapService(RestAdapter restAdapter) {
        return new ApiService(restAdapter);
    }

    @Provides
    Loc2meServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter) {
        return new Loc2meServiceProvider(restAdapter);
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
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
