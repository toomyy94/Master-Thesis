package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import com.google.gson.annotations.SerializedName;

/**
 * ProbeConfiguration request.
 * <p>
 * Created by pedro on 19/05/2017.
 */
public class ProbeConfiguration {

    private static final String NAME = "nome";

    private static final String IP = "ip";

    private static final String IP_GATEWAY = "ip_gw";

    private static final String NETMASK = "netmask";

    private static final String IP_MANAGEMENT = "ip_ges";

    private static final String MAX_TEMPERATURE = "tempmax";

    private static final String CONFIGURATION_SYNC = "confsync";

    private static final String VPN_DYNAMIC_ADDRESS = "vpn_dynamic_addr";

    private static final String VPN_GW = "vpn_gw";

    private static final String VPN_MANAGER = "vpn_gestor";

    private static final String VPN_GROUP = "vpn_group";

    private static final String VPN_GROUP_PASSWORD = "vpn_group_pass";

    private static final String VPN_USER = "vpn_usr";

    private static final String VPN_USER_PASSWORD = "vpn_usr_pass";

    private static final String BATTERY_UPPER = "battery_upper";

    private static final String BATTERY_LOWER = "battery_lower";

    private static final String BATTERY_PROPERTY_CURRENT_NOW = "battery_property_current_now";

    private static final String BATTERY_PROPERTY_CAPACITY = "battery_property_capacity";

    private static final String MANAGEMENT_MODULE_APN = "modgestao_apn";

    private static final String MANAGEMENT_MODULE_USER = "modgestao_user";

    private static final String MANAGEMENT_MODULE_PASSWORD = "modgestao_pass";

    private static final String MANAGEMENT_MODULE_MANAGER = "modgestao_gestor";

    private static final String MANAGMENT_SERVER_ADDRESS = "ip_address_sg";

    private static final String MAX_HISTORY_TIME = "max_history_time";

    private static final String FILES_MAX_HISTORY_TIME = "files_max_history_time";

    private static final String PERCENTAGE_MAX_MEMORY_OCCUPIED = "percentage_max_memory_occupied";

    private static final String RADIOLOGS_DEDICATED = "radiolog_dedicated_mode";

    private static final String RADIOLOGS_IDLE = "radiolog_idle_mode";

    private static final String SCANLOGS_ENABLE = "scanlog_enabled";

    private static final String RADIOLOGS_INTERVAL = "radiolog_interval";

    private static final String SCANLOGS_INTERVAL = "scanlog_interval";

    private static final String RADIOLOGS_MAXSIZE = "radiologs_maxsize";

    private static final String RADIOLOGS_MULTIEVENT = "radiologs_multievent";

    private static final String RADIOLOGS_CELLRESELECTION = "radiologs_cellreselection";

    private static final String BASE_DESTINATION_SFTP = "destination_SFTP";

    private static final String SFTP_USERNAME = "SFTP_username";

    private static final String SFTP_PASSWORD = "SFTP_password";

    @SerializedName(NAME)
    private String name;

    @SerializedName(IP)
    private String ip;

    @SerializedName(IP_GATEWAY)
    private String ipGateway;

    @SerializedName(NETMASK)
    private String netmask;

    @SerializedName(IP_MANAGEMENT)
    private String ipManagement;

    @SerializedName(MAX_TEMPERATURE)
    private Integer maxTemperature;

    @SerializedName(CONFIGURATION_SYNC)
    private String configurationSync;

    @SerializedName(VPN_DYNAMIC_ADDRESS)
    private String vpnDynamicAddress;

    @SerializedName(VPN_GW)
    private String vpnGateway;

    @SerializedName(VPN_MANAGER)
    private String vpnManager;

    @SerializedName(VPN_GROUP)
    private String vpnGroup;

    @SerializedName(VPN_GROUP_PASSWORD)
    private String vpnGroupPassword;

    @SerializedName(VPN_USER)
    private String vpnUser;

    @SerializedName(VPN_USER_PASSWORD)
    private String vpnUserPassword;

    @SerializedName(BATTERY_UPPER)
    private String batteryUpper;

    @SerializedName(BATTERY_LOWER)
    private String batteryLower;

    @SerializedName(BATTERY_PROPERTY_CURRENT_NOW)
    private String batteryPropertyCurrentNow;

    @SerializedName(BATTERY_PROPERTY_CAPACITY)
    private String batteryPropertyCapacity;

