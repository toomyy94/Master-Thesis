package pt.ptinovacao.arqospocket.core.cdrs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import pt.ptinovacao.arqospocket.persistence.models.CDR;

/**
 * This class is responsible to parse the cdrs input in JSON format and return a list with the cdrs to execute.
 * <p>
 * Created by Tomás Rodrigues on 13-09-2017.
 */
public class CDRsParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CDRsParser.class);

    /**
     * Parses a list of CDR with a cdr on it.
     *
     * @param pendingCDR the cdrs with the ddr data.
     * @return the parsed JsonObject
     */
    public JsonObject CDRsToStrings(List<CDR> pendingCDR){
        String PIPE = "|";
        JsonObject jsonObject = new JsonObject();
        JsonArray data = new JsonArray();

        for(CDR cdr : pendingCDR) {
            String pippedCDR = StringUtils.EMPTY + cdr.getTaskDate() + PIPE + cdr.getInitReportDate() + PIPE + cdr.getInitCallDate() + PIPE +
                    cdr.getFinalCallDate() + PIPE + cdr.getFinalReportDate() + PIPE + cdr.getInitGpsLocation() + PIPE +
                    cdr.getFinalGpsLocation() + PIPE + cdr.getCaller() + PIPE + cdr.getOperatorName() + PIPE +
                    cdr.getOriginNumber() + PIPE + cdr.getDestinationNumber() + PIPE + cdr.getCallType() + PIPE +
                    cdr.getNetworkType() + PIPE + cdr.getNetworkError()+": " + cdr.getNetworkErrorMessage() + PIPE +
                    cdr.getServiceError()+": " + cdr.getServiceErrorMessage() + PIPE + cdr.getOperationSuccess() + PIPE +
                    cdr.getSecondCall() + PIPE + cdr.getRecordCall() + PIPE + cdr.getInitCell() + PIPE + cdr.getFinalCell() + PIPE +
                    cdr.getInitSignalLevel() + PIPE + cdr.getFinalSignalLevel() + PIPE + cdr.getUpstreamTraffic() + PIPE +
                    cdr.getDownstreamTraffic() + PIPE + cdr.getIpAddress() + PIPE + cdr.getMask() + PIPE + cdr.getGateway() + PIPE +
                    cdr.getDNS1() + PIPE + cdr.getDNS2() + PIPE + cdr.getSMSTimestamp() + PIPE + cdr.getSMSText() + PIPE +
                    cdr.getSMSC() + PIPE + cdr.getTotalImpulses() + PIPE + cdr.getInitImpulses() + PIPE + cdr.getImpulsesInterval() + PIPE +
                    cdr.getImpulsesStandardDeviation() + PIPE + cdr.getVoipParameters() + PIPE + cdr.getSMSMultipartParameters() + PIPE +
                    cdr.getIccid();

            pippedCDR = pippedCDR.replace("null", StringUtils.EMPTY);
            pippedCDR = pippedCDR.replace("|: ", "|");
            data.add(pippedCDR);
        }

        jsonObject.add("data", data);
        return jsonObject;
    }
}