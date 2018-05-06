package work.juanhernandez.postaround.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import work.juanhernandez.postaround.R;
import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.ui.feed.FeedActivity;
import work.juanhernandez.postaround.utils.PrefUtils;

import static work.juanhernandez.postaround.utils.Constants.ACCESS_TOKEN_KEY;

/**
 * Created by juan.hernandez on 5/5/18.
 * LoginActivity
 */

public class LoginActivity extends BaseActivity {
    public static final int INSTAGRAM_LOGIN = 1;

    public static final String LOGIN_RESULT = "LOGIN_RESULT";
    public static final int LOGIN_OK = 1;
    public static final int LOGIN_ERROR = 2;
    public static final int LOGIN_UNDEFINED = 3;

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginWithInstagramClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, InstagramLogin.class);
        startActivityForResult(intent, INSTAGRAM_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INSTAGRAM_LOGIN) {
            if (resultCode == RESULT_OK) {
                // the activity executed successfully
                switch (data.getIntExtra(LOGIN_RESULT, LOGIN_UNDEFINED)) {
                    case LOGIN_OK:
                        PrefUtils.setStringPref(this, ACCESS_TOKEN_KEY,
                                data.getStringExtra(ACCESS_TOKEN));
                        startActivity(new Intent(LoginActivity.this, FeedActivity.class));
                        finish();
                        break;

                    case LOGIN_ERROR:
                        // todo: show error message
                        break;

                    case LOGIN_UNDEFINED:
                    default:
                        // todo: show undefined message
                        break;
                }
            }
        }
    }
}