    @SerializedName(MANAGEMENT_MODULE_APN)
    private String managementModuleApn;

    @SerializedName(MANAGEMENT_MODULE_USER)
    private String managementModuleUser;

    @SerializedName(MANAGEMENT_MODULE_PASSWORD)
    private String managementModulePassword;

    @SerializedName(MANAGEMENT_MODULE_MANAGER)
    private String managementModuleManager;

    @SerializedName(MANAGMENT_SERVER_ADDRESS)
    private String managementServerAddress;

    @SerializedName(MAX_HISTORY_TIME)
    private Integer maxHistoryTime;

    @SerializedName(FILES_MAX_HISTORY_TIME)
    private Integer maxHistoryTimeFiles;

    @SerializedName(PERCENTAGE_MAX_MEMORY_OCCUPIED)
    private Integer percentageMaxMemoryOccupied;

    @SerializedName(RADIOLOGS_DEDICATED)
    private Integer radiologDedicated;

    @SerializedName(RADIOLOGS_IDLE)
    private Integer radiologIdle;

    @SerializedName(SCANLOGS_ENABLE)
    private Integer scanlogsEnable;

    @SerializedName(RADIOLOGS_INTERVAL)
    private Integer radiologsInterval;

    @SerializedName(SCANLOGS_INTERVAL)
    private Integer scanlogsInterval;

    @SerializedName(RADIOLOGS_MAXSIZE)
    private Integer radiologsMaxsize;

    @SerializedName(RADIOLOGS_MULTIEVENT)
    private Integer radiologsMultievent;

    @SerializedName(RADIOLOGS_CELLRESELECTION)
    private Integer radiologsCellreselection;

    @SerializedName(BASE_DESTINATION_SFTP)
    private String baseDestinationSFTP;

    @SerializedName(SFTP_USERNAME)
    private String SFTPUsername;

    @SerializedName(SFTP_PASSWORD)
    private String SFTPPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpGateway() {
        return ipGateway;
    }

