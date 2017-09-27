package pt.ptinovacao.arqospocket.core.serialization.entities.results.type2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro on 19/05/2017.
 */
public class ModuleStatusRequest {

    private static final String G_CALL_STATUS = "g_call_status";

    private static final String TESTE = "teste";

    private static final String ID_TAREFA = "id_tarefa";

    private static final String ID_TIMESLOT = "id_timeslot";

    private static final String TAREFA_N = "tarefa_n";

    private static final String NOME_TESTE = "nome_teste";

    private static final String TAREFA = "tarefa";

    private static final String SINAL = "sinal";

    private static final String REDE = "rede";

    private static final String CEL = "cel";

    private static final String CALL = "call";

    private static final String GPS = "gps";

    private static final String TEMPERATURA = "temperatura";

    private static final String DF = "df";

    private static final String SYNC = "sync";

    private static final String PWR_STATUS = "pwr_status";

    private static final String BAT_LEVEL = "bat_level";

    private static final String BAT_TIME = "bat_time";

    private static final String ICCID = "iccid";

    private static final String SIM_TYPE = "sim_type";

    private static final String BER = "ber";

    private static final String MODEM = "modem";

    private static final String GSMMODE = "gsmmode";

    private static final String GSMTYPE = "gsmtype";

    private static final String GSMROAM = "gsmroam";

    private static final String country_code = "country_code";

    private static final String PSC = "psc";

    private static final String ECIO = "ecio";

    private static final String RSCP = "rscp";

    private static final String MCC = "mcc";

    private static final String MNC = "mnc";

    private static final String ARFCN = "arfcn";

    private static final String RAC = "rac";

    private static final String BSIC = "bsic";

    private static final String STATUS = "status";

    private static final String BW = "bw";

    private static final String RXCH = "rxch";

    private static final String TXCH = "txch";

    private static final String SINR = "sinr";

    private static final String RSRP = "rsrp";

    private static final String RSRQ = "rsrq";

    private static final String PPPOE_ADDR = "pppoe_addr";

    private static final String PPPOE_GW = "pppoe_gw";

    private static final String PPPOE_MASK = "pppoe_mask";

    private static final String PPPOE_DNS1 = "pppoe_dns1";

    private static final String PPPOE_DNS2 = "pppoe_dns2";

    private static final String WIFI_ADDR = "wifi_addr";

    private static final String WIFI_GW = "wifi_gw";

    private static final String WIFI_MASK = "wifi_mask";

    private static final String WIFI_DNS1 = "wifi_dns1";

    private static final String WIFI_DNS2 = "wifi_dns2";

    private static final String WIFI_LEASE = "wifi_lease";

    private static final String WIFI_DOMAIN = "wifi_domain";

    private static final String WIFI_RENEW = "wifi_renew";

    private static final String WIFI_EXPIRE = "wifi_expire";

    private static final String WIFI_REBIND = "wifi_rebind";

    private static final String WIFI_BSSID = "wifi_bssid";

    private static final String WIFI_HIDDEN_SSID = "wifi_hidden_ssid";

    private static final String WIFI_NETWORK_ID = "wifi_network_id";

    private static final String WIFI_RSSI = "wifi_rssi";

    private static final String WIFI_SSID = "wifi_ssid";

    private static final String WIFI_CONNECTION_STATE = "wifi_connection_state";

    private static final String WIFI_QUALITY = "wifi_quality";

    private static final String WIFI_NOISE_LEVEL = "wifi_noise_level";

    private static final String WIFI_SNR = "wifi_snr";

    private static final String WIFI_MCS = "wifi_mcs";

    private static final String WIFI_BW = "wifi_bw";

    private static final String SIM_LOCK_STATUS = "sim_lock_status";

    @SerializedName(G_CALL_STATUS)
    private Integer gCallStatus;

    @SerializedName(TESTE)
    private Integer teste;

