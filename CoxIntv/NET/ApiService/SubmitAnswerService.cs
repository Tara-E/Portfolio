using System.Collections.Generic;
using System.Threading.Tasks;
using CoxIntv.Model;
using CoxIntv.Model.DataSet;
using Newtonsoft.Json;
using DtoVehicle = CoxIntv.Model.DataSet.Vehicle;
using Vehicle = CoxIntv.Model.Vehicles.Vehicle;

namespace CoxIntv.ApiService
{
    /// <inheritdoc/>
    public class SubmitAnswerService : ISubmitAnswerService
    {
        private ICoxIntvApiService apiService;

        /// <summary>
        /// The class constructor.
        /// </summary>
        /// <param name="apiService">
        /// The ICoxIntvApiService used to communicate with Cox API
        /// </param>
        public SubmitAnswerService(ICoxIntvApiService apiService)
        {
            this.apiService = apiService;
        }

        /// <inheritdoc/>
        async Task<AnswerServiceResponse> ISubmitAnswerService.SubmitNewAnswer()
        {
            string datasetId = (await apiService.CreateDataSet()).DatasetId;

            // Create an answer for this dataset
            Answer answer = await GetAnswer(datasetId);

            // Submit the answer and return response
            AnswerServiceResponse finalServiceResponse = new AnswerServiceResponse();
            finalServiceResponse.JsonRequest = JsonConvert.SerializeObject(answer, Formatting.Indented);
            AnswerResponse answerResponse = await apiService.PostAnswer(datasetId, answer);

            finalServiceResponse.Success = answerResponse.Success;
            finalServiceResponse.Message = answerResponse.Message;
            finalServiceResponse.TotalMilliseconds = answerResponse.TotalMilliseconds;

            return finalServiceResponse;

        }

        /// <summary>
        /// Creates a new Answer for this datasetId
        /// <remarks>
        /// Gets information for all vehicles in parallel.
        /// When information for a vehicle returns, get the information for its dealer, unless we have it or are already getting it.
        /// Wait for all vehicle and dealer information, transform it into an Answer, then return the Answer.
        /// </remarks>
        private async Task<Answer> GetAnswer(string datasetId)
        {
            ICollection<int> vehicleIds = (await apiService.GetVehicles(datasetId)).VehicleIds;

            Dictionary<int, ICollection<DtoVehicle>> dealerVehicleMap = new Dictionary<int, ICollection<DtoVehicle>>();
            var tasks = new List<Task<Model.Dealers.Dealer>>();

            foreach (int vehicleId in vehicleIds)
            {
                // Get all vehicle information in parallel
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

        /// <summary>
        /// Adds this dealer to our dealer dictionary, and adds this vehicle to the dealer's vehicle list.
        /// <returns>
        /// Returns true if this dealer was already in our dictionary, otherwise false.
        /// </returns>
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
