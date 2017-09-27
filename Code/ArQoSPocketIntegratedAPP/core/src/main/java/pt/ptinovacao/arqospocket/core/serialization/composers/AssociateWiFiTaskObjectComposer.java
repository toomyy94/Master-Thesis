package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AssociateWiFiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AssociateWiFiTaskData;

/**
 * Object composer for the {@link AssociateWiFiTaskData}.
 * <p>
 * Created by Emílio Simões on 13-04-2017.
 */
class AssociateWiFiTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((AssociateWiFiTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((AssociateWiFiTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((AssociateWiFiTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((AssociateWiFiTaskResult) data, propertyMap);
    }

    private void fillTaskData(AssociateWiFiTaskData data, Map<String, String> propertyMap) {
        data.setEssid(propertyMap.get(AssociateWiFiTaskData.ESSID));
        data.setBssid(propertyMap.get(AssociateWiFiTaskData.BSSID));
        data.setPassword(propertyMap.get(AssociateWiFiTaskData.PASSWORD));
    }

    private void fillResultData(AssociateWiFiTaskResult data, Map<String, String> propertyMap) {
        data.setSsid(propertyMap.get(AssociateWiFiTaskResult.SSID));
        data.setMacAddress(propertyMap.get(AssociateWiFiTaskResult.MAC_ADDRESS));
        data.setFrequency(propertyMap.get(AssociateWiFiTaskResult.FREQUENCY));
        data.setChannel(propertyMap.get(AssociateWiFiTaskResult.CHANNEL));
        data.setMode(propertyMap.get(AssociateWiFiTaskResult.MODE));
        data.setProtocol(propertyMap.get(AssociateWiFiTaskResult.PROTOCOL));
        data.setBitrate(propertyMap.get(AssociateWiFiTaskResult.BITRATE));
        data.setEncryption(propertyMap.get(AssociateWiFiTaskResult.ENCRYPTION));
        data.setAssociationTimeInSeconds(propertyMap.get(AssociateWiFiTaskResult.ASSOCIATION_TIME_IN_SECONDS));
        data.setSignalLevel(propertyMap.get(AssociateWiFiTaskResult.SIGNAL_LEVEL));
        data.setNoiseLevel(propertyMap.get(AssociateWiFiTaskResult.NOISE_LEVEL));
        data.setRatioSignalNoise(propertyMap.get(AssociateWiFiTaskResult.RATIO_SIGNAL_NOISE));
        data.setAddress(propertyMap.get(AssociateWiFiTaskResult.ADDRESS));
        data.setMask(propertyMap.get(AssociateWiFiTaskResult.MASK));
        data.setGateway(propertyMap.get(AssociateWiFiTaskResult.GATEWAY));
        data.setDns(propertyMap.get(AssociateWiFiTaskResult.DNS));
        data.setDomain(propertyMap.get(AssociateWiFiTaskResult.DOMAIN));
        data.setLeaseInSeconds(propertyMap.get(AssociateWiFiTaskResult.LEASE_IN_SECONDS));
    }

    private void collectTaskData(AssociateWiFiTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(AssociateWiFiTaskData.ESSID, data.getEssid());
        propertyMap.put(AssociateWiFiTaskData.BSSID, data.getBssid());
        propertyMap.put(AssociateWiFiTaskData.PASSWORD, data.getPassword());
    }

    private void collectResultData(AssociateWiFiTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(AssociateWiFiTaskResult.SSID, data.getSsid());
        propertyMap.put(AssociateWiFiTaskResult.MAC_ADDRESS, data.getMacAddress());
        propertyMap.put(AssociateWiFiTaskResult.FREQUENCY, data.getFrequency());
        propertyMap.put(AssociateWiFiTaskResult.CHANNEL, data.getChannel());
        propertyMap.put(AssociateWiFiTaskResult.MODE, data.getMode());
        propertyMap.put(AssociateWiFiTaskResult.PROTOCOL, data.getProtocol());
        propertyMap.put(AssociateWiFiTaskResult.BITRATE, data.getBitrate());
        propertyMap.put(AssociateWiFiTaskResult.ENCRYPTION, data.getEncryption());
        propertyMap.put(AssociateWiFiTaskResult.ASSOCIATION_TIME_IN_SECONDS, data.getAssociationTimeInSeconds());
        propertyMap.put(AssociateWiFiTaskResult.SIGNAL_LEVEL, data.getSignalLevel());
        propertyMap.put(AssociateWiFiTaskResult.NOISE_LEVEL, data.getNoiseLevel());
        propertyMap.put(AssociateWiFiTaskResult.RATIO_SIGNAL_NOISE, data.getRatioSignalNoise());
        propertyMap.put(AssociateWiFiTaskResult.ADDRESS, data.getAddress());
        propertyMap.put(AssociateWiFiTaskResult.MASK, data.getMask());
        propertyMap.put(AssociateWiFiTaskResult.GATEWAY, data.getGateway());
        propertyMap.put(AssociateWiFiTaskResult.DNS, data.getDns());
        propertyMap.put(AssociateWiFiTaskResult.DOMAIN, data.getDomain());
        propertyMap.put(AssociateWiFiTaskResult.LEASE_IN_SECONDS, data.getLeaseInSeconds());
    }
}
