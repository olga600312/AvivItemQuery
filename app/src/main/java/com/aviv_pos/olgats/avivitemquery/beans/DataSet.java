package com.aviv_pos.olgats.avivitemquery.beans;

import java.util.Arrays;

/**
 * Created by olgats on 08/02/2016.
 */
public class DataSet<T> {
    private int status;
    private T[] set = (T[]) new Object[] {};

    public T[] getSet() {
        return set;
    }

    public void setSet(T[] set) {
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
        return "DataSet{" +
                "set=" + Arrays.toString(set) +
                ", status=" + status +
                '}';
    }
}
