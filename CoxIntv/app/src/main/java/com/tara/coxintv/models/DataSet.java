package com.tara.coxintv.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataSet {

    @SerializedName("datasetId")
    @Expose
    private String datasetId;

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

}