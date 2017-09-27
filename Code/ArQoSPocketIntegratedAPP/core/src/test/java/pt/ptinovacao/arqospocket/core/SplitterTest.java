package pt.ptinovacao.arqospocket.core;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SplitterTest {

    @Test
    public void splitter_hasCorrectOutput() throws Exception {
        String source = "1|1||0|60|36|0|1234|1234|";
        Iterable<String> split = Splitter.on('|').trimResults().split(source);
        List<String> list = Lists.newArrayList(split);
        assertEquals(10, list.size());

        source = "";
        split = Splitter.on('|').trimResults().split(source);
        list = Lists.newArrayList(split);
        assertEquals(1, list.size());
    }

    @Test
    public void splitter_handlesNulls() throws Exception {
        Iterable<String> split = Splitter.on('|').trimResults().split(Strings.nullToEmpty(null));
        List<String> list = Lists.newArrayList(split);
        assertEquals(1, list.size());
    }
}
