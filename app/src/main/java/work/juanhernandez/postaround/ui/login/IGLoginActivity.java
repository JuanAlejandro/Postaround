package work.juanhernandez.postaround.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import okhttp3.internal.Util;
import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.utils.Constants;
import work.juanhernandez.postaround.utils.Utils;

import static work.juanhernandez.postaround.MainActivity.ACCESS_TOKEN;
import static work.juanhernandez.postaround.MainActivity.LOGIN_ERROR;
import static work.juanhernandez.postaround.MainActivity.LOGIN_OK;
import static work.juanhernandez.postaround.MainActivity.LOGIN_RESULT;

/**
 * Created by juan.hernandez on 5/3/18.
 * IGLoginActivity
 */

public class IGLoginActivity extends BaseActivity {
    private final String url = Constants.BASE_URL
            + "oauth/authorize/?client_id=" +
            Constants.CLIENT_ID +
            "&redirect_uri=" +
            Constants.REDIRECT_URI +
            "&response_type=token" +
            "&display=touch&scope=public_content";

    private WebView webView;

    private ProgressBar pbLoading;

    private LinearLayout llDontHaveAccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iglogin);

        initializeViews();
    }

    @Override
    protected void initializeViews() {
        initializeWebView();

        pbLoading = findViewById(R.id.pbLoading);

        llDontHaveAccess = findViewById(R.id.llDontHaveAccess);
    }

    private void initializeWebView() {
        webView = findViewById(R.id.wvLogin);
        // in order to reload instagram login page
        Utils.clearCookies(this);

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            String accessToken = "";

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // to get the access token
                if (url.contains("#access_token=")) {
                    Uri uri = Uri.parse(url);
                    accessToken = uri.getEncodedFragment();
                    // get the token after '='
                    accessToken = accessToken.substring(accessToken.lastIndexOf("=") + 1);
                    // save access token in bundle
                    setActivityResult(LOGIN_OK, accessToken);
                    return true;
                } else if (url.contains("?error")) {
                    setActivityResult(LOGIN_ERROR, null);
                    return false;
                } else {
                    return false;
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                webView.setVisibility(View.GONE);
                llDontHaveAccess.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setActivityResult(int loginResult, String accessToken) {
        Intent intent = new Intent();
        intent.putExtra(LOGIN_RESULT, loginResult);
        if (accessToken != null)
            intent.putExtra(ACCESS_TOKEN, accessToken);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void contactMeClicked(View view) {
        Utils.writeAnEmail(IGLoginActivity.this);
    }
}