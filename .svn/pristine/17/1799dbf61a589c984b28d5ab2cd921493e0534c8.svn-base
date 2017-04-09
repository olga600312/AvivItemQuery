package com.aviv_pos.olgats.avivitemquery.adapters;

/**
 * Created by olgats on 29/06/2016.
 */
public abstract class AbstractExtraModel<T> {
    public static final int MAP_TYPE = 1;
    public static final int FLOAT_TYPE = 2;
    public static final int BOOL_TYPE = 3;
    private String name;
    private T value;
    private int type;

    public AbstractExtraModel(int type, String name, T value) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public AbstractExtraModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String valueToString() {
        return value != null ? value.toString() : "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}