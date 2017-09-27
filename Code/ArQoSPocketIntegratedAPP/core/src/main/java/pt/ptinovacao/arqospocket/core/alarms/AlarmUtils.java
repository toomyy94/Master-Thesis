package pt.ptinovacao.arqospocket.core.alarms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class is responsible to check alarms generation.
 * <p>
 * Created by Tomás Rodrigues on 11-09-2017.
 */
public class AlarmUtils  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmUtils.class);

    //final variables
    public static final String INIT = "Init";
    public static final String INICIO = "Inicio";
    public static final String END = "End";
    public static final String FIM = "Fim";

    //agents
    public static final String FW = "FW";
    public static final String SG = "SG";


    public int getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            int cpuTemperature = Math.round(Float.parseFloat(line) / 1000.0f);

            return cpuTemperature;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("could not get CPU temperature from File");
        }
        return 0;
    }
}