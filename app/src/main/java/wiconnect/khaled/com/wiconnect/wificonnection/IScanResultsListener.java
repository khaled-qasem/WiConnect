package wiconnect.khaled.com.wiconnect.wificonnection;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Khaled on 4/27/2018.
 * Assumptions
 * Descriptions
 */

public interface IScanResultsListener {
    void onScanResultsAvailable(List<ScanResult> scanResults);
}
