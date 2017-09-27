package pt.ptinovacao.arqospocket.core.alarms;

/**
 * Constants that identify the alarm types in a alarm object.
 * <p>
 * Created by Tomás Rodrigues on 07-09-2017.
 */

public enum AlarmType {

    A000 ("Signal level below threshold"),
    A001 ("Cold reset"),
    A002 ("Thread locked"),
    A003 ("New test scheduled"),
    A004 ("New configuration applied"),
    A006 ("No GPS signal"),
    A008 ("High temperature"),
    A010 ("Overflow in audio data reception"),
    A011 ("Overflow in audio data transmission"),
    A012 ("Disk free below threshold"),
    A031 ("Restart after energy failure"),
    A032 ("Warm reset"),
    A033 ("No synchronism"),
    A035 ("Management System communication failure"),
    A037 ("SIP registration failure"),
    A039 ("VOIP connection failure"),
    A040 ("Radio module registration failure"),
    A041 ("SIM Manager connection failure"),
    A042 ("SIM Array connection failure"),
    A043 ("Module reset after no response"),
    A044 ("Probe reset after no response"),
    A045 ("Module reconnect after no response"),
    A046 ("Manual probe shutdown"),
    A047 ("Low battery probe shutdown"),
    A049 ("SIM removed"),
    A050 ("User module reset"),
    A051 ("User probe reset"),
    A052 ("Schedulling failure due to invalid MAC Address"),
    A053 ("SIM Card not found"),
    A054 ("Missing SMSC"),
    A055 ("Network registration denied"),
    A056 ("GPRS registration"),
    A057 ("Module reset failure"),
    A058 ("Physical IP connection failure"),
    A059 ("DHCP Lease expired"),
    A060 ("DHCP Timeout reached with no valid configuration received"),
    A062 ("POST request failure"),
    A064 ("File not available"),
    A065 ("The probe did not shutdown properly"),
    A066 ("Test Enabled"),
    A067 ("Test Disabled"),
    A068 ("Test Deleted"),
    A070 ("GSM service failure"),
    A071 ("Result size exceeds limt"),
    A072 ("Result file is corrupt"),
    A073 ("QMI SDK comunication problem"),
    A074 ("NTP Synchronization request"),
    A075 ("Invalid test file"),
    A076 ("WIFI State changed");

    private final String alarmContent;

    private AlarmType(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAlarmContent(){
        return alarmContent;
    }

}
