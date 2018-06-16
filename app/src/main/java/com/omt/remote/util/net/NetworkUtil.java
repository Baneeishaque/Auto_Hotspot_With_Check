package com.omt.remote.util.net;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * To use this Util you need following permissions<BR/>
 * <BR/>
 * <BR/>
 * <b> android.permission.INTERNET<BR/>
 * android.permission.ACCESS_NETWORK_STATE<BR/>
 * android.permission.ACCESS_WIFI_STATE<BR/>
 * android.permission.CHANGE_WIFI_STATE<BR/>
 * android.permission.BLUETOOTH<BR/>
 * android.permission.BLUETOOTH_ADMIN<BR/>
 * </b>
 *
 * This Utility contains important network related methods. It is helpful to
 * turn on wifi, hotspot etc.
 *
 *
 *
 */
public class NetworkUtil {

    /**
     * Check... Is phone connected to internet
     *
     * @param context
     * @return
     */
    public static boolean isConnectedToNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isConnected = networkInfo.isConnected();
        }

        return isConnected;
    }

    /**
     * Is internet connected using Wifi
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnectedThroughWifi(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            isConnected = networkInfo.isConnected();
        }

        return isConnected;
    }

    /**
     * Is internet connected using Bluetooth
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnectedThroughBluetooth(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        if (networkInfo != null) {
            isConnected = networkInfo.isConnected();
        }

        return isConnected;
    }

    /**
     * This method will return list of devices that connected with your phone
     * using Hotspot
     *
     * @return
     * @throws IOException
     */
    public static List<NetworkDevice> getDevicesConnectedToHotspot()
            throws IOException {
        List<NetworkDevice> devices = new ArrayList<NetworkDevice>();
        BufferedReader bufferedReader;

        bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] splitted = line.split(" +");
            if (splitted != null) {

                String mac = splitted[3];
                Log.i("NetworkUtil", "Mac : Outside If " + mac);
                if (mac.matches("..:..:..:..:..:..")) {

                    NetworkDevice device = new NetworkDevice();
                    device.setIpAddress(splitted[0]);
                    device.setMacAddress(mac);
                    int count = 0;
                    for (String data : splitted) {
                        Log.i("NetworkUtil", "Data (" + (count++) + "):" + data);
                    }
                    devices.add(device);

                }

            }
        }
        return devices;
    }

    /**
     * Turn On or Off wifi
     *
     * @param context
     * @param isTurnToOn
     */
    public static void turnOnOffWifi(Context context, boolean isTurnToOn) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        if (isTurnToOn && isHotspotSupported() && isHotspotOn(context)) {
            turnOnOffHotspot(context, false);
        }

        wifiManager.setWifiEnabled(isTurnToOn);
    }

    /**
     * is wifi is on.
     *
     * @param context
     * @return true for on otherwise false
     */
    public static boolean isWifiOn(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * Turn on or off Hotspot.
     *
     * @param context
     * @param isTurnToOn
     */
    public static void turnOnOffHotspot(Context context, boolean isTurnToOn) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiApControl apControl = WifiApControl.getApControl(wifiManager);
        if (apControl != null) {

            if (isWifiOn(context) && isTurnToOn) {
                turnOnOffWifi(context, false);
            }

            apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),
                    isTurnToOn);
        }
    }

    /**
     * check.. is hotspot on ?
     *
     * @param context
     * @return
     */
    public static boolean isHotspotOn(Context context) {
        boolean isOn = false;

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiApControl apControl = WifiApControl.getApControl(wifiManager);
        if (apControl != null) {
            isOn = apControl.isWifiApEnabled();
        }
        return isOn;
    }

    /**
     * Is device has support of Hotspot.
     *
     * @return
     */
    public static boolean isHotspotSupported() {
        return WifiApControl.isApSupported();
    }

    /**
     * This method will return Inet 4 address base on enable (Wifi or Hotspot)
     * connection.
     *
     * @return
     * @throws SocketException
     */
    public static List<String> getInet4NetworkAddress() throws SocketException {

        List<String> ipAddressList = new ArrayList<String>();

        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
                .hasMoreElements();) {
            NetworkInterface intf = (NetworkInterface) en.nextElement();
            for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr
                    .hasMoreElements();) {
                InetAddress inetAddress = (InetAddress) enumIpAddr
                        .nextElement();
                if (!inetAddress.isLoopbackAddress()
                        && inetAddress instanceof Inet4Address) {
                    ipAddressList.add(inetAddress.getHostAddress().toString());
                }
            }
        }
        return ipAddressList;
    }

    public static boolean isBluetoothOn() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.isEnabled();
    }

    public static boolean isBluetoothSupported() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return false;
        }
        return true;
    }

    public static void turnOnOffBluetooth(boolean isTurnToOn) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (isTurnToOn) {
            adapter.enable();
        } else {
            adapter.disable();
        }
    }

}