package work.juanhernandez.postaround.data.retrofit.recentmedia;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import work.juanhernandez.postaround.data.response.RecentMediaResponse;
import work.juanhernandez.postaround.utils.Constants;

/**
 * Created by juan.hernandez on 5/4/18.
 * Retrofit description of the recent media endpoint
 */

public class FeedRemoteDataSource implements FeedDataSource {

    private FeedDataSource api;

    public FeedRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        this.api = retrofit.create(FeedDataSource.class);
    }

    public FeedDataSource getApi() {
        return api;
    }

    @Override
    public Observable<RecentMediaResponse> getRecentMedia(double lat, double lng, int distance,
                                                          String accessToken, int count) {
        return this.api.getRecentMedia(lat, lng, distance, accessToken, count);
    }
}
