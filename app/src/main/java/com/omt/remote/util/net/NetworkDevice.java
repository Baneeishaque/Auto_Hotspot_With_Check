package com.omt.remote.util.net;

/**
 * This class contains information of network devices like Mac Address and
 * IpAddress. 
 */
public class NetworkDevice {
    private String macAddress;
    private String ipAddress;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}