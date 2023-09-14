package org.sk.pdfreader.view;

public class AppEvent<T> {


    private final T data;
    private final String eventType;
    public AppEvent(T value, String eventType) {
        data=value;
        this.eventType =eventType;
    }

    public T getData(){
        return data;
    }

}
