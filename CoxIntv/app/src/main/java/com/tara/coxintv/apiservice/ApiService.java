package com.tara.coxintv.apiservice;

import com.tara.coxintv.models.DataSet;
import com.tara.coxintv.models.Vehicle;
import com.tara.coxintv.models.Vehicles;
import com.tara.coxintv.models.answer.Dealers;
import com.tara.coxintv.models.answer.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/api/datasetId")
    Call<DataSet> createDataSet();

    @GET("/api/{datasetId}/vehicles")
    Call<Vehicles> getVehicleList(@Path("datasetId") String datasetId);

    @GET("/api/{datasetId}/vehicles/{vehicleid}")
    Call<Vehicle> getVehicle(@Path("datasetId") String datasetId, @Path("vehicleid") String vehicleid);

    @GET("/api/{datasetId}/vehicles/{dealerid}")
    Call<Dealers> getDealers(@Path("datasetId") String datasetId, @Path("dealerid") String dealerid);

    @POST("/api/{datasetId}/answer")
    Call<Response> createUser(@Path("datasetId") String datasetId, @Body Dealers dealers);
}