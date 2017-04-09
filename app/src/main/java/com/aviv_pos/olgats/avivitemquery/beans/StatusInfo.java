package com.aviv_pos.olgats.avivitemquery.beans;

/**
 * Created by olgats on 08/02/2016.
 */
public class StatusInfo {
    private String avivId;
    private String name;
    private String address;
    private float salesSum;
    private int clientCount;
    private int onlineTerminals;
    private int openDealCount;
    private float openDealSum;
    private long firstDealTime;
    private long firstShiftEnter;
    private int hrTotal;
    private int hrCurrent;
    private long whTotal;

    public StatusInfo() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getWhTotal() {
        return whTotal;
    }

    public void setWhTotal(long whTotal) {
        this.whTotal = whTotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvivId() {
        return avivId;
    }

    public void setAvivId(String avivId) {
        this.avivId = avivId;
    }


    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public long getFirstDealTime() {
        return firstDealTime;
    }

    public void setFirstDealTime(long firstDealTime) {
        this.firstDealTime = firstDealTime;
    }

    public long getFirstShiftEnter() {
        return firstShiftEnter;
    }

    public void setFirstShiftEnter(long firstShiftEnter) {
        this.firstShiftEnter = firstShiftEnter;
    }

    public int getHrCurrent() {
        return hrCurrent;
    }

    public void setHrCurrent(int hrCurrent) {
        this.hrCurrent = hrCurrent;
    }

    public int getHrTotal() {
        return hrTotal;
    }

    public void setHrTotal(int hrTotal) {
        this.hrTotal = hrTotal;
    }

    public int getOnlineTerminals() {
        return onlineTerminals;
    }

    public void setOnlineTerminals(int onlineTerminals) {
        this.onlineTerminals = onlineTerminals;
    }

    public int getOpenDealCount() {
        return openDealCount;
    }

    public void setOpenDealCount(int openDealCount) {
        this.openDealCount = openDealCount;
    }

    public float getOpenDealSum() {
        return openDealSum;
    }

    public void setOpenDealSum(float openDealSum) {
        this.openDealSum = openDealSum;
    }

    public float getSalesSum() {
        return salesSum;
    }

    public void setSalesSum(float salesSum) {
        this.salesSum = salesSum;
    }
}
