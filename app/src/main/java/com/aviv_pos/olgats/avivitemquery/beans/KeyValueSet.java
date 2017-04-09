package com.aviv_pos.olgats.avivitemquery.beans;

import java.util.Arrays;

/**
 * Created by olgats on 21/12/2015.
 */
public class KeyValueSet {
    private KeyValue[] set = new KeyValue[] {};
    private int status;

    public KeyValue[] getSet() {
        return set;
    }

    public void setSet(KeyValue[] set) {
        this.set = set;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "KeyValueSet{" +
                "set=" + Arrays.toString(set) +
                ", status=" + status +
                '}';
    }
}
