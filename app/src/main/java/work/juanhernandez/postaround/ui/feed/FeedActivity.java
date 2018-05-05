package work.juanhernandez.postaround.ui.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.data.model.RecentMedia;
import work.juanhernandez.postaround.ui.base.BaseActivity;

/**
 * Created by juan.hernandez on 5/4/18.
 * FeedActivity
 */

public class FeedActivity extends BaseActivity implements FeedContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    @Override
    protected void initializeViews() {

    }

    @Override
    public void onGetRecentMediaStarted() {

    }

    @Override
    public void onGetRecentMediaCompleted() {

    }

    @Override
    public void onGetRecentMediaSuccess(List<RecentMedia> recentMedia) {

    }

    @Override
    public void onGetRecentMediaError(Throwable e) {

    }
}
