using CoxIntv.Model.DataSet;
using System.Collections.Generic;
using DtoVehicle = CoxIntv.Model.DataSet.Vehicle;
using Vehicle = CoxIntv.Model.Vehicles.Vehicle;
using Dealer = CoxIntv.Model.Dealers.Dealer;
using DtoDealer = CoxIntv.Model.DataSet.Dealer;

namespace CoxIntv.Model
{
    /// <summary>
    /// Helper methods used to transform Model data.
    /// </summary>
    /// <remarks>
    /// This helps keep our ApiService decoupled from Model data.
    /// </remarks>
    public class DtoConverter
    {
        /// <summary>
        /// Transforms the domain Vehicle to our Answer's Vehicle object.
        /// </summary>
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

        /// <summary>
        /// Transforms a dictionary of dealerIds, and a collection of dealer information to an Answer.
        /// </summary>
        /// <param name="dealerVehicleMap">
        /// a Dictionary that maps dealerId keys to thier vehicle list values.
        /// </param>
        /// <param name="dealers">
        /// A Collection of Dealers
        /// </param>
        public static Answer From(IDictionary<int, ICollection<DtoVehicle>> dealerVehicleMap, ICollection<Dealer> dealers)
        {
            ICollection<DtoDealer> dealerList = new List<DtoDealer>();

            foreach (Dealer d in dealers)
            {
                if (d != null)
                {
                    DtoDealer dealer = new DtoDealer();
                    dealer.DealerId = d.DealerId;
                    dealer.Name = d.Name;
                    dealer.Vehicles = dealerVehicleMap[d.DealerId];
                    dealerList.Add(dealer);
                }
            }

            Answer answer = new Answer();
            answer.Dealers = dealerList;
            return answer;
        }
    }
}
