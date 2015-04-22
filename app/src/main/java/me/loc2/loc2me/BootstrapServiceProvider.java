
package me.loc2.loc2me;

import android.accounts.AccountsException;
import android.app.Activity;

import me.loc2.loc2me.core.BootstrapService;

import java.io.IOException;

import retrofit.RestAdapter;

/**
 * Provider for a {@link me.loc2.loc2me.core.BootstrapService} instance
 */
public class BootstrapServiceProvider {

    private RestAdapter restAdapter;

    public BootstrapServiceProvider(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    /**
     * Get service for configured key provider
     * <p/>
     * This method gets an auth key and so it blocks and shouldn't be called on the main thread.
     *
     * @return bootstrap service
     * @throws IOException
     * @throws AccountsException
     */
    public BootstrapService getService(final Activity activity)
            throws IOException, AccountsException {

        // TODO: See how that affects the bootstrap service.
        return new BootstrapService(restAdapter);
    }
}
