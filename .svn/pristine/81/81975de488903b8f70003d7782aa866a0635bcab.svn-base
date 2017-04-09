package com.aviv_pos.olgats.avivitemquery.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgats on 24/12/2015.
 */
public class Result<T> {
    private int code;
    private List<T> data;

    public Result() {
    }

    public Result(int code) {
        this.code = code;
        data=new ArrayList<T>();
    }

    public Result(int code, List<T> data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