    public void setIpGateway(String ipGateway) {
        this.ipGateway = ipGateway;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getIpManagement() {
        return ipManagement;
    }

    public void setIpManagement(String ipManagement) {
        this.ipManagement = ipManagement;
    }

    public Integer getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Integer maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getConfigurationSync() {
        return configurationSync;
    }

    public void setConfigurationSync(String configurationSync) {
        this.configurationSync = configurationSync;
    }

    public String getVpnDynamicAddress() {
        return vpnDynamicAddress;
    }

    public void setVpnDynamicAddress(String vpnDynamicAddress) {
        this.vpnDynamicAddress = vpnDynamicAddress;
    }

    public String getVpnGateway() {
        return vpnGateway;
    }

    public void setVpnGateway(String vpnGateway) {
        this.vpnGateway = vpnGateway;
    }

    public String getVpnManager() {
        return vpnManager;
    }

    public void setVpnManager(String vpnManager) {
        this.vpnManager = vpnManager;
    }

    public String getVpnGroup() {
        return vpnGroup;
    }

    public void setVpnGroup(String vpnGroup) {
        this.vpnGroup = vpnGroup;
    }

    public String getVpnGroupPassword() {
        return vpnGroupPassword;
    }

    public void setVpnGroupPassword(String vpnGroupPassword) {
        this.vpnGroupPassword = vpnGroupPassword;
    }

    public String getVpnUser() {
        return vpnUser;
    }

    public void setVpnUser(String vpnUser) {
        this.vpnUser = vpnUser;
    }

    public String getVpnUserPassword() {
        return vpnUserPassword;
    }

    public void setVpnUserPassword(String vpnUserPassword) {
        this.vpnUserPassword = vpnUserPassword;
    }

    public String getBatteryUpper() {
        return batteryUpper;
    }

    public void setBatteryUpper(String batteryUpper) {
        this.batteryUpper = batteryUpper;
    }

    public String getBatteryLower() {
        return batteryLower;
    }

    public void setBatteryLower(String batteryLower) {
        this.batteryLower = batteryLower;
    }

    public String getBatteryPropertyCurrentNow() {
        return batteryPropertyCurrentNow;
    }

    public void setBatteryPropertyCurrentNow(String batteryPropertyCurrentNow) {
        this.batteryPropertyCurrentNow = batteryPropertyCurrentNow;
    }

    public String getBatteryPropertyCapacity() {
        return batteryPropertyCapacity;
    }

    public void setBatteryPropertyCapacity(String batteryPropertyCapacity) {
        this.batteryPropertyCapacity = batteryPropertyCapacity;
    }

    public String getManagementModuleApn() {
        return managementModuleApn;
    }

    public void setManagementModuleApn(String managementModuleApn) {
        this.managementModuleApn = managementModuleApn;
    }

    public String getManagementModuleUser() {
        return managementModuleUser;
    }

    public void setManagementModuleUser(String managementModuleUser) {
        this.managementModuleUser = managementModuleUser;
    }

    public String getManagementModulePassword() {
        return managementModulePassword;
    }

    public void setManagementModulePassword(String managementModulePassword) {
        this.managementModulePassword = managementModulePassword;
    }

    public String getManagementModuleManager() {
        return managementModuleManager;
    }

    public void setManagementModuleManager(String managementModuleManager) {
        this.managementModuleManager = managementModuleManager;
    }

    public String getManagementServerAddress() {
        return managementServerAddress;
    }

    public void setManagementServerAddress(String managementServerAddress) {
        this.managementServerAddress = managementServerAddress;
    }

    public Integer getMaxHistoryTime() {
        return maxHistoryTime;
    }

    public void setMaxHistoryTime(Integer maxHistoryTime) {
        this.maxHistoryTime = maxHistoryTime;
    }

    public Integer getMaxHistoryTimeFiles() {
        return maxHistoryTimeFiles;
    }

    public void setMaxHistoryTimeFiles(Integer maxHistoryTimeFiles) {
        this.maxHistoryTimeFiles = maxHistoryTimeFiles;
    }

    public Integer getPercentageMaxMemoryOccupied() {
        return percentageMaxMemoryOccupied;
    }

    public void setPercentageMaxMemoryOccupied(Integer percentageMaxMemoryOccupied) {
        this.percentageMaxMemoryOccupied = percentageMaxMemoryOccupied;
    }

    public Integer getRadiologsDedicated() {
        return radiologDedicated;
    }

    public void setRadiologsDedicated(Integer radiologDedicated) {
        this.radiologDedicated = radiologDedicated;
    }

    public Integer getRadiologsIdle() {
        return radiologIdle;
    }

    public void setRadiologsIdle(Integer radiologIdle) {
        this.radiologIdle = radiologIdle;
    }

    public Integer getScanlogsEnable() {
        return scanlogsEnable;
    }

    public void setScanlogsEnable(Integer scanlogsEnable) {
        this.scanlogsEnable = scanlogsEnable;
    }

    public Integer getRadiologsInterval() {
        return radiologsInterval;
    }

    public void setRadiologsInterval(Integer radiologsInterval) {
        this.radiologsInterval = radiologsInterval;
    }

    public Integer getScanlogsInterval() {
        return scanlogsInterval;
    }

    public void setScanlogsInterval(Integer scanlogsInterval) {
        this.scanlogsInterval = scanlogsInterval;
    }

    public Integer getRadiologsMaxsize() {
        return radiologsMaxsize;
    }

    public void setRadiologsMaxsize(Integer radiologsMaxsize) {
        this.radiologsMaxsize = radiologsMaxsize;
    }

    public Integer getRadiologsMultievent() {
        return radiologsMultievent;
    }

    public void setRadiologsMultievent(Integer radiologsMultievent) {
        this.radiologsMultievent = radiologsMultievent;
    }

    public Integer getRadiologsCellreselection() {
        return radiologsCellreselection;
    }

    public void setRadiologsCellreselection(Integer radiologsCellreselection) {
        this.radiologsCellreselection = radiologsCellreselection;
    }

    public String getBaseDestinationSFTP() {
        return baseDestinationSFTP;
    }

    public void setBaseDestinationSFTP(String baseDestinationSFTP) {
        this.baseDestinationSFTP = baseDestinationSFTP;
    }

    public String getSFTPUsername() {
        return SFTPUsername;
    }

    public void setSFTPUsername(String SFTPUsername) {
        this.SFTPUsername = SFTPUsername;
    }

    public String getSFTPPassword() {
        return SFTPPassword;
    }

    public void setSFTPPassword(String SFTPPassword) {
        this.SFTPPassword = SFTPPassword;
    }
}