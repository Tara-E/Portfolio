using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using CoxIntv.Model;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;
using Newtonsoft.Json;
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
            // Create a new dataset and get the vehicles for it
            string datasetId = null;
            Task<Vehicles> vehiclesTask = await apiService.CreateDataSet()
                .ContinueWith(task =>
                {
                    datasetId = task.Result.DatasetId;
                    return apiService.GetVehicles(datasetId);
                });

            // Create an answer for this dataset
            Task<Answer> answerTask = await vehiclesTask.ContinueWith(task => GetAnswer(datasetId, task.Result.VehicleIds));

            // Submit the answer and return response
            AnswerServiceResponse finalServiceResponse = new AnswerServiceResponse();
            AnswerResponse answerResponse = await answerTask
                .ContinueWith(task =>
                {
                    Answer answer = task.Result;
                    finalServiceResponse.JsonRequest = JsonConvert.SerializeObject(answer, Formatting.Indented);
                    return apiService.PostAnswer(datasetId, answer);
                }).Result;

            finalServiceResponse.Success = answerResponse.Success;
            finalServiceResponse.Message = answerResponse.Message;
            finalServiceResponse.TotalMilliseconds = answerResponse.TotalMilliseconds;

            return finalServiceResponse;

        }

        private async Task<Answer> GetAnswer(string datasetId, ICollection<int> vehicleIds)
        {
            Dictionary<int, ICollection<DtoVehicle>> dealerVehicleMap = new Dictionary<int, ICollection<DtoVehicle>>();
            var tasks = new List<Task<Model.Dealers.Dealer>>();

            foreach (int vehicleId in vehicleIds)
            {
                var dealer = apiService.GetVehicle(datasetId, vehicleId).ContinueWith(task =>
                {
                    Vehicle vehicle = task.Result;
                    int dealerId = vehicle.DealerId;

                    bool dealerAlreadyExists = UpdateDealerVehicleMap(dealerVehicleMap, dealerId, vehicle);

                    // Get this vehicle's dealer name if we aren't already getting it
                    return dealerAlreadyExists ?
                        null :
                        apiService.GetDealer(datasetId, dealerId).Result;
                });

                tasks.Add(dealer);
            }

            // Wait for all tasks to finish then return Answer
            return await Task.WhenAll(tasks).ContinueWith(task => DtoConverter.From(dealerVehicleMap, task.Result));
        }

        // returns true if the dealer already existed otherwise false
        private bool UpdateDealerVehicleMap(Dictionary<int, ICollection<DtoVehicle>> dictionary, int dealerId, Vehicle vehicle)
        {
            bool dealerAlreadyExists;
            ICollection<DtoVehicle> vehicleList = new List<DtoVehicle>();

            lock (dictionary)
            {
                dealerAlreadyExists = dictionary.ContainsKey(dealerId);
                if (dealerAlreadyExists)
                {
                    vehicleList = dictionary[dealerId];
                }
                else
                {
                    dictionary.Add(dealerId, vehicleList);
                }
            }
            vehicleList.Add(DtoConverter.From(vehicle));

            return dealerAlreadyExists;
        }
    }
}
