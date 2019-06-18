package com.tara.coxintv.models.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public String toString() {
        Gson gson = new Gson();
        String string = super.toString();
        try {
            JSONObject json = new JSONObject(gson.toJson(this));
            string = json.toString(2);
        } catch (JSONException ignored) {
        }
        return string;
    }
}