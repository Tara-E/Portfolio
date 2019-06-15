package com.tara.coxintv.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DtoAnswer {

    @SerializedName("dealers")
    @Expose
    private List<DtoDealer> dealers = null;

    public List<DtoDealer> getDealers() {
        return dealers;
    }

    public void setDealers(List<DtoDealer> dealers) {
        this.dealers = dealers;
    }

    public List<Integer> getDealerIds() {
        if (dealers == null) {
            return null;
        }

        List<Integer> dealerIds = new ArrayList<>();
        for (DtoDealer d : dealers) {
            dealerIds.add(d.getDealerId());
        }
        return dealerIds;
    }
}