package com.github.jura120596.molodec.retrofit.responses;


public class ObjectResponse<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
