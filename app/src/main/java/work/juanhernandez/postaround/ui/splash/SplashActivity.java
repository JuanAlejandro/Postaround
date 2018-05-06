package work.juanhernandez.postaround.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.ui.feed.FeedActivity;
import work.juanhernandez.postaround.ui.login.LoginActivity;
import work.juanhernandez.postaround.utils.PrefUtils;

import static work.juanhernandez.postaround.utils.Constants.ACCESS_TOKEN_KEY;

/**
 * Created by juan.hernandez on 5/5/18.
 * SplashActivity
 */

public class SplashActivity extends BaseActivity {
    private static final long SPLASH_TIME = 2 * 1000; // 2 seconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // todo: implement logic splashing
        splashing();
    }

    private void splashing() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME);

                    Class classToStart =
                            PrefUtils.getStringPref(SplashActivity.this, ACCESS_TOKEN_KEY) != null
                                    ? FeedActivity.class
                                    : LoginActivity.class;

                    startActivity(new Intent(SplashActivity.this, classToStart));
                    finish();

                } catch (Exception e) {
                    Log.e(SplashActivity.class.getName(), e.toString());
                }
            }
        }.start();
    }
}