    @SerializedName(ID_TAREFA)
    private Integer idiTarefa;

    @SerializedName(ID_TIMESLOT)
    private Integer idTimeslot;

    @SerializedName(TAREFA_N)
    private Long tarefaN;

    @SerializedName(NOME_TESTE)
    private String nomeTeste;

    @SerializedName(TAREFA)
    private Integer tarefa;

    @SerializedName(SINAL)
    private Integer signal;

    @SerializedName(REDE)
    private String rede;

    @SerializedName(CALL)
    private String call;

    @SerializedName(CEL)
    private String cel;

    @SerializedName(GPS)
    private String gps;

    @SerializedName(TEMPERATURA)
    private Integer temperatura;

    @SerializedName(DF)
    private Integer df;

    @SerializedName(SYNC)
    private String sync;

    @SerializedName(PWR_STATUS)
    private Integer pwrStatus;

    @SerializedName(BAT_LEVEL)
    private Float batLevel;

    @SerializedName(BAT_TIME)
    private Integer batTime;

    @SerializedName(ICCID)
    private String iccid;

    @SerializedName(SIM_TYPE)
    private Integer simType;

    @SerializedName(BER)
    private Float ber;

    @SerializedName(MODEM)
    private Integer modem;

    @SerializedName(GSMMODE)
    private Integer gsmMode;

    @SerializedName(GSMTYPE)
    private Integer gsmType;

    @SerializedName(GSMROAM)
    private Integer gsmRoam;

    @SerializedName(country_code)
    private String countryCode;

    @SerializedName(PSC)
    private Integer psc;

    @SerializedName(ECIO)
    private String ecio;

    @SerializedName(RSCP)
    private Integer rscp;

    @SerializedName(MCC)
    private Integer mcc;

    @SerializedName(MNC)
    private Integer mnc;

    @SerializedName(ARFCN)
    private Integer arfcn;

    @SerializedName(RAC)
    private Integer rac;

    @SerializedName(BSIC)
    private Integer bsic;

    @SerializedName(STATUS)
    private Integer status;

    @SerializedName(BW)
    private Integer bw;

    @SerializedName(RXCH)
    private Integer rxch;

    @SerializedName(TXCH)
    private Integer txch;

    @SerializedName(SINR)
    private Float sinr;

    @SerializedName(RSRP)
    private Integer rsrp;

    @SerializedName(RSRQ)
    private Integer rsrq;

    @SerializedName(PPPOE_ADDR)
    private String pppoeAddr;

    @SerializedName(PPPOE_GW)
    private String pppoeGw;

    @SerializedName(PPPOE_MASK)
    private String pppoeMask;

    @SerializedName(PPPOE_DNS1)
    private String pppoeDns1;

    @SerializedName(PPPOE_DNS2)
    private String pppoeDns2;

    @SerializedName(WIFI_ADDR)
    private String wifiAddr;

    @SerializedName(WIFI_GW)
    private String wifiGw;

    @SerializedName(WIFI_MASK)
    private String wifiMask;

    @SerializedName(WIFI_DNS1)
    private String wifiDns1;

    @SerializedName(WIFI_DNS2)
    private String wifiDns2;

    @SerializedName(WIFI_LEASE)
    private Integer wifiLease;

    @SerializedName(WIFI_DOMAIN)
    private String wifiDomain;

    @SerializedName(WIFI_RENEW)
    private Integer wifiRenew;

    @SerializedName(WIFI_EXPIRE)
    private Integer wifiExpire;

    @SerializedName(WIFI_REBIND)
    private Integer wifiRebind;

    @SerializedName(WIFI_BSSID)
    private String wifiBssid;

    @SerializedName(WIFI_HIDDEN_SSID)
    private Integer wifiHiddenSsid;

    @SerializedName(WIFI_NETWORK_ID)
    private Integer wifiNetworkId;

    @SerializedName(WIFI_RSSI)
    private Integer wifiRssi;

