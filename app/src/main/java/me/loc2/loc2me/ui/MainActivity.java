

package me.loc2.loc2me.ui;

import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Random;

import javax.inject.Inject;

import butterknife.Views;
import me.loc2.loc2me.Loc2meServiceProvider;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.ApiService;
import me.loc2.loc2me.core.services.OfferEventService;
import me.loc2.loc2me.core.services.OfferLoaderService;
import me.loc2.loc2me.core.services.OfferStorageService;
import me.loc2.loc2me.core.services.TimerService;
import me.loc2.loc2me.core.services.WifiScanService;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.OfferImage;
import me.loc2.loc2me.events.NavItemSelectedEvent;
import me.loc2.loc2me.ui.md.OfferListFragment;
import me.loc2.loc2me.util.Ln;
import me.loc2.loc2me.util.SafeAsyncTask;
import me.loc2.loc2me.util.UIUtils;


/**
* Initial activity for the application.
*
*/
public class MainActivity extends Loc2meFragmentActivity {

    @Inject protected Loc2meServiceProvider serviceProvider;
    @Inject protected OfferEventService offerEventService;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        Views.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);
        }
        checkAuth();


    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }


    private void initScreen() {
        if (userHasAuthenticated) {

            startService(new Intent(this, OfferLoaderService.class));
            startService(new Intent(this, TimerService.class));
            startService(new Intent(this, WifiScanService.class));
            startService(new Intent(this, OfferStorageService.class));

            Ln.d("Foo");
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new OfferListFragment())
                    .commit();
        }

    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final ApiService svc = serviceProvider.getService(MainActivity.this);
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;
                initScreen();
                initImageLoader();
            }
        }.execute();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            case R.id.menu_add:
                Offer offer = new Offer();
                Random random = new Random();
                int index = random.nextInt(10);
                String indexStr = String.valueOf(index);

                offer.setId(new BigInteger(indexStr));
                offer.setName("Item " + indexStr);
                offer.setMessage(getString(R.string.lorem_ipsum_long));
                offer.setType("Type " + indexStr);
                offer.setCategory("Category " + indexStr);
                offer.setImage(new OfferImage("http://lorempixel.com/", 1080, index * 100 + 900));
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1 * (index + 1));
                offer.setAddedAt(cal.getTimeInMillis());
                offerEventService.add(offer);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
        }
    }
}
