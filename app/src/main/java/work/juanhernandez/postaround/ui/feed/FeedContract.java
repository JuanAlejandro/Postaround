package work.juanhernandez.postaround.ui.feed;

import java.util.List;

import work.juanhernandez.postaround.data.model.RecentMedia;

/**
 * Created by juan.hernandez on 5/5/18.
 * This interface defines the communication between the View and the Presenter
 */

public interface FeedContract {
    interface View {
        // notify the view that the request is about to start
        void onGetRecentMediaStarted();

        // notify the view no more data will be returned
        void onGetRecentMediaCompleted();

        // return the requested data
        void onGetRecentMediaSuccess(List<RecentMedia> recentMedia);

        // an error has occurred during the request
        void onGetRecentMediaError(Throwable e);
    }

    interface Presenter {
        // will tell the presenter start fetching data
        void loadData();

        // notify the presenter that its view has become active
        void subscribe();

        // notify the presenter that the distance changed
        void subscribe(int distance);

        // notify the presenter that its view has become inactive
        void unsubscribe();

        // notify the presenter that its view instance is about to be destroyed
        void onDestroy();
    }
}
