package im.wise.wiseim.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import im.wise.wiseim.Injector;
import im.wise.wiseim.R;
import im.wise.wiseim.core.Constants;

public class FilterFragment extends Fragment {
    @Inject Bus eventBus;

    @InjectView(R.id.filterSalesSeek) protected SeekBar filterSalesSeek;

    @InjectView(R.id.filterSalesPercent) protected TextView filterSalesPercent;

    @InjectView(R.id.filtersCollectiablesArt) protected CheckBox filtersCollectiablesArt;
    @InjectView(R.id.filterElectronics) protected CheckBox filterElectronics;
    @InjectView(R.id.filterEntertainment) protected CheckBox filterEntertainment;
    @InjectView(R.id.filterFashion) protected CheckBox filterFashion;
    @InjectView(R.id.filterMotor) protected CheckBox filterMotor;
    @InjectView(R.id.filtersHomeGarden) protected CheckBox filtersHomeGarden;
    @InjectView(R.id.filterSports) protected CheckBox filterSports;
    @InjectView(R.id.filterToysHobbies) protected CheckBox filterToysHobbies;

    protected int salePercent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filters_layout, container, false);
    }

    @Override
    public void onDestroy() {
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.Prefs.NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constants.Prefs.FILTER_SALE_PERCENT, salePercent);
        editor.putBoolean(Constants.Prefs.FILTER_COLLECTINABLES_ART, filtersCollectiablesArt.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_ELECTRONICS, filterElectronics.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_ENTERTAIMENT, filterEntertainment.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_FASHION, filterFashion.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_MOTOR, filterMotor.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_HOME_GARDEN, filtersHomeGarden.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_SPORTS, filterSports.isChecked());
        editor.putBoolean(Constants.Prefs.FILTER_TOYS_HOBBIES, filterToysHobbies.isChecked());

        editor.commit();
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        eventBus.register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Views.inject(this, getView());
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.Prefs.NAME, 0);

        salePercent = settings.getInt(Constants.Prefs.FILTER_SALE_PERCENT, 15);
        filterSalesPercent.setText(salePercent + "%");

        filtersCollectiablesArt.setChecked(settings.getBoolean(Constants.Prefs.FILTER_COLLECTINABLES_ART, true));
        filterElectronics.setChecked(settings.getBoolean(Constants.Prefs.FILTER_ELECTRONICS, true));
        filterEntertainment.setChecked(settings.getBoolean(Constants.Prefs.FILTER_ENTERTAIMENT, true));
        filterFashion.setChecked(settings.getBoolean(Constants.Prefs.FILTER_FASHION, true));
        filterMotor.setChecked(settings.getBoolean(Constants.Prefs.FILTER_MOTOR, true));
        filtersHomeGarden.setChecked(settings.getBoolean(Constants.Prefs.FILTER_HOME_GARDEN, true));
        filterSports.setChecked(settings.getBoolean(Constants.Prefs.FILTER_SPORTS, true));
        filterToysHobbies.setChecked(settings.getBoolean(Constants.Prefs.FILTER_TOYS_HOBBIES, true));


        final int step = 1;
        final int max = 99;
        final int min = 1;
        filterSalesSeek.setMax((max - min) / step);
        filterSalesSeek.setProgress(salePercent);
        filterSalesSeek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        // Ex :
                        // And finnaly when you want to retrieve the value in the range you
                        // wanted in the first place -> [3-5]
                        //
                        // if progress = 13 -> value = 3 + (13 * 0.1) = 4.3
                        salePercent = min + (progress * step);
                        filterSalesPercent.setText(salePercent + "%");
                    }
                }
        );
    }
}
