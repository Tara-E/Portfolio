package com.tara.coxintv.apiservice;

import com.tara.coxintv.models.AnswerResponse;
import com.tara.coxintv.models.DataSet;
import com.tara.coxintv.models.Dealer;
import com.tara.coxintv.models.Vehicle;
import com.tara.coxintv.models.Vehicles;
import com.tara.coxintv.models.dto.DtoAnswer;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/api/datasetId")
    Observable<DataSet> createDataSet();

    @GET("/api/{datasetId}/vehicles")
    Observable<Vehicles> getVehicleList(@Path("datasetId") String datasetId);

    @GET("/api/{datasetId}/vehicles/{vehicleid}")
    Observable<Vehicle> getVehicle(@Path("datasetId") String datasetId, @Path("vehicleid") Integer vehicleid);

    @GET("/api/{datasetId}/dealers/{dealerid}")
    Observable<Dealer> getDealer(@Path("datasetId") String datasetId, @Path("dealerid") Integer dealerid);

    @POST("/api/{datasetId}/answer")
    Observable<AnswerResponse> postAnswer(@Path("datasetId") String datasetId, @Body DtoAnswer answer);
}