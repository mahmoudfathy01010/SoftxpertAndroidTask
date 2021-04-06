package com.elbaitdesign.softxpertandroidtask.model;

import java.util.ArrayList;

public class CarApiResponse {
    private Integer status;
    private ArrayList<Car> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<Car> getData() {
        return data;
    }

    public void setData(ArrayList<Car> data) {
        this.data = data;
    }
}
