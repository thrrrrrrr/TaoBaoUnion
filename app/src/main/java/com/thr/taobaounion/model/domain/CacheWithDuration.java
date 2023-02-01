package com.thr.taobaounion.model.domain;

public class CacheWithDuration {
    private Object value;
    private long duration;

    public CacheWithDuration(){

    }

    public CacheWithDuration(Object value, long duration) {
        this.value = value;
        this.duration = duration;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
