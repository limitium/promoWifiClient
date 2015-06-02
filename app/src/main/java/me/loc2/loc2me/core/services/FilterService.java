package me.loc2.loc2me.core.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import me.loc2.loc2me.R;
import me.loc2.loc2me.util.Ln;

public class FilterService {

    private final ImmutableSet<String> keys;
    private Map<String, Boolean> filters;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Inject
    public FilterService(Context context) {
        Context appContext = context.getApplicationContext();
        keys = ImmutableSet.of(
                appContext.getResources().getString(R.string.pref_filter_food),
                appContext.getResources().getString(R.string.pref_filter_cosmetics)
        );
        setUpFiltersInMemoryMap(appContext);
        subscribeToFilterPreferencesChanges(appContext);
    }

    /**
     * @return all filters values in (name, active\inactive) format
     */
    public ImmutableList<Map.Entry<String, Boolean>> findAll() {
        return ImmutableList.copyOf(filters.entrySet());
    }

    /**
     * @return only active (checked) filters
     */
    public ImmutableList<String> findAllActive() {
        return FluentIterable.from(filters.entrySet()).filter(new Predicate<Map.Entry<String, Boolean>>() {
            @Override
            public boolean apply(Map.Entry<String, Boolean> input) {
                return input.getValue();
            }
        }).transform(new Function<Map.Entry<String,Boolean>, String>() {
            @Override
            public String apply(Map.Entry<String, Boolean> input) {
                return input.getKey();
            }
        }).toList();
    }

    public Optional<Boolean> findOne(String key) {
        return Optional.fromNullable(filters.get(key));
    }

    private void setUpFiltersInMemoryMap(Context context) {
        filters = new ConcurrentHashMap<String, Boolean>(keys.size());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for (String key: keys) {
            filters.put(key, prefs.getBoolean(key, false));
        }
    }

    private void subscribeToFilterPreferencesChanges(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (keys.contains(key)) {
                    filters.put(key, sharedPreferences.getBoolean(key, false));
                }

                Ln.i("Filter service active:" + Arrays.toString(findAllActive().toArray()));
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
