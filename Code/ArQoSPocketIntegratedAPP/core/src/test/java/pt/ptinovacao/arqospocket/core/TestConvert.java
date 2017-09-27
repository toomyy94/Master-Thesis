package pt.ptinovacao.arqospocket.core;

import org.junit.Test;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestConvert {

    @Test
    public void test() throws Exception {

        ArrayList<String> strings = new ArrayList<>();

        assertEquals("", SharedPreferencesManager.convertArrayListToString(strings));

        strings.add("teste");
        strings.add("teste1");
        strings.add("meio");
        strings.add("meio");

        strings.remove("teste1");

        strings.add("teste1");

        assertEquals("teste|meio|meio|teste1", SharedPreferencesManager.convertArrayListToString(strings));

    }

}
