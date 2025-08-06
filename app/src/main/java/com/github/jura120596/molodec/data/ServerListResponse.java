package com.github.jura120596.molodec.data;

import java.util.List;

public class ServerListResponse<T> {
    private List<T> data;
    private int last_page;
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }
}
