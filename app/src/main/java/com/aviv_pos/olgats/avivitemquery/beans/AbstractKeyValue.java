package com.aviv_pos.olgats.avivitemquery.beans;

/**
 * Created by olgats on 13/07/2016.
 */
public class AbstractKeyValue<K,V> {
    private K key;
    private V value;

    public AbstractKeyValue() {
        super();
    }

    public AbstractKeyValue(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value!=null?value.toString():null;
    }
}
