package com.tara.coxintv;

import com.tara.coxintv.models.Dealer;
import com.tara.coxintv.models.Vehicle;
import com.tara.coxintv.models.dto.DtoAnswer;
import com.tara.coxintv.models.dto.DtoDealer;
import com.tara.coxintv.models.dto.DtoVehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DtoConverter {

    public static DtoAnswer from(Map<Integer, List<DtoVehicle>> dealerVehicleMap) {
        List<DtoDealer> dealerList = new ArrayList<>();
        for (Map.Entry<Integer, List<DtoVehicle>> entry : dealerVehicleMap.entrySet()) {
            DtoDealer dealer = new DtoDealer();
            dealer.setDealerId(entry.getKey());
            dealer.setVehicles(entry.getValue());
            dealerList.add(dealer);
        }

        DtoAnswer answer = new DtoAnswer();
        answer.setDealers(dealerList);
        return answer;
    }

    public static void updateDealerNames(DtoAnswer answer, Object[] dealers) {
        for (DtoDealer dealer : answer.getDealers()) {
            for (Object o : dealers) {
                Dealer domainDealer;
                if (o instanceof Dealer) {
                    domainDealer = (Dealer) o;
                } else {
                    continue;
                }

                if (domainDealer.getName() == null) {
                    continue;
                }

                if (dealer.getDealerId() == domainDealer.getDealerId()) {
                    dealer.setName(domainDealer.getName());
                    break;
                }
            }
        }
    }

    public static DtoVehicle from(Vehicle vehicle) {
        DtoVehicle dtoVehicle = new DtoVehicle();
        dtoVehicle.setVehicleId(vehicle.getVehicleId());
        dtoVehicle.setYear(vehicle.getYear());
        dtoVehicle.setMake(vehicle.getMake());
        dtoVehicle.setModel(vehicle.getModel());

        return dtoVehicle;
    }
}
