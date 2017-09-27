package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro on 19/05/2017.
 */
public class ProbeHardware {

    private static final String VER_HARD = "verHard";

    private static final String VER = "ver";

    private static final String MAC = "mac";

    private static final String TIPO_R1 = "tipo_r1";

    private static final String VER_R1 = "ver_r1";

    private static final String IMEI_R1 = "imei_r1";

    private static final String IMSI_R1 = "imsi_r1";

    private static final String ID_R3 = "id_r3";

    private static final String TIPO_R3 = "tipo_r3";

    private static final String VER_R3 = "ver_r3";

    private static final String IMSI_R3 = "imsi_r3";

    private static final String IMEI_R3 = "imei_r3";

    @SerializedName(VER_HARD)
    private String verHard;

    @SerializedName(VER)
    private String ver;

    @SerializedName(MAC)
    private String mac;

    @SerializedName(TIPO_R1)
    private String tipoR1;

    @SerializedName(VER_R1)
    private String verR1;

    @SerializedName(IMEI_R1)
    private String imeiR1;

    @SerializedName(IMSI_R1)
    private String imsiR1;

    @SerializedName(ID_R3)
    private String idR3;

    @SerializedName(TIPO_R3)
    private String tipoR3;

    @SerializedName(VER_R3)
    private String verR3;

    @SerializedName(IMSI_R3)
    private String imsiR3;

    @SerializedName(IMEI_R3)
    private String imeiR3;

    public String getVerHard() {
        return verHard;
    }

    public void setVerHard(String verHard) {
        this.verHard = verHard;
    }

    public static String getVER() {
        return MAC;
    }

    public static String getTipoR1() {
        return TIPO_R1;
    }

    public void setTipoR1(String tipoR1) {
        this.tipoR1 = tipoR1;
    }

    public static String getVerR1() {
        return VER_R1;
    }

    public void setVerR1(String verR1) {
        this.verR1 = verR1;
    }

    public static String getImeiR1() {
        return IMEI_R1;
    }

    public void setImeiR1(String imeiR1) {
        this.imeiR1 = imeiR1;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getImsiR1() {
        return imsiR1;
    }

    public void setImsiR1(String imsiR1) {
        this.imsiR1 = imsiR1;
    }

    public static String getIdR3() {
        return ID_R3;
    }

    public void setIdR3(String idR3) {
        this.idR3 = idR3;
    }

    public static String getTipoR3() {
        return TIPO_R3;
    }

    public void setTipoR3(String tipoR3) {
        this.tipoR3 = tipoR3;
    }

    public static String getVerR3() {
        return VER_R3;
    }

    public void setVerR3(String verR3) {
        this.verR3 = verR3;
    }

    public static String getImsiR3() {
        return IMSI_R3;
    }

    public void setImsiR3(String imsiR3) {
        this.imsiR3 = imsiR3;
    }

    public static String getImeiR3() {
        return IMEI_R3;
    }

    public void setImeiR3(String imeiR3) {
        this.imeiR3 = imeiR3;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
