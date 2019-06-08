package com.tara.coxintv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tara.coxintv.apiservice.ApiService;
import com.tara.coxintv.apiservice.ServiceGenerator;
import com.tara.coxintv.models.DataSet;
import com.tara.coxintv.models.Vehicles;

import java.util.List;

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

    public void doTheThing() {
        final ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<DataSet> dataSetCall = apiService.createDataSet();

        dataSetCall.enqueue(new Callback<DataSet>() {
            @Override
            public void onResponse(Call<DataSet> call, Response<DataSet> response) {
                if (response.isSuccessful()) {
                    DataSet dataset = response.body();
                    System.out.println(dataset);


                    Call<Vehicles> dataSetCall = apiService.getVehicleList(dataset.getDatasetId());

                    dataSetCall.enqueue(new Callback<Vehicles>() {
                        @Override
                        public void onResponse(Call<Vehicles> call, Response<Vehicles> response) {
                            if (response.isSuccessful()) {
                                Vehicles vehicles = response.body();

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
