package com.tara.coxintv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tara.coxintv.apiservice.ApiService;
import com.tara.coxintv.apiservice.ServiceGenerator;
import com.tara.coxintv.models.AnswerResponse;
import com.tara.coxintv.models.DataSet;
import com.tara.coxintv.models.Dealer;
import com.tara.coxintv.models.Vehicle;
import com.tara.coxintv.models.Vehicles;
import com.tara.coxintv.models.dto.DtoAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doTheThing();
    }

    public Observable<List<Vehicle>> getVehicleList(ApiService apiService, String dataSetId, List<Integer> vehicleIds) {
        List<Observable<Vehicle>> requests = new ArrayList<>();
        for (int vehicleId : vehicleIds) {
            requests.add(apiService.getVehicle(dataSetId, vehicleId).subscribeOn(Schedulers.newThread()));
        }

        return Observable.zip(
                requests,
                results -> {
                    return Stream.of(results).map(o -> (Vehicle) o).collect(Collectors.toList());
                }).subscribeOn(Schedulers.io());
    }

    public Observable<List<Dealer>> getDealerList(ApiService apiService, String dataSetId, List<Integer> dealerIds) {
        List<Observable<Dealer>> requests = new ArrayList<>();
        for (int dealerId : dealerIds) {
            requests.add(apiService.getDealer(dataSetId, dealerId).subscribeOn(Schedulers.newThread()));
        }

        return Observable.zip(
                requests,
                results -> {
                    return Stream.of(results).map(o -> (Dealer) o).collect(Collectors.toList());
                }).subscribeOn(Schedulers.io());
    }

    public void doTheThing() {
        final ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<DataSet> dataSetCall = apiService.createDataSet();

        dataSetCall.enqueue(new Callback<DataSet>() {
            @Override
            public void onResponse(Call<DataSet> call, Response<DataSet> response) {
                if (response.isSuccessful()) {
                    final DataSet dataset = response.body();
                    System.out.println(dataset);


                    Call<Vehicles> dataSetCall = apiService.getVehicleList(dataset.getDatasetId());

                    dataSetCall.enqueue(new Callback<Vehicles>() {
                        @Override
                        public void onResponse(Call<Vehicles> call, Response<Vehicles> response) {
                            if (response.isSuccessful()) {
                                Vehicles vehicles = response.body();
                                getVehicleList(apiService, dataset.getDatasetId(), vehicles.getVehicleIds())
                                        .subscribe(new Consumer<List<Vehicle>>() {
                                            @Override
                                            public void accept(List<Vehicle> vehicleList) {
                                                final DtoConverter converter = new DtoConverter();
                                                final DtoAnswer answer = converter.from(vehicleList);
                                                String what = "";


                                                getDealerList(apiService, dataset.getDatasetId(), answer.getDealerIds())
                                                        .subscribe(new Consumer<List<Dealer>>() {
                                                            @Override
                                                            public void accept(List<Dealer> dealerList) {
                                                                converter.updateDealerNames(answer, dealerList);
                                                                String what = "";


                                                                apiService.postAnswer(dataset.getDatasetId(), answer)
                                                                        .subscribe(new Consumer<AnswerResponse>() {
                                                                            @Override
                                                                            public void accept(AnswerResponse s) {
                                                                                String what = "";
                                                                            }
                                                                        });
                                                            }
                                                        });

                                            }
                                        });
                                String what = "";
                            } else {
                                System.out.println(response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<Vehicles> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DataSet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