    @SerializedName(WIFI_SSID)
    private String wifiSsid;

    @SerializedName(WIFI_CONNECTION_STATE)
    private Integer wifiConnectionState;

    @SerializedName(WIFI_QUALITY)
    private Integer wifiQuality;

    @SerializedName(WIFI_NOISE_LEVEL)
    private Integer wifiNoiseLevel;

    @SerializedName(WIFI_SNR)
    private Integer wifiSnr;

    @SerializedName(WIFI_MCS)
    private Integer wifiMcs;

    @SerializedName(WIFI_BW)
    private Integer wifiBw;

    @SerializedName(SIM_LOCK_STATUS)
    private Integer sinLockStatus;

    public void setgCallStatus(Integer gCallStatus) {
        this.gCallStatus = gCallStatus;
    }

    public Integer getTeste() {
        return teste;
    }

    public void setTeste(Integer teste) {
        this.teste = teste;
    }

    public Integer getIdiTarefa() {
        return idiTarefa;
    }

    public void setIdiTarefa(Integer idiTarefa) {
        this.idiTarefa = idiTarefa;
    }

    public void setIdTimeslot(Integer idTimeslot) {
        this.idTimeslot = idTimeslot;
    }

    public void setTarefaN(Long tarefaN) {
        this.tarefaN = tarefaN;
    }

    public void setNomeTeste(String nomeTeste) {
        this.nomeTeste = nomeTeste;
    }

    public Integer getTarefa() {
        return tarefa;
    }

    public void setTarefa(Integer tarefa) {
        this.tarefa = tarefa;
    }

    public Integer getSignal() {
        return signal;
    }

