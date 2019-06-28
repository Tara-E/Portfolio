using System;
using System.Net.Http;
using System.Threading.Tasks;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;

namespace CoxIntv.ApiService
{
    public class CoxIntvApiService : ICoxIntvApiService
    {
        private HttpClient client;

        public CoxIntvApiService(Uri baseUri)
        {
            client = new HttpClient
            {
                BaseAddress = baseUri
            };
        }

        async Task<DataSet> ICoxIntvApiService.CreateDataSet()
        {
            const string path = "/api/datasetId";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<DataSet>();
        }

        async Task<Vehicles> ICoxIntvApiService.GetVehicles(string datasetId)
        {
            string path = $"/api/{datasetId}/vehicles";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<Vehicles>();
        }

        async Task<Model.Vehicles.Vehicle> ICoxIntvApiService.GetVehicle(string datasetId, int vehicleId)
        {
            string path = $"/api/{datasetId}/vehicles/{vehicleId}";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<Model.Vehicles.Vehicle>();
        }

        async Task<Model.Dealers.Dealer> ICoxIntvApiService.GetDealer(string datasetId, int dealerId)
        {
            string path = $"/api/{datasetId}/dealers/{dealerId}";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<Model.Dealers.Dealer>();
        }

        async Task<AnswerResponse> ICoxIntvApiService.PostAnswer(string datasetId, Answer answer)
        {
            string path = $"/api/{datasetId}/answer";

            HttpResponseMessage response = await client.PostAsJsonAsync(path, answer);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<AnswerResponse>();
        }
    }
}
