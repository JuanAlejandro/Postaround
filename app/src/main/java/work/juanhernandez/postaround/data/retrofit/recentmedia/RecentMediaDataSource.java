package work.juanhernandez.postaround.data.retrofit.recentmedia;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import work.juanhernandez.postaround.data.response.RecentMediaResponse;

/**
 * Created by juan.hernandez on 5/4/18.
 * RecentMediaDataSource
 */

public interface RecentMediaDataSource {
    @GET("v1/media/search")
    Observable<RecentMediaResponse> getRecentMedia(@Query("lat") double lat,
                                                   @Query("lng") double lng,
                                                   @Query("distance") long distance,
                                                   @Query("access_token") String accessToken,
                                                   @Query("count") int count);
}
