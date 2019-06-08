package com.tara.coxintv.models.answer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dealers {

    @SerializedName("dealers")
    @Expose
    private List<Dealer> dealers = null;

    public List<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }
}