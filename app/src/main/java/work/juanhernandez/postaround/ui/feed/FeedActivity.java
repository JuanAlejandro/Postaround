package work.juanhernandez.postaround.ui.feed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.data.model.RecentMedia;
import work.juanhernandez.postaround.data.request.RecentMediaRequest;
import work.juanhernandez.postaround.data.retrofit.recentmedia.RecentMediaRemoteDataSource;
import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.utils.PrefUtils;

import static work.juanhernandez.postaround.utils.Constants.ACCESS_TOKEN_KEY;

/**
 * Created by juan.hernandez on 5/4/18.
 * FeedActivity
 */

public class FeedActivity extends BaseActivity implements FeedContract.View {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long LOCATION_REFRESH_TIME = 5 * 1000;
    private static final float LOCATION_REFRESH_DISTANCE = 10;
    private static final String LOCATION_ID_KEY = "LOCATION_ID_KEY";
    private static final int MAX_COUNT = 10;

    LocationManager locationManager;

    ProgressBar pbLoadingRecentMedia;

    TextView tvMessage;

    List<RecentMedia> recentMedia = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeViews();

        enableUserLocation();
    }

    @Override
    protected void initializeViews() {
        pbLoadingRecentMedia = findViewById(R.id.pbLoadingRecentMedia);
        setProgressView(pbLoadingRecentMedia);

        tvMessage = findViewById(R.id.tvMessage);
        setMessageView(tvMessage);
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            requestPermission();
        } else {
            // Access to the location has been granted to the app.
            updateLocationData();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // todo: display a nice message with the reason to get user's location
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            // LocationIG permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // I'm making sure to request permissions before execute this code
    @SuppressLint("MissingPermission")
    private void updateLocationData() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                getRecentMedia(location);
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, locationListener);
        }

        showProgress();
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                getRecentMedia(location);
            }

            hideProgress();
        }

        // region not used methods
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
        // endregion
    };

    private void getRecentMedia(Location location) {

        FeedPresenter feedPresenter = new FeedPresenter(
                new RecentMediaRemoteDataSource().getApi(),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                this,
                new RecentMediaRequest(
                        location.getLatitude(),
                        location.getLongitude(),
                        1000,
                        PrefUtils.getStringPref(this, ACCESS_TOKEN_KEY),
                        MAX_COUNT));

        feedPresenter.loadData();

        hideProgress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0]) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocationData();
            } else {
                // Permission was denied. todo: Display an error message.
                hideProgress();

                showMessage();
            }
        }
    }

    @Override
    public void onGetRecentMediaStarted() {
        showProgress();
    }

    @Override
    public void onGetRecentMediaCompleted() {
        hideProgress();
    }

    @Override
    public void onGetRecentMediaSuccess(List<RecentMedia> recentMedia) {
        Toast.makeText(this, "recent media fetched successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetRecentMediaError(Throwable e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }
}
