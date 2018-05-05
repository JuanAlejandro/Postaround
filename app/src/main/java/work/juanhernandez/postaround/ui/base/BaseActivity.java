package work.juanhernandez.postaround.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by juan.hernandez on 5/3/18.
 * BaseActivity
 */

public class BaseActivity extends AppCompatActivity {
    View progressView;

    View messageView;

    /**
     * warning: you have to call it in your onCreate method
     */
    protected void initializeViews() {

    }

    public void setProgressView(View view) {
        this.progressView = view;
    }

    public void showProgress() {
        if (progressView != null)
            progressView.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (progressView != null)
            progressView.setVisibility(View.GONE);
    }

    public void setMessageView(View view) {
        this.messageView = view;
    }

    public void showMessage() {
        if (messageView != null)
            messageView.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {
        if (messageView != null)
            messageView.setVisibility(View.GONE);
    }
}
