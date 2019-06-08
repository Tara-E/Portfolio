package com.tara.coxintv.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vehicles {

    @SerializedName("vehicleIds")
    @Expose
    private List<Integer> vehicleIds = null;

    public List<Integer> getVehicleIds() {
        return vehicleIds;
    }

    public void setVehicleIds(List<Integer> vehicleIds) {
        this.vehicleIds = vehicleIds;
    }

}