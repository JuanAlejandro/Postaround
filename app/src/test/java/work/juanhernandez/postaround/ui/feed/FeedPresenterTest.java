package work.juanhernandez.postaround.ui.feed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;
import work.juanhernandez.postaround.data.request.RecentMediaRequest;
import work.juanhernandez.postaround.data.response.RecentMediaResponse;
import work.juanhernandez.postaround.data.retrofit.recentmedia.FeedRemoteDataSource;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by juan.hernandez on 5/5/18.
 * FeedPresenterTest
 */
public class FeedPresenterTest {
    @Mock
    private FeedRemoteDataSource recentMediaDataSource;

    @Mock
    private FeedContract.View view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRecentMediaShouldLoadIntoView() {
        RecentMediaResponse recentMediaResponse = new RecentMediaResponse(new ArrayList<>());

        when(recentMediaDataSource.getRecentMedia(0, 0, 0, "", 0))
                .thenReturn(Observable.just(recentMediaResponse));

        FeedPresenter feedPresenter = new FeedPresenter(
                this.recentMediaDataSource,
                Schedulers.immediate(),
                Schedulers.immediate(),
                this.view,
                new RecentMediaRequest(0, 0, 0, "", 0));

        feedPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(view);

        inOrder.verify(view, times(1)).onGetRecentMediaStarted();
        inOrder.verify(view, times(1)).onGetRecentMediaSuccess(recentMediaResponse.getData());
        inOrder.verify(view, times(1)).onGetRecentMediaCompleted();
    }

    @Test
    public void getRecentMediaShouldReturnErrorToView() {
        Exception exception = new Exception();
        when(recentMediaDataSource.getRecentMedia(0, 0, 0, "", 0))
                .thenReturn(Observable.error(exception));

        FeedPresenter feedPresenter = new FeedPresenter(
                this.recentMediaDataSource,
                Schedulers.immediate(),
                Schedulers.immediate(),
                this.view,
                new RecentMediaRequest(0, 0, 0, "", 0));

        feedPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(view);

        inOrder.verify(view, times(1)).onGetRecentMediaStarted();
        inOrder.verify(view, times(1)).onGetRecentMediaError(exception);
        inOrder.verify(view, never()).onGetRecentMediaCompleted();
    }

}