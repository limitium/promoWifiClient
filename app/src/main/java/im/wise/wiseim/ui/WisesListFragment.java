package im.wise.wiseim.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import im.wise.wiseim.BootstrapServiceProvider;
import im.wise.wiseim.Injector;
import im.wise.wiseim.R;
import im.wise.wiseim.authenticator.LogoutService;
import im.wise.wiseim.core.Constants;
import im.wise.wiseim.core.events.NewWiseEvent;
import im.wise.wiseim.core.models.Wise;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WisesListFragment extends ItemListFragment<Wise> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject Bus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
        eventBus.register(this);
        items = new ArrayList<Wise>();
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
    public Loader<List<Wise>> onCreateLoader(int id, Bundle args) {
        final List<Wise> initialItems = items;
        return new ThrowableLoader<List<Wise>>(getActivity(), items) {

            @Override
            public List<Wise> loadData() throws Exception {
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
    protected SingleTypeAdapter<Wise> createAdapter(List<Wise> items) {
        return new WisesListAdapter(getActivity().getLayoutInflater(), items);
    }



    public void onListItemClick(ListView l, View v, int position, long id) {
        Wise wise = ((Wise) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), WiseActivity.class).putExtra(Constants.Extra.WISE_ITEM, wise));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }
}
