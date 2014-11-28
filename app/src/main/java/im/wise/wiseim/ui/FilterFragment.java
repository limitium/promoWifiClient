package im.wise.wiseim.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TitlePageIndicator;

import butterknife.InjectView;
import butterknife.Views;
import im.wise.wiseim.R;

public class FilterFragment extends Fragment {

//    @InjectView(R.id.tpi_header)
//    protected TitlePageIndicator indicator;
//
//    @InjectView(R.id.vp_pages)
//    protected ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filters_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Views.inject(this, getView());

//        pager.setAdapter(new BootstrapPagerAdapter(getResources(), getChildFragmentManager()));
//        indicator.setViewPager(pager);
//        pager.setCurrentItem(0);

    }
}
