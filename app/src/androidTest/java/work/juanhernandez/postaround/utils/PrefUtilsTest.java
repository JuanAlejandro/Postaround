package work.juanhernandez.postaround.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by juan.hernandez on 5/5/18.
 */
@RunWith(AndroidJUnit4.class)
public class PrefUtilsTest {
    @Test
    public void prefsAreSavedCorrectly() throws Exception {
        String key = "foo_key";
        String value = "This is the String to save";
        Context appContext = InstrumentationRegistry.getTargetContext();

        PrefUtils.setStringPref(appContext, key, value);
        assertEquals(value, PrefUtils.getStringPref(appContext, key));
    }
}