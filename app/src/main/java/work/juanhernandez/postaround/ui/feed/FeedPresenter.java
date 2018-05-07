package work.juanhernandez.postaround.ui.feed;

import android.support.annotation.NonNull;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import work.juanhernandez.postaround.data.request.RecentMediaRequest;
import work.juanhernandez.postaround.data.response.RecentMediaResponse;
import work.juanhernandez.postaround.data.retrofit.recentmedia.FeedDataSource;

/**
 * Created by juan.hernandez on 5/4/18.
 * FeedPresenter is the class responsible for connecting with the data source (FeedDataSource)
 * and update the view (FeedActivity)
 */

public class FeedPresenter implements FeedContract.Presenter {
    @NonNull
    private FeedDataSource feedDataSource;  // retrofit instance

    @NonNull
    private Scheduler backgroundScheduler;  // scheduler on which the API will operate (work thread)

    @NonNull
    private Scheduler mainScheduler;    // scheduler which will wait for the API responses (ui thread)

    @NonNull
    private CompositeSubscription subscriptions;    // will have all subscriptions generated

    private FeedContract.View view;     // instance which implements the View interface

    private RecentMediaRequest recentMediaRequest;

    // all the dependencies are injected to help test the code
    FeedPresenter(@NonNull FeedDataSource feedDataSource,
                  @NonNull Scheduler backgroundScheduler,
                  @NonNull Scheduler mainScheduler,
                  FeedContract.View view,
                  RecentMediaRequest recentMediaRequest) {
        this.feedDataSource = feedDataSource;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.view = view;
        this.recentMediaRequest = recentMediaRequest;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void loadData() {
        // notify the view the API request will start soon
        view.onGetRecentMediaStarted();
        // clear previous API request
        subscriptions.clear();

        Subscription subscription = feedDataSource
                .getRecentMedia(recentMediaRequest.getLat(),
                        recentMediaRequest.getLng(), recentMediaRequest.getDistance(),
                        recentMediaRequest.getAccessToken(), recentMediaRequest.getCount())
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Observer<RecentMediaResponse>() {
                    @Override
                    public void onCompleted() {
                        // notify the view all the data is fetched already
                        view.onGetRecentMediaCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // notify the view there was an error in the API request
                        view.onGetRecentMediaError(e);
                    }

                    @Override
                    public void onNext(RecentMediaResponse recentMediaResponse) {
                        // returns the data requested to the view
                        view.onGetRecentMediaSuccess(recentMediaResponse.getData());
                    }
                });

        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        // the view is ready to get the data and the presenter starts the API request
        loadData();
    }

    @Override
    public void subscribe(int distance) {
        // the user changed the distance and the presenter starts the API request with new data
        recentMediaRequest.setDistance(distance);
        loadData();
    }

    @Override
    public void unsubscribe() {
        // the view became inactive
        subscriptions.clear();
    }

    @Override
    public void onDestroy() {
        // clearing the reference of the view when it's been destroyed
        this.view = null;
    }
}