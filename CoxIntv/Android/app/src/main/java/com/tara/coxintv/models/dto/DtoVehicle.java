package com.tara.coxintv.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DtoVehicle {

    @SerializedName("vehicleId")
    @Expose
    private int vehicleId;
    @SerializedName("year")
    @Expose
    private int year;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
