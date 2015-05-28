package me.loc2.loc2me.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.loc2.loc2me.R;
import me.loc2.loc2me.ui.Loc2meFragmentActivity;
import me.loc2.loc2me.ui.MainActivity;
import me.loc2.loc2me.ui.md.BackPressListener;
import me.loc2.loc2me.ui.md.OfferListFragment;
import me.loc2.loc2me.util.Ln;

public class SettingsFragment extends PreferenceFragment implements BackPressListener {

    private String SOUND_KEY;
    private String SHOW_KEY;
    private String VIBRO_KEY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SOUND_KEY = getString(R.string.pref_notification_sound);
        SHOW_KEY = getString(R.string.pref_notification_show);
        VIBRO_KEY = getString(R.string.pref_notification_vibro);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void goBack(Activity activity) {
        MainActivity mainActivity = (MainActivity)activity;
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mainActivity.openFragment(mainActivity.getOfferListFragment(), MainActivity.LIST_FRAGMENT_TAG);
    }
}