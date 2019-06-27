package com.tara.coxintv.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DtoDealer {

    @SerializedName("dealerId")
    @Expose
    private int dealerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vehicles")
    @Expose
    private List<DtoVehicle> vehicles = null;

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

    public List<DtoVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<DtoVehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
