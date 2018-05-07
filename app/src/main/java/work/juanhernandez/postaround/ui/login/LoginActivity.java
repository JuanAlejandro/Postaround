package work.juanhernandez.postaround.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

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

    // executed when sign in button is pressed
    public void loginWithInstagramClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, IGLoginActivity.class);
        // start activity for login result
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
                        // login successful
                        PrefUtils.setStringPref(this, ACCESS_TOKEN_KEY,
                                data.getStringExtra(ACCESS_TOKEN));
                        // go to FeedActivity
                        startActivity(new Intent(LoginActivity.this, FeedActivity.class));
                        finish();
                        break;

                    case LOGIN_ERROR:
                        Toast.makeText(this, R.string.something_wrong_happened, Toast.LENGTH_SHORT).show();
                        break;

                    case LOGIN_UNDEFINED:
                    default:
                        Toast.makeText(this, R.string.you_found_wormhole,
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }
}
