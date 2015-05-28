package me.loc2.loc2me.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import me.loc2.loc2me.R;
import me.loc2.loc2me.ui.Loc2meFragmentActivity;
import me.loc2.loc2me.ui.md.BackPressListener;

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
        Loc2meFragmentActivity activity = (Loc2meFragmentActivity) getActivity();
        activity.setOnBackPressListener(this);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void goBack(Activity activity) {
        activity.onBackPressed();
    }
}