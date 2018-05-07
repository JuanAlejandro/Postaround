package work.juanhernandez.postaround.ui.feed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.data.model.RecentMedia;
import work.juanhernandez.postaround.data.request.RecentMediaRequest;
import work.juanhernandez.postaround.data.retrofit.recentmedia.FeedRemoteDataSource;
import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.ui.feed.adapter.FeedAdapter;
import work.juanhernandez.postaround.ui.login.LoginActivity;
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

    private static final int MAX_COUNT = 10;
    private static final int MAX_DISTANCE = 5000;
    private static final int DEFAULT_DISTANCE = 500;

    // region location vars
    LocationManager locationManager;
    Location location;
    // endregion

    // region view vars
    ProgressBar pbLoadingRecentMedia;
    ImageView ivRuler;
    TextView tvEmpty;
    RecyclerView rvFeed;
    RecyclerView.LayoutManager layoutManager;
    FeedAdapter feedAdapter;
    LinearLayout llPermissionsDenied;
    LinearLayout llWeNeedYourPermission;
    LinearLayout llEmptySearch;
    LinearLayout llWormHole;
    // endregion

    FeedPresenter feedPresenter;

    List<RecentMedia> recentMedia = new ArrayList<>();

    // distance to search recent media
    int distance = DEFAULT_DISTANCE;

    // the app will search until it finds recent media if autoSearch is true
    boolean autoSearch = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeViews();

        initializeToolbar();

        getUserLocation();
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

        ivRuler = findViewById(R.id.ivRuler);

        rvFeed = findViewById(R.id.rvFeed);
        layoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(layoutManager);
        feedAdapter = new FeedAdapter(this.recentMedia);
        rvFeed.setAdapter(feedAdapter);

        llEmptySearch = findViewById(R.id.llEmptySearch);
        llWeNeedYourPermission = findViewById(R.id.llWeNeedYourPermission);
        llWormHole = findViewById(R.id.llWormHole);
        llPermissionsDenied = findViewById(R.id.llPermissionsDenied);

        tvEmpty = findViewById(R.id.tvEmpty);
    }

    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            llWeNeedYourPermission.setVisibility(View.VISIBLE);
        } else {
            // Access to the location has been granted to the app.
            updateLocationData();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
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
            // show distance selector option
            ivRuler.setVisibility(View.VISIBLE);

            // if the device has recent location saved, use it
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                getRecentMedia(location, distance);
                return;
            }

            // if not request location update to the system
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, locationListener);

            showProgress();
        } else {
            llWormHole.setVisibility(View.VISIBLE);
        }

    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                FeedActivity.this.location = location;
                getRecentMedia(location, distance);
            }
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
        feedPresenter = new FeedPresenter(
                new FeedRemoteDataSource().getApi(),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                this,
                new RecentMediaRequest(
                        location.getLatitude(),
                        location.getLongitude(),
                        distance,
                        PrefUtils.getStringPref(this, ACCESS_TOKEN_KEY),
                        MAX_COUNT));

        // notify the presenter the view is ready to receive data
        feedPresenter.subscribe();
    }

    void showDistancePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_select_distance, null);

        SeekBar sbDistanceSelector = dialogView.findViewById(R.id.sbDistanceSelector);
        sbDistanceSelector.setMax(MAX_DISTANCE);
        sbDistanceSelector.setProgress(distance);

        TextView tvDistance = dialogView.findViewById(R.id.tvDistance);
        tvDistance.setText(String.format(Locale.getDefault(), "%d meters", distance));

        sbDistanceSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvDistance.setText(String.format(Locale.getDefault(), "%d meters", i));
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
                    // deactivate auto-search since the user wants to see posts around a specific distance
                    autoSearch = false;

                    if (llEmptySearch.getVisibility() == View.VISIBLE)
                        llEmptySearch.setVisibility(View.GONE);

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
                hideProgress();
                // show request permission message
                llPermissionsDenied.setVisibility(View.VISIBLE);
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
        this.recentMedia.clear();

        if (recentMedia.size() != 0) {
            this.recentMedia.addAll(recentMedia);
            feedAdapter.notifyDataSetChanged();
            return;
        }

        // if the search was triggered by the user and there's no results
        // show empty message
        if (!autoSearch) {
            llEmptySearch.setVisibility(View.VISIBLE);
            autoSearch = true;
            return;
        }

        // if posts aren't found, increase the distance automatically
        distance += DEFAULT_DISTANCE;

        // if distance is less than max distance keep looking
        if (distance < MAX_DISTANCE) {
            feedPresenter.subscribe(distance);
        } else if (distance >= MAX_DISTANCE) {
            // if distance is greater or equal than MAX_DISTANCE search one more time
            distance = MAX_DISTANCE;
            feedPresenter.subscribe(distance);
        } else {
            // if no posts aren't found show empty message
            tvEmpty.setText(String.format(getString(R.string.no_instagramers_around_max), MAX_DISTANCE));
            llEmptySearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetRecentMediaError(Throwable e) {
        Log.e(FeedActivity.class.getName(), e.toString());
        startActivity(new Intent(FeedActivity.this, LoginActivity.class));
        PrefUtils.setStringPref(this, ACCESS_TOKEN_KEY, null);
        Toast.makeText(this, R.string.something_wrong_happened, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void distancePickerClicked(View view) {
        showDistancePickerDialog();
    }

    public void getPermissions(View view) {
        switch (view.getId()){
            case R.id.btnReqPermFirstTime:
                llWeNeedYourPermission.setVisibility(View.GONE);
                break;
            case R.id.btnReqPermAgain:
                llPermissionsDenied.setVisibility(View.GONE);
                break;
        }

        requestPermission();
    }

    public void reUpdateLocationData(View view) {
        switch (view.getId()){
            case R.id.btnEmptySearch:
                llEmptySearch.setVisibility(View.GONE);
                break;
            case R.id.btnWormHole:
                llWormHole.setVisibility(View.GONE);
                break;
        }

        updateLocationData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        feedPresenter.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        feedPresenter.onDestroy();
    }
}
