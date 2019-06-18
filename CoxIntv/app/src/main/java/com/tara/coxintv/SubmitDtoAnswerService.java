package com.tara.coxintv;

import com.tara.coxintv.apiservice.ApiService;
import com.tara.coxintv.apiservice.ServiceGenerator;
import com.tara.coxintv.models.AnswerResponse;
import com.tara.coxintv.models.Dealer;
import com.tara.coxintv.models.dto.DtoAnswer;
import com.tara.coxintv.models.dto.DtoVehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SubmitDtoAnswerService {

    public static class AnswerServiceResponse {
        public AnswerResponse answerResponse;
        public DtoAnswer answer;
    }

    public static Observable<AnswerServiceResponse> submitNewAnswer() {
        final ApiService apiService = ServiceGenerator.createService(ApiService.class);
        final AnswerServiceResponse finalResponse = new AnswerServiceResponse();
        return apiService.createDataSet()
                .flatMap(dataSet ->
                        apiService.getVehicleList(dataSet.getDatasetId())
                                .flatMap(vehicles ->
                                        getAnswer(apiService, dataSet.getDatasetId(), vehicles.getVehicleIds())
                                                .flatMap(answer -> {
                                                    finalResponse.answer = answer;
                                                    return apiService.postAnswer(dataSet.getDatasetId(), answer)
                                                            .flatMap(answerResponse -> {
                                                                finalResponse.answerResponse = answerResponse;
                                                                return Observable.just(finalResponse);
                                                            });
                                                })
                                )
                );
    }

    private static Observable<DtoAnswer> getAnswer(ApiService apiService, String dataSetId, List<Integer> vehicleIds) {
        final DtoConverter converter = new DtoConverter();
        final Map<Integer, List<DtoVehicle>> dealerVehicleMap = new ConcurrentHashMap<>();
        List<Observable<Dealer>> requests = new ArrayList<>();
        for (int vehicleId : vehicleIds) {
            requests.add(apiService.getVehicle(dataSetId, vehicleId)
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(vehicle -> {
                        int dealerId = vehicle.getDealerId();
                        List<DtoVehicle> vehicleList = dealerVehicleMap.get(dealerId);

                        boolean dealerAlreadyExists = false;
                        if (vehicleList == null) {
                            dealerAlreadyExists = true;
                            vehicleList = new ArrayList<>();
                            dealerVehicleMap.put(dealerId, vehicleList);
                        }
                        vehicleList.add(converter.from(vehicle));

                        return dealerAlreadyExists ?
                                Observable.just(new Dealer()) :
                                apiService.getDealer(dataSetId, dealerId).subscribeOn(Schedulers.newThread());
                    }));
        }

        return Observable.zip(
                requests,
                results -> {
                    DtoAnswer answer = converter.from(dealerVehicleMap);
                    converter.updateDealerNames(answer, results);
                    return answer;
                }).subscribeOn(Schedulers.io());
    }
}
