package im.wise.wiseim.ui;

import android.os.Bundle;
import android.widget.TextView;

import im.wise.wiseim.R;

import butterknife.InjectView;
import im.wise.wiseim.core.models.Wise;

import static im.wise.wiseim.core.Constants.Extra.WISE_ITEM;

public class WiseActivity extends BootstrapActivity {

    private Wise wise;

    @InjectView(R.id.tv_title) protected TextView title;
    @InjectView(R.id.tv_content) protected TextView content;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wise);

        if (getIntent() != null && getIntent().getExtras() != null) {
            wise = (Wise) getIntent().getExtras().getSerializable(WISE_ITEM);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(wise.getName());

        title.setText(wise.getType());
        content.setText(wise.getMessage());

    }

}
