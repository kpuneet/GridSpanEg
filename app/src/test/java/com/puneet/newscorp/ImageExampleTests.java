package com.puneet.newscorp;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.ActivityController;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", sdk = Build.VERSION_CODES.M, constants = BuildConfig.class)
public class ImageExampleTests {

    MainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    @Test
    public void mainActivity() {
        ActivityController mainActivityActivityController = Robolectric.buildActivity(MainActivity.class);
        mainActivity = (MainActivity) mainActivityActivityController.create().start().resume().visible().get();
        await().atMost(5, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            public Boolean call() {
                return mainActivity.getSupportFragmentManager().findFragmentByTag(ImagesFragment.class.getSimpleName()) != null;
            }
        });
        mainActivityActivityController.pause().stop().userLeaving().destroy();
    }
}