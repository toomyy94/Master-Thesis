package pt.ptinovacao.arqospocket.core;

import org.junit.Test;

import pt.ptinovacao.arqospocket.core.utils.NumberUtils;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestNumberUtils {

    @Test
    public void testRegex() throws Exception {
        assertEquals(true, NumberUtils.isNumberToSendSms("323"));
        assertEquals(true, NumberUtils.isNumberToSendSms("+323"));
        assertEquals(false, NumberUtils.isNumberToSendSms("3df23"));
        assertEquals(false, NumberUtils.isNumberToSendSms(""));
        assertEquals(false, NumberUtils.isNumberToSendSms("sdsd"));
    }

}
