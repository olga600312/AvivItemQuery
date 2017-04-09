package com.aviv_pos.olgats.avivitemquery.beans;

/**
 * Created by olgats on 08/02/2016.
 */
public class Store {
    private String avivId;
    private String terminalId;
    private String name;
    private String address;
    private String city;
    private int type;
    private int chainId;

    public Store() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvivId() {
        return avivId;
    }

    public void setAvivId(String avivId) {
        this.avivId = avivId;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
