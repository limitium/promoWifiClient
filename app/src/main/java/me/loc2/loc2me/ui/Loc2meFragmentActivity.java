package me.loc2.loc2me.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.settings.SettingsFragment;
import me.loc2.loc2me.ui.md.BackPressListener;

/**
 * Base class for all Bootstrap Activities that need fragments.
 */
public abstract class Loc2meFragmentActivity extends AppCompatActivity {

    @Inject
    protected Bus eventBus;

    protected Toolbar toolbar;
    private BackPressListener onBackPressListener;

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        setContentView(getLayoutResource());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportFragmentManager().addOnBackStackChangedListener(
                    new FragmentManager.OnBackStackChangedListener() {
                        public void onBackStackChanged() {
                            toolbar.setTitle(R.string.app_name);
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
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

    @Override
    public void onBackPressed() {
        if (null != getOnBackPressListener()) {
            getOnBackPressListener().goBack(this);
        } else {
            super.onBackPressed();
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setOnBackPressListener(BackPressListener onBackPressListener) {
        this.onBackPressListener = onBackPressListener;
    }

    public BackPressListener getOnBackPressListener() {
        return onBackPressListener;
    }
}
