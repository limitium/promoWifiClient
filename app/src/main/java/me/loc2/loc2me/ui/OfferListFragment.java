package me.loc2.loc2me.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import me.loc2.loc2me.BootstrapServiceProvider;
import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.authenticator.LogoutService;
import me.loc2.loc2me.core.Constants;
import me.loc2.loc2me.core.models.Offer;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class OfferListFragment extends ItemListFragment<Offer> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject Bus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
        eventBus.register(this);
        items = new ArrayList<Offer>();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_wises);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

//        getListAdapter()
//                .addHeader(activity.getLayoutInflater()
//                        .inflate(R.layout.wise_list_item_labels, null));
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<Offer>> onCreateLoader(int id, Bundle args) {
        final List<Offer> initialItems = items;
        return new ThrowableLoader<List<Offer>>(getActivity(), items) {

            @Override
            public List<Offer> loadData() throws Exception {
                return items;
//                try {
//                    if (getActivity() != null) {
//                        return serviceProvider.getService(getActivity()).getNews();
//                        return Collections.emptyList();
//                    } else {
//                        return Collections.emptyList();
//                    }
//
//                } catch (OperationCanceledException e) {
//                    Activity activity = getActivity();
//                    if (activity != null)
//                        activity.finish();
//                    return initialItems;
//                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Offer> createAdapter(List<Offer> items) {
        return new OfferListAdapter(getActivity().getLayoutInflater(), items);
    }



    public void onListItemClick(ListView l, View v, int position, long id) {
        Offer offer = ((Offer) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), OfferActivity.class).putExtra(Constants.Extra.WISE_ITEM, offer));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }
}
