package work.juanhernandez.postaround.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by juan.hernandez on 5/3/18.
 * BaseActivity
 */

public class BaseActivity extends AppCompatActivity {
    View progressView;
    /**
     * warning: you have to call it in your onCreate method
     */
    protected void initializeViews() {

    }

    public void setProgressView(View view){
        this.progressView = view;
    }

    public void showProgress() {
        progressView.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressView.setVisibility(View.GONE);
    }
}
