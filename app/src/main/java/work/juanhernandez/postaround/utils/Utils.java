package work.juanhernandez.postaround.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by juan.hernandez on 5/6/18.
 * Utils
 */

public class Utils {
    public static void writeAnEmail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jalejandro.hperez@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sandbox invite");
        // todo: resolve activity

        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(Intent.createChooser(intent, "Send Email"));
        else
            Toast.makeText(context, "You don't have email app installed", Toast.LENGTH_SHORT).show();
    }
}
