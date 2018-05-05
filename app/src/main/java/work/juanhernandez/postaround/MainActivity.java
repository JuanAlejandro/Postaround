package work.juanhernandez.postaround;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import work.juanhernandez.postaround.ui.base.BaseActivity;
import work.juanhernandez.postaround.ui.login.InstagramLogin;
import work.juanhernandez.postaround.utils.PrefUtils;

import static work.juanhernandez.postaround.utils.Constants.ACCESS_TOKEN_KEY;

public class MainActivity extends BaseActivity {
    public static final int INSTAGRAM_LOGIN = 1;

    public static final String LOGIN_RESULT = "LOGIN_RESULT";
    public static final int LOGIN_OK = 1;
    public static final int LOGIN_ERROR = 2;
    public static final int LOGIN_UNDEFINED = 3;

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    // views
    TextView tvState;

    TextView tvAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
    }

    protected void initializeViews() {
        tvState = findViewById(R.id.tvState);

        tvAccessToken = findViewById(R.id.tvAccessToken);
    }

    public void loginWithInstagramClicked(View view) {
        Intent intent = new Intent(MainActivity.this, InstagramLogin.class);
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
                        tvState.setText(data.getStringExtra(ACCESS_TOKEN));
                        break;

                    case LOGIN_ERROR:
                        tvState.setText(R.string.login_failed);
                        break;

                    case LOGIN_UNDEFINED:
                    default:
                        tvState.setText(R.string.login_undefined);
                        break;
                }
            }
        }
    }
}
