package me.loc2.loc2me.settings;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import me.loc2.loc2me.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.settings);
    }
}