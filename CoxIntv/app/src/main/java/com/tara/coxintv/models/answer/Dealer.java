package com.tara.coxintv.models.answer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tara.coxintv.models.Vehicle;

import java.util.List;

public class Dealer {

    @SerializedName("dealerId")
    @Expose
    private int dealerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vehicles")
    @Expose
    private List<Vehicle> vehicles = null;

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
