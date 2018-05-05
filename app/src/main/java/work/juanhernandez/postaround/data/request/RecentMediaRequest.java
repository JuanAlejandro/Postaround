package work.juanhernandez.postaround.data.request;

/**
 * Created by juan.hernandez on 5/5/18.
 * RecentMediaRequest
 */

public class RecentMediaRequest {

    double lat;

    double lng;

    long distance;

    String accessToken;

    int count;

    public RecentMediaRequest(double lat, double lng, long distance, String accessToken, int count) {
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.accessToken = accessToken;
        this.count = count;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public long getDistance() {
        return distance;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getCount() {
        return count;
    }
}