    public void setSignal(Integer signal) {
        this.signal = signal;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public String getNomeTeste() {
        return nomeTeste;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Integer getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Integer temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getDf() {
        return df;
    }

    public void setDf(Integer df) {
        this.df = df;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public void setPwrStatus(Integer pwrStatus) {
        this.pwrStatus = pwrStatus;
    }

    public void setBatLevel(Float batLevel) {
        this.batLevel = batLevel;
    }

    public void setBatTime(Integer batTime) {
        this.batTime = batTime;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public void setSimType(Integer simType) {
        this.simType = simType;
    }

    public Float getBer() {
        return ber;
    }

    public void setBer(Float ber) {
        this.ber = ber;
    }

    public Integer getModem() {
        return modem;
    }

    public void setModem(Integer modem) {
        this.modem = modem;
    }

    public Integer getGsmMode() {
        return gsmMode;
    }

    public void setGsmMode(Integer gsmMode) {
        this.gsmMode = gsmMode;
    }

    public Integer getGsmType() {
        return gsmType;
    }

    public void setGsmType(Integer gsmType) {
        this.gsmType = gsmType;
    }

    public Integer getGsmRoam() {
        return gsmRoam;
    }

    public void setGsmRoam(Integer gsmRoam) {
        this.gsmRoam = gsmRoam;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getPsc() {
        return psc;
    }

    public void setPsc(Integer psc) {
        this.psc = psc;
    }

    public String getEcio() {
        return ecio;
    }

    public void setEcio(String ecio) {
        this.ecio = ecio;
    }

    public Integer getRscp() {
        return rscp;
    }

    public void setRscp(Integer rscp) {
        this.rscp = rscp;
    }

    public Integer getMcc() {
        return mcc;
    }

    public void setMcc(Integer mcc) {
        this.mcc = mcc;
    }

    public Integer getMnc() {
        return mnc;
    }

    public void setMnc(Integer mnc) {
        this.mnc = mnc;
    }

    public Integer getArfcn() {
        return arfcn;
    }

    public void setArfcn(Integer arfcn) {
        this.arfcn = arfcn;
    }

    public Integer getRac() {
        return rac;
    }

    public void setRac(Integer rac) {
        this.rac = rac;
    }

    public Integer getBsic() {
        return bsic;
    }

    public void setBsic(Integer bsic) {
        this.bsic = bsic;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBw() {
        return bw;
    }

    public void setBw(Integer bw) {
        this.bw = bw;
    }

    public Integer getRxch() {
        return rxch;
    }

    public void setRxch(Integer rxch) {
        this.rxch = rxch;
    }

    public Integer getTxch() {
        return txch;
    }

    public void setTxch(Integer txch) {
        this.txch = txch;
    }

    public Float getSinr() {
        return sinr;
    }

    public void setSinr(Float sinr) {
        this.sinr = sinr;
    }

    public Integer getRsrp() {
        return rsrp;
    }

    public void setRsrp(Integer rsrp) {
        this.rsrp = rsrp;
    }

    public Integer getRsrq() {
        return rsrq;
    }

    public void setRsrq(Integer rsrq) {
        this.rsrq = rsrq;
    }

    public void setPppoeAddr(String pppoeAddr) {
        this.pppoeAddr = pppoeAddr;
    }

    public void setPppoeGw(String pppoeGw) {
        this.pppoeGw = pppoeGw;
    }

    public void setPppoeMask(String pppoeMask) {
        this.pppoeMask = pppoeMask;
    }

    public void setPppoeDns1(String pppoeDns1) {
        this.pppoeDns1 = pppoeDns1;
    }

    public void setPppoeDns2(String pppoeDns2) {
        this.pppoeDns2 = pppoeDns2;
    }

    public void setWifiAddr(String wifiAddr) {
        this.wifiAddr = wifiAddr;
    }

    public void setWifiGw(String wifiGw) {
        this.wifiGw = wifiGw;
    }

    public void setWifiMask(String wifiMask) {
        this.wifiMask = wifiMask;
    }

    public void setWifiDns1(String wifiDns1) {
        this.wifiDns1 = wifiDns1;
    }

    public void setWifiDns2(String wifiDns2) {
        this.wifiDns2 = wifiDns2;
    }

    public void setWifiLease(Integer wifiLease) {
        this.wifiLease = wifiLease;
    }

    public void setWifiDomain(String wifiDomain) {
        this.wifiDomain = wifiDomain;
    }

    public void setWifiRenew(Integer wifiRenew) {
        this.wifiRenew = wifiRenew;
    }

    public void setWifiExpire(Integer wifiExpire) {
        this.wifiExpire = wifiExpire;
    }

    public void setWifiRebind(Integer wifiRebind) {
        this.wifiRebind = wifiRebind;
    }

    public void setWifiBssid(String wifiBssid) {
        this.wifiBssid = wifiBssid;
    }

    public void setWifiHiddenSsid(Integer wifiHiddenSsid) {
        this.wifiHiddenSsid = wifiHiddenSsid;
    }

    public void setWifiNetworkId(Integer wifiNetworkId) {
        this.wifiNetworkId = wifiNetworkId;
    }

    public void setWifiRssi(Integer wifiRssi) {
        this.wifiRssi = wifiRssi;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    public void setWifiConnectionState(Integer wifiConnectionState) {
        this.wifiConnectionState = wifiConnectionState;
    }

    public void setWifiQuality(Integer wifiQuality) {
        this.wifiQuality = wifiQuality;
    }

    public void setWifiNoiseLevel(Integer wifiNoiseLevel) {
        this.wifiNoiseLevel = wifiNoiseLevel;
    }

    public void setWifiSnr(Integer wifiSnr) {
        this.wifiSnr = wifiSnr;
    }

    public void setWifiMcs(Integer wifiMcs) {
        this.wifiMcs = wifiMcs;
    }

    public void setWifiBw(Integer wifiBw) {
        this.wifiBw = wifiBw;
    }

    public Integer getSinLockStatus() {
        return sinLockStatus;
    }

    public void setSinLockStatus(Integer sinLockStatus) {
        this.sinLockStatus = sinLockStatus;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }
}
