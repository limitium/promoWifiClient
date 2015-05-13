package me.loc2.loc2me.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Views;
import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;

/**
 * Base class for all Bootstrap Activities that need fragments.
 */
public class Loc2meFragmentActivity extends AppCompatActivity {

    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.global, menu);
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        return true;
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        Views.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }
}
