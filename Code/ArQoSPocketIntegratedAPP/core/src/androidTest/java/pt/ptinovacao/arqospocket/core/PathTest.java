package pt.ptinovacao.arqospocket.core;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PathTest {

    private static final String BASE_DIR = "arqospocket";

    private static final String TESTS_DIR = "tests";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("pt.ptinovacao.arqospocket.core.test", appContext.getPackageName());
    }

    @Test
    public void pathIsCorrect() {
        File storage = Environment.getExternalStorageDirectory();
        assertEquals("/storage/emulated/0", storage.getAbsolutePath());

        String pathA = storage.getAbsolutePath() + File.separator + BASE_DIR + File.separator + TESTS_DIR;
        String pathB = new File(new File(storage, BASE_DIR), TESTS_DIR).getAbsolutePath();
        assertEquals(pathA, pathB);
    }
}
