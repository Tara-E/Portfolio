using CoxIntv.Model.DataSet;
using System.Collections.Generic;
using DtoVehicle = CoxIntv.Model.DataSet.Vehicle;
using Vehicle = CoxIntv.Model.Vehicles.Vehicle;
using Dealer = CoxIntv.Model.Dealers.Dealer;
using DtoDealer = CoxIntv.Model.DataSet.Dealer;

namespace CoxIntv.Model
{
    public class DtoConverter
    {
        public static DtoVehicle From(Vehicle vehicle)
        {
            DtoVehicle dtoVehicle = new DtoVehicle
            {
                VehicleId = vehicle.VehicleId,
                Year = vehicle.Year,
                Make = vehicle.Make,
                Model = vehicle.Model
            };

            return dtoVehicle;
        }

        public static Answer From(IDictionary<int, ICollection<DtoVehicle>> dealerVehicleMap, ICollection<Dealer> dealers)
        {
            ICollection<DtoDealer> dealerList = new List<DtoDealer>();
            foreach (KeyValuePair<int, ICollection<DtoVehicle>> entry in dealerVehicleMap)
            {
                DtoDealer dealer = new DtoDealer();
                dealer.DealerId = entry.Key;
                dealer.Vehicles = entry.Value;

                foreach (Dealer d in dealers)
                {
                    if (dealer.DealerId == d.DealerId)
                    {
                        dealer.Name = d.Name;
                        break;
                    }
                }

                dealerList.Add(dealer);
            }

            Answer answer = new Answer();
            answer.Dealers = dealerList;
            return answer;
        }
    }
}
