package pt.ptinovacao.arqospocket.core;

import org.junit.Before;
import org.junit.Test;

import pt.ptinovacao.arqospocket.core.http.client.HttpClient;

/**
 * Tests for the {@link HttpClient}.
 * <p>
 * Created by Emílio Simões on 11-05-2017.
 */
public class HttpClientTest {

    private static final String RESPONSE =
            "<notification-processor result-code=\"001\" result-message=\"Parser not found! No path info defined: /\"/>";

    private static String body = "{\n" + "   \"probenotification\":{\n" + "      \"serialnumber\":\"00139500F85F\",\n" +
            "      \"macaddress\":\"00139500F85F\",\n" + "      \"ipaddress\":\"10.112.85.102\",\n" +
            "      \"equipmenttype\":\"10\",\n" + "      \"timestamp\":\"20120718145054\",\n" +
            "      \"associatedResponse\":{\n" + "         \"probeResults\":[\n" + "            {\n" +
            "               \"modulo\":4,\n" + "               \"dataini\":\"20120718145039\",\n" +
            "               \"datafim\":\"20120718145219\",\n" + "               \"testeid\":\"T423485\",\n" +
            "               \"data\":[\n" +
            "                  \"423486 | 1 | 2012-07-18 14:50:39 | 2012-07-18 14:50:54 | 1 | 89351060000101666632 | 405-30460,-71,4,2,TMN |  | OK:M3021: Atendimento com deteccao de 2100Hz |1 |967909837 |9016 |  | |0 | | | | |\"\n" +
            "               ]\n" + "            },\n" + "            {\n" + "               \"modulo\":0,\n" +
            "               \"dataini\":\"20120718145039\",\n" + "               \"datafim\":\"20120718145219\",\n" +
            "               \"testeid\":\"T423888\",\n" + "               \"data\":[\n" +
            "                  \"423888 | 2 | 2012-07-18 14:50:50 | 2012-07-18 14:50:54 | 3 | 89351060000103552848 | 405-45460,-60,4,2,TMN |  | OK |6178 |918 4 |848 4 | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |3 |\"\n" +
            "               ]\n" + "            }\n" + "         ]\n" + "      }\n" + "   }\n" + "}";

    private HttpClient client;

    @Before
    public void init() {
       // client = new HttpClient();
    }

    @Test
    public void httpClient_executesTestRequest() {
        //
        //        String response = client.testRequest(body);
        //        System.out.println("response:" + response);
        //        Assert.assertEquals(RESPONSE, response);
    }
}
