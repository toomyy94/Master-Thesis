package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.PingTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.PingTaskData;

/**
 * Ping task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class PingTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((PingTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((PingTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((PingTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((PingTaskResult) data, propertyMap);
    }

    private void fillTaskData(PingTaskData data, Map<String, String> propertyMap) {
        data.setInterval(propertyMap.get(PingTaskData.INTERVAL));
        data.setIpAddress(propertyMap.get(PingTaskData.IP_ADDRESS));
        data.setPacketNumber(propertyMap.get(PingTaskData.PACKET_NUMBER));
        data.setPacketSize(propertyMap.get(PingTaskData.PACKET_SIZE));
        data.setPingTimeout(propertyMap.get(PingTaskData.PING_TIMEOUT));
    }

    private void fillResultData(PingTaskResult data, Map<String, String> propertyMap) {
        data.setMinimum(propertyMap.get(PingTaskResult.MINIMUM));
        data.setMedium(propertyMap.get(PingTaskResult.MEDIUM));
        data.setMaximum(propertyMap.get(PingTaskResult.MAXIMUM));
        data.setSentPackets(propertyMap.get(PingTaskResult.SENT_PACKETS));
        data.setReceivedPackets(propertyMap.get(PingTaskResult.RECEIVED_PACKETS));
        data.setLostPackets(propertyMap.get(PingTaskResult.LOST_PACKETS));
    }

    private void collectTaskData(PingTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(PingTaskData.INTERVAL, data.getInterval());
        propertyMap.put(PingTaskData.IP_ADDRESS, data.getIpAddress());
        propertyMap.put(PingTaskData.PACKET_NUMBER, data.getPacketNumber());
        propertyMap.put(PingTaskData.PACKET_SIZE, data.getPacketSize());
        propertyMap.put(PingTaskData.PING_TIMEOUT, data.getPingTimeout());
    }

    private void collectResultData(PingTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(PingTaskResult.MINIMUM, data.getMinimum());
        propertyMap.put(PingTaskResult.MEDIUM, data.getMedium());
        propertyMap.put(PingTaskResult.MAXIMUM, data.getMaximum());
        propertyMap.put(PingTaskResult.SENT_PACKETS, data.getSentPackets());
        propertyMap.put(PingTaskResult.RECEIVED_PACKETS, data.getReceivedPackets());
        propertyMap.put(PingTaskResult.LOST_PACKETS, data.getLostPackets());
    }
}

