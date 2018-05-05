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

public class RecentMediaRemoteDataSource implements RecentMediaDataSource {

    private RecentMediaDataSource api;

    public RecentMediaRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        this.api = retrofit.create(RecentMediaRemoteDataSource.class);
    }

    @Override
    public Observable<RecentMediaResponse> getRecentMedia(double lat, double lng, long distance,
                                                          String accessToken, int count) {
        return this.api.getRecentMedia(lat, lng, distance, accessToken, count);
    }
}
