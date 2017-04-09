package com.aviv_pos.olgats.avivitemquery.beans;

/**
 * Created by olgats on 24/12/2015.
 */
public class PointValue {
    private float x;
    private float y;
    private String lable;

    public PointValue() {
    }

    public PointValue(float x, float y, String lable) {
        this.x = x;
        this.y = y;
        this.lable = lable;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    @Override
    public String toString() {
        return "PointValue{" +
                "x=" + x +
                ", y=" + y +
                ", lable='" + lable + '\'' +
                '}';
    }
}
