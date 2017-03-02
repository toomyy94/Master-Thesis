package pt.ptinovacao.arqospocket.service.tasks.utils;


import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;


public class WiFiConnect {
    
		private final static Logger logger = LoggerFactory.getLogger(WiFiConnect.class);

        
		/*
        public static boolean connectToNetwork(Context ctx, WifiManager mWifiManager, WifiInfoStruct2 network, ScanResult mScanResult) {

    		
    		if(mScanResult == null) {
    			return false;
    		}
    		
    		if(isAdHoc(mScanResult)) {
    			return false;
    		}
    		
    		final String security = WiFiConfiguration.getScanResultSecurity(mScanResult);
    		final WifiConfiguration config = WiFiConfiguration.getWifiConfiguration(mWifiManager, mScanResult, security);

    		boolean connResult = false;
			if(config != null) {
				connResult = connectToConfiguredNetwork(ctx, mWifiManager, config);
			}
			
			return connResult;
        }
        */
        
        
    	private static boolean isAdHoc(final ScanResult scanResult) {
    		return scanResult.capabilities.indexOf("IBSS") != -1;
    	}
    	
        /**
         * Change the password of an existing configured network and connect to it
         * @param wifiMgr
         * @param config
         * @param newPassword
         * @return
         */
        public static boolean changePasswordAndConnect(final Context ctx, final WifiManager wifiMgr, final WifiConfiguration config, final String newPassword, final int numOpenNetworksKept) {
 
        	
        	WiFiConfiguration.setupSecurity(config, WiFiConfiguration.getWifiConfigurationSecurity(config), newPassword);
                final int networkId = wifiMgr.updateNetwork(config);
                if(networkId == -1) {
                        // Update failed.
                        return false;
                }
                // Force the change to apply.
                wifiMgr.disconnect();
                return connectToConfiguredNetwork(ctx, wifiMgr, config);
        }
        
        /**
         * Configure a network, and connect to it.
         * @param wifiMgr
         * @param scanResult
         * @param password Password for secure network or is ignored.
         * @return
         */
        public static boolean connectToNewNetwork(final Context ctx, final WifiManager wifiMgr, final ScanResult scanResult, final String password, final int numOpenNetworksKept) {
        		final String method = "connectToNewNetwork";
                final String security = WiFiConfiguration.getScanResultSecurity(scanResult);
                
                if(WiFiConfiguration.isOpenNetwork(security)) {
                        checkForExcessOpenNetworkAndSave(wifiMgr, numOpenNetworksKept);
                }
                
                WifiConfiguration config = new WifiConfiguration();
                config.SSID = WiFiConfiguration.convertToQuotedString(scanResult.SSID);
                config.BSSID = scanResult.BSSID;
                WiFiConfiguration.setupSecurity(config, security, password);
                
                int id = -1;
                try {
                        id = wifiMgr.addNetwork(config);
                } catch(NullPointerException ex) {
                		MyLogger.error(logger, method, ex);
                        // Weird!! Really!!
                        // This exception is reported by user to Android Developer Console(https://market.android.com/publish/Home)
                }
                if(id == -1) {
                        return false;
                }
                
                if(!wifiMgr.saveConfiguration()) {
                        return false;
                }
                
                config = WiFiConfiguration.getWifiConfiguration(wifiMgr, config, security);
                if(config == null) {
                        return false;
                }
                
                return connectToConfiguredNetwork(ctx, wifiMgr, config);
        }
        
        /**
         * Connect to a configured network.
         * @param wifiManager
         * @param config
         * @param numOpenNetworksKept Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT
         * @return
         */
        public static boolean connectToConfiguredNetwork(final Context ctx, final WifiManager wifiMgr, WifiConfiguration config) {

        		if(!wifiMgr.enableNetwork(config.networkId, true)) {
        			return false;
        		}
        		
        		return true;
        }
        
        
        
        private static void sortByPriority(final List<WifiConfiguration> configurations) {
                java.util.Collections.sort(configurations, new Comparator<WifiConfiguration>() {

                        @Override
                        public int compare(WifiConfiguration object1,
                                        WifiConfiguration object2) {
                                return object1.priority - object2.priority;
                        }
                });
        }
        
        /**
         * Ensure no more than numOpenNetworksKept open networks in configuration list.
         * @param wifiMgr
         * @param numOpenNetworksKept
         * @return Operation succeed or not.
         */
        private static boolean checkForExcessOpenNetworkAndSave(final WifiManager wifiMgr, final int numOpenNetworksKept) {
                final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
                sortByPriority(configurations);
                
                boolean modified = false;
                int tempCount = 0;
                for(int i = configurations.size() - 1; i >= 0; i--) {
                        final WifiConfiguration config = configurations.get(i);
                        if(WiFiConfiguration.isOpenNetwork(WiFiConfiguration.getWifiConfigurationSecurity(config))) {
                                tempCount++;
                                if(tempCount >= numOpenNetworksKept) {
                                        modified = true;
                                        wifiMgr.removeNetwork(config.networkId);
                                }
                        }
                }
                if(modified) {
                        return wifiMgr.saveConfiguration();
                }
                
                return true;
        }
        
        /*
        private static final int MAX_PRIORITY = 99999;
        
        private static int shiftPriorityAndSave(final WifiManager wifiMgr) {
                final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
                sortByPriority(configurations);
                final int size = configurations.size();
                for(int i = 0; i < size; i++) {
                        final WifiConfiguration config = configurations.get(i);
                        config.priority = i;
                        wifiMgr.updateNetwork(config);
                }
                wifiMgr.saveConfiguration();
                return size;
        }

        private static int getMaxPriority(final WifiManager wifiManager) {
                final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
                int pri = 0;
                for(final WifiConfiguration config : configurations) {
                        if(config.priority > pri) {
                                pri = config.priority;
                        }
                }
                return pri;
        }
         */
}
