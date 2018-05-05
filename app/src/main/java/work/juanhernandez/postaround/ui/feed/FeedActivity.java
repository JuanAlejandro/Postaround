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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.data.model.RecentMedia;
import work.juanhernandez.postaround.data.request.RecentMediaRequest;
import work.juanhernandez.postaround.data.retrofit.recentmedia.RecentMediaRemoteDataSource;
import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.ui.feed.adapter.FeedAdapter;
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
    private static final int MAX_DISTANCE = 5000;
    private static final int DEFAULT_DISTANCE = 500;

    LocationManager locationManager;
    Location location;

    ProgressBar pbLoadingRecentMedia;

    TextView tvMessage;

    List<RecentMedia> recentMedia = new ArrayList<>();

    RecyclerView rvFeed;
    RecyclerView.LayoutManager layoutManager;
    FeedAdapter feedAdapter;

    FeedPresenter feedPresenter;

    int distance = DEFAULT_DISTANCE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeViews();

        initializeToolbar();

        enableUserLocation();
    }

    private void initializeToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initializeViews() {
        pbLoadingRecentMedia = findViewById(R.id.pbLoadingRecentMedia);
        setProgressView(pbLoadingRecentMedia);

        tvMessage = findViewById(R.id.tvMessage);
        setMessageView(tvMessage);

        rvFeed = findViewById(R.id.rvFeed);
        layoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(layoutManager);
        feedAdapter = new FeedAdapter(this.recentMedia);
        rvFeed.setAdapter(feedAdapter);
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
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                getRecentMedia(location, distance);
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
                FeedActivity.this.location = location;
                getRecentMedia(location, distance);
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

    private void getRecentMedia(Location location, int distance) {
        showProgress();

        recentMedia.clear();

        feedPresenter = new FeedPresenter(
                new RecentMediaRemoteDataSource().getApi(),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                this,
                new RecentMediaRequest(
                        location.getLatitude(),
                        location.getLongitude(),
                        distance,
                        PrefUtils.getStringPref(this, ACCESS_TOKEN_KEY),
                        MAX_COUNT));

        feedPresenter.loadData();
    }

    void showDistancePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_select_distance, null);

        SeekBar sbDistanceSelector = dialogView.findViewById(R.id.sbDistanceSelector);
        sbDistanceSelector.setMax(MAX_DISTANCE);
        sbDistanceSelector.setProgress(distance);

        TextView tvDistance = dialogView.findViewById(R.id.tvDistance);
        tvDistance.setText(String.valueOf(distance));

        sbDistanceSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvDistance.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                FeedActivity.this.distance = seekBar.getProgress();
            }
        });

        builder.setView(dialogView)
                // add action buttons
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    getRecentMedia(location, distance);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());

        builder.create().show();
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
        this.recentMedia.addAll(recentMedia);
        feedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetRecentMediaError(Throwable e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }

    public void distancePickerClicked(View view) {
        showDistancePickerDialog();
    }
}
