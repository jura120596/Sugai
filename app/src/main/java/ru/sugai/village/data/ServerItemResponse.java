package ru.sugai.village.data;

public class ServerItemResponse<T> {
    private T data;
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
