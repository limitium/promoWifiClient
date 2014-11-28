package im.wise.wiseim.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import im.wise.wiseim.BootstrapServiceProvider;
import im.wise.wiseim.Injector;
import im.wise.wiseim.R;
import im.wise.wiseim.authenticator.LogoutService;
import im.wise.wiseim.core.models.CheckIn;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class CheckInsListFragment extends Fragment {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }
}
