package work.juanhernandez.postaround;

import android.app.Application;
import android.content.Context;

/**
 * Created by juan.hernandez on 5/6/18.
 */

public class PostaroundApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        PostaroundApplication.context = getApplicationContext();
    }

    public static Context getContext(){
        return PostaroundApplication.context;
    }
}
