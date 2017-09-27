package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import java.util.Date;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Represents a generated cdr.
 * <p>
 * Created by Tomás Rodrigues on 13/00/2017.
 */
@Entity
@Table(name = "cdr")
public abstract class BaseCDR implements Parcelable, Persistable {

    /**
     * Gets the database generated entity ID. This value will be generated when inserting in the database and cannot be
     * manually set.
     *
     * @return the database generated entity ID.
     */
    @Key
    @Generated
    @Column(name = "_id")
    public  abstract long getId();

    @Column(name = "cdr_report_date")
    abstract Date getCDReportDate();
    abstract void setCDReportDate(Date cdr_report_date);

    @Column(name = "task_date")
    abstract String getTaskDate();
    abstract void setTaskDate(String task_date);

    @Column(name = "init_reported_date")
    abstract String getInitReportDate();
    abstract void setInitReportDate(String init_reported_date);

    @Column(name = "init_call_date")
    abstract String getInitCallDate();
    abstract void setInitCallDate(String init_call_date);

    @Column(name = "final_call_date")
    abstract String getFinalCallDate();
    abstract void setFinalCallDate(String final_call_date);

    @Column(name = "final_reported_date")
    abstract String getFinalReportDate();
    abstract void setFinalReportDate(String final_reported_date);

    @Column(name = "init_gps_location")
    abstract String getInitGpsLocation();
    abstract void setInitGpsLocation(String init_latitude);

    @Column(name = "final_gps_location")
    abstract String getFinalGpsLocation();
    abstract void setFinalGpsLocation(String final_longitude);

    @Column(name = "calle_caller")
    abstract Integer getCaller();
    abstract void setCaller(Integer calle_caller);

    @Column(name = "operator_name")
    abstract String getOperatorName();
    abstract void setOperatorName(String operator_name);

    @Column(name = "origin_number")
    abstract String getOriginNumber();
    abstract void setOriginNumber(String origin_number);

    @Column(name = "destination_number")
    abstract String getDestinationNumber();
    abstract void setDestinationNumber(String destination);

    @Column(name = "call_type")
    abstract Integer getCallType();
    abstract void setCallType(Integer call_type);

    @Column(name = "network_type")
    abstract Integer getNetworkType();
    abstract void setNetworkType(Integer network_type);

    @Column(name = "network_error_code")
    abstract String getNetworkError();
    abstract void setNetworkError(String network_error_code);

    @Column(name = "network_error_message")
    abstract String getNetworkErrorMessage();
    abstract void setNetworkErrorMessage(String network_error_message);

    @Column(name = "service_error_code")
    abstract String getServiceError();
    abstract void setServiceError(String service_error_code);

    @Column(name = "service_error_message")
    abstract String getServiceErrorMessage();
    abstract void setServiceErrorMessage(String service_error_message);

    @Column(name = "operation_success")
    abstract Integer getOperationSuccess();
    abstract void setOperationSuccess(Integer operation_success);

    @Column(name = "second_call")
    abstract Integer getSecondCall();
    abstract void setSecondCall(Integer second_call);

    @Column(name = "record_call")
    abstract Integer getRecordCall();
    abstract void setRecordCall(Integer record_call);

    @Column(name = "init_cel")
    abstract String getInitCell();
    abstract void setInitCell(String init_cel);

    @Column(name = "final_cel")
    abstract String getFinalCell();
    abstract void setFinalCell(String final_cel);

    @Column(name = "init_signal_level")
    abstract Integer getInitSignalLevel();
    abstract void setInitSignalLevel(Integer init_signal_level);

    @Column(name = "final_signal_level")
    abstract Integer getFinalSignalLevel();
    abstract void setFinalSignalLevel(Integer final_signal_level);

    @Column(name = "upstream_traffic")
    abstract String getUpstreamTraffic();
    abstract void setUpstreamTraffic(String upstream_traffic);

    @Column(name = "downstream_traffic")
    abstract String getDownstreamTraffic();
    abstract void setDownstreamTraffic(String downstream_traffic);

    @Column(name = "ip_address")
    abstract String getIpAddress();
    abstract void setIpAddress(String ip_address);

    @Column(name = "mask")
    abstract String getMask();
    abstract void setMask(String mask);

    @Column(name = "gateway")
    abstract String getGateway();
    abstract void setGateway(String gateway);

    @Column(name = "dns1")
    abstract String getDNS1();
    abstract void setDNS1(String dns1);

    @Column(name = "dns2")
    abstract String getDNS2();
    abstract void setDNS2(String dns2);

    @Column(name = "sms_timestamp")
    abstract Integer getSMSTimestamp();
    abstract void setSMSTimestamp(Integer sms_timestamp);

    @Column(name = "sms_text")
    abstract String getSMSText();
    abstract void setSMSText(String sms_text);

    @Column(name = "smsc")
    abstract String getSMSC();
    abstract void setSMSC(String smsc);

    @Column(name = "total_impulses")
    abstract Integer getTotalImpulses();
    abstract void setTotalImpulses(Integer total_impulses);

    @Column(name = "init_impulses")
    abstract Integer getInitImpulses();
    abstract void setInitImpulses(Integer init_impulses);

    @Column(name = "impulses_interval")
    abstract Double getImpulsesInterval();
    abstract void setImpulsesInterval(Double init_impulses);

    @Column(name = "impulses_standard_deviation")
    abstract Double getImpulsesStandardDeviation();
    abstract void setImpulsesStandardDeviation(Double impulses_standard_deviation);

    @Column(name = "voip_parameters")
    abstract String getVoipParameters();
    abstract void setVoipParameters(String voip_parameters);

    @Column(name = "sms_multipart_parameters")
    abstract String getSMSMultipartParameters();
    abstract void setSMSMultipartParameters(String sms_multipart_parameters);

    @Column(name = "iccid")
    abstract String getIccid();
    abstract void setIccid(String iccid);

    /**
     * Gets if an cdr has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @return if an cdr has been reported to the management server.
     */
    @Column(name = "reported")
    abstract boolean isReported();

    /**
     * Sets if an cdr has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @param reported if an cdr has been reported to the management server.
     */
    abstract void setReported(boolean reported);

    @Column(name = "is_notification_sent")
    abstract boolean isNotificationSent();

    abstract void setNotificationSent(boolean isNotificationSent);
}
