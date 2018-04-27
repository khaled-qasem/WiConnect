package wiconnect.khaled.com.wiconnect.wificonnection;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wiconnect.khaled.com.wiconnect.utils.Logger;
import wiconnect.khaled.com.wiconnect.utils.StringUtil;

import static java.lang.String.format;

/**
 * Created by Khaled on 4/27/2018.
 * Assumptions
 * Descriptions
 */

public class WifiHelper {
    private final WifiManager wifiManager;
    private final Logger logger;

    WifiHelper(Context context) {
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.logger = Logger.createLogger(WifiHelper.class);
    }

    boolean connectToSSID(String SSID) {
        WifiConfiguration configuration = createOpenWifiConfiguration(SSID);
        logger.d("Priority assigned to configuration is " + configuration.priority);

        int networkId = wifiManager.addNetwork(configuration);
        logger.d("networkId assigned while adding network is " + networkId);

        return enableNetwork(SSID, networkId);
    }

    String findAvailableSSID(List<String> SSIDs, List<ScanResult> scanResults) {
        logger.i("Available SSIDs count: " + scanResults.size());

        sortBySignalStrength(scanResults);

        for (ScanResult scanResult : scanResults) {
            if (SSIDs.contains(scanResult.SSID)) {
                return scanResult.SSID;
            }
        }
        return null;
    }

    boolean hasActiveSSID(String SSID) {
        String currentSSID = wifiManager.getConnectionInfo().getSSID();
        return areEqual(SSID, currentSSID);
    }

    void enableWifi() {
        wifiManager.setWifiEnabled(true);
    }

    static boolean areEqual(String SSID, String anotherSSID) {
        return TextUtils.equals(StringUtil.trimQuotes(SSID), StringUtil.trimQuotes(anotherSSID));
    }

    private static String formatSSID(String wifiSSID) {
        return format("\"%s\"", wifiSSID);
    }

    void startScan() {
        wifiManager.startScan();
    }

    public void disconnect() {
        wifiManager.disconnect();
    }

    private boolean enableNetwork(String SSID, int networkId) {
        if (networkId == -1) {
            networkId = getExistingNetworkId(SSID);
            logger.d("networkId of existing network is " + networkId);

            if (networkId == -1) {
                logger.e("Couldn't add network with SSID: " + SSID);
                return false;
            }
        }

        return wifiManager.enableNetwork(networkId, true);
    }

    private WifiConfiguration createOpenWifiConfiguration(String SSID) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = formatSSID(SSID);
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        assignHighestPriority(configuration);
        return configuration;
    }

    private void sortBySignalStrength(List<ScanResult> scanResults) {
        Collections.sort(scanResults, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult resultOne, ScanResult resultTwo) {
                return resultTwo.level - resultOne.level;
            }
        });
    }

    private int getExistingNetworkId(String SSID) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (areEqual(StringUtil.trimQuotes(existingConfig.SSID), StringUtil.trimQuotes(SSID))) {
                    return existingConfig.networkId;
                }
            }
        }
        return -1;
    }

    private void assignHighestPriority(WifiConfiguration config) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (config.priority <= existingConfig.priority) {
                    config.priority = existingConfig.priority + 1;
                }
            }
        }
    }

}
