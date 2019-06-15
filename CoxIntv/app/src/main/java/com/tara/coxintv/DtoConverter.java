package com.tara.coxintv;

import com.tara.coxintv.models.Dealer;
import com.tara.coxintv.models.Vehicle;
import com.tara.coxintv.models.dto.DtoAnswer;
import com.tara.coxintv.models.dto.DtoDealer;
import com.tara.coxintv.models.dto.DtoVehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoConverter {

    public static DtoAnswer from(List<Vehicle> domainVehicleList) {
        Map<Integer, List<DtoVehicle>> vehicleMap = new HashMap<>();

        for (Vehicle v : domainVehicleList) {
            int dealerId = v.getDealerId();
            List<DtoVehicle> vehicleList = vehicleMap.get(dealerId);

            if (vehicleList == null) {
                vehicleList = new ArrayList<>();
                vehicleMap.put(dealerId, vehicleList);
            }

            vehicleList.add(from(v));
        }

        List<DtoDealer> dealerList = new ArrayList<>();
        for (Map.Entry<Integer, List<DtoVehicle>> entry : vehicleMap.entrySet()) {
            DtoDealer dealer = new DtoDealer();
            dealer.setDealerId(entry.getKey());
            dealer.setVehicles(entry.getValue());
            dealerList.add(dealer);
        }

        DtoAnswer answer = new DtoAnswer();
        answer.setDealers(dealerList);

        return answer;
    }

    public static void updateDealerNames(DtoAnswer answer, List<Dealer> dealerList){
        for(DtoDealer dealer : answer.getDealers()){
            for(Dealer domainDealer : dealerList){
                if(dealer.getDealerId() == domainDealer.getDealerId()){
                    dealer.setName(domainDealer.getName());
                    break;
                }
            }
        }
    }

    private static DtoVehicle from(Vehicle vehicle) {
        DtoVehicle dtoVehicle = new DtoVehicle();
        dtoVehicle.setVehicleId(vehicle.getVehicleId());
        dtoVehicle.setYear(vehicle.getYear());
        dtoVehicle.setMake(vehicle.getMake());
        dtoVehicle.setModel(vehicle.getModel());

        return dtoVehicle;
    }
}
