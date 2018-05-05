package work.juanhernandez.postaround.ui.feed;

import android.support.annotation.NonNull;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import work.juanhernandez.postaround.data.request.RecentMediaRequest;
import work.juanhernandez.postaround.data.response.RecentMediaResponse;
import work.juanhernandez.postaround.data.retrofit.recentmedia.RecentMediaDataSource;
import work.juanhernandez.postaround.data.retrofit.recentmedia.RecentMediaRemoteDataSource;

/**
 * Created by juan.hernandez on 5/4/18.
 * FeedPresenter
 */

public class FeedPresenter implements FeedContract.Presenter {
    @NonNull
    private RecentMediaDataSource recentMediaDataSource;

    @NonNull
    private Scheduler backgroundScheduler;

    @NonNull
    private Scheduler mainScheduler;

    @NonNull
    private CompositeSubscription subscriptions;

    private FeedContract.View view;

    private RecentMediaRequest recentMediaRequest;

    // all the dependencies are injected to help our unit tests
    public FeedPresenter(@NonNull RecentMediaDataSource recentMediaDataSource,
                         @NonNull Scheduler backgroundScheduler,
                         @NonNull Scheduler mainScheduler,
                         FeedContract.View view,
                         RecentMediaRequest recentMediaRequest) {
        this.recentMediaDataSource = recentMediaDataSource;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.view = view;
        // data
        this.recentMediaRequest = recentMediaRequest;
        this.subscriptions = new CompositeSubscription();
    }


    @Override
    public void loadData() {
        view.onGetRecentMediaStarted();
        subscriptions.clear();

        Subscription subscription = recentMediaDataSource
                .getRecentMedia(recentMediaRequest.getLat(),
                        recentMediaRequest.getLng(), recentMediaRequest.getDistance(),
                        recentMediaRequest.getAccessToken(), recentMediaRequest.getCount())
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Observer<RecentMediaResponse>() {
                    @Override
                    public void onCompleted() {
                        view.onGetRecentMediaCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onGetRecentMediaError(e);
                    }

                    @Override
                    public void onNext(RecentMediaResponse recentMediaResponse) {
                        view.onGetRecentMediaSuccess(recentMediaResponse.getData());
                    }
                });

        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadData();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }
}