package com.example.wifiscaner;

public class WifiData {
    private String ssid;
    private String bssid;
    private String capabilities;
    private String centerFreq0;
    private String centerFreq1;
    private String frequency;
    private String level;
    private Integer channelWidth;
    private Long timeStamp;
/*
    public WifiData(String ssid, String bssid, String capabilities, String centerFreq0, String centerFreq1, String frequency, String level, Integer channelWidth, Long timeStamp) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.capabilities = capabilities;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        this.frequency = frequency;
        this.level = level;
        this.channelWidth = channelWidth;
        this.timeStamp = timeStamp;
    }
*/
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getCenterFreq0() {
        return centerFreq0;
    }

    public void setCenterFreq0(String centerFreq0) {
        this.centerFreq0 = centerFreq0;
    }

    public String getCenterFreq1() {
        return centerFreq1;
    }

    public void setCenterFreq1(String centerFreq1) {
        this.centerFreq1 = centerFreq1;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getChannelWidth() {
        return channelWidth;
    }

    public void setChannelWidth(Integer channelWidth) {
        this.channelWidth = channelWidth;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
