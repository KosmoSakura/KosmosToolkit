package cos.mos.utils;

import android.content.Context;
import android.os.Environment;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.

        System.out.println("1-" + Environment.getExternalStorageDirectory().getAbsolutePath());
        System.out.println("2-" + Environment.getExternalStorageDirectory());

        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("cos.mos.utils", appContext.getPackageName());
    }
}
