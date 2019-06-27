using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading.Tasks;
using CoxIntv.Model;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;
using DtoVehicle = CoxIntv.Model.DataSet.Vehicle;
using Vehicle = CoxIntv.Model.Vehicles.Vehicle;

namespace CoxIntv.ApiService
{
    public class SubmitAnswerService : ISubmitAnswerService
    {
        private ICoxIntvApiService apiService;

        public SubmitAnswerService(ICoxIntvApiService apiService)
        {
            this.apiService = apiService;
        }

        async Task<AnswerServiceResponse> ISubmitAnswerService.SubmitNewAnswer()
        {
            AnswerServiceResponse finalResponse = null;

            string datasetId = null;
            Task<Vehicles> vehiclesTask = await apiService.CreateDataSet()
                .ContinueWith(task =>
                {
                    datasetId = task.Result.DatasetId;
                    return apiService.GetVehicles(datasetId);
                });

            Task<Answer> answerTask = await vehiclesTask.ContinueWith(task => GetAnswer(datasetId, task.Result.VehicleIds));


            return await new Task<AnswerServiceResponse>(null);

        }

        private async Task<Answer> GetAnswer(string datasetId, ICollection<int> vehicleIds)
        {
            ConcurrentDictionary<int, ICollection<DtoVehicle>> dealerVehicleMap = new ConcurrentDictionary<int, ICollection<DtoVehicle>>();
            var tasks = new List<Task<Model.Dealers.Dealer>>();
            foreach (int vehicleId in vehicleIds)
            {
                Task<Model.Dealers.Dealer> dealerTask = await apiService.GetVehicle(datasetId, vehicleId).ContinueWith(task =>
                {
                    Vehicle vehicle = task.Result;
                    int dealerId = vehicle.DealerId;

                    //Get this vehicle's dealer name if we aren't already getting it
                    ICollection<DtoVehicle> vehicleList = dealerVehicleMap[dealerId];
                    bool dealerAlreadyExists = false;
                    if (vehicleList == null)
                    {
                        dealerAlreadyExists = true;
                        vehicleList = new List<DtoVehicle>();
                        dealerVehicleMap[dealerId] = vehicleList;
                    }

                    vehicleList.Add(DtoConverter.From(vehicle));

                    return dealerAlreadyExists ?
                        null :
                        apiService.GetDealer(datasetId, dealerId);
                });

                tasks.Add(dealerTask);
            }

            // Wait for all tasks to finish then return Answer
            return await Task.WhenAll(tasks).ContinueWith(task => DtoConverter.From(dealerVehicleMap, task.Result));
        }
    }
}
