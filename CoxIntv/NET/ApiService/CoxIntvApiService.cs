using System;
using System.Net.Http;
using System.Threading.Tasks;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;

namespace CoxIntv.ApiService
{
    /// <inheritdoc/>
    public class CoxIntvApiService : ICoxIntvApiService
    {
        private Uri BaseUri;

        /// <summary>
        /// The class constructor.
        /// </summary>
        /// <param name="baseUri">
        /// The base address of Uniform Resource Identifier (URI) of the Internet resource used when sending requests.
        /// </param>
        public CoxIntvApiService(Uri baseUri)
        {
            BaseUri = baseUri;
        }

        /// <inheritdoc/>
        async Task<DataSet> ICoxIntvApiService.CreateDataSet()
        {
            HttpClient client = createClient();
            const string path = "/api/datasetId";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<DataSet>();
        }

        /// <inheritdoc/>
        async Task<Vehicles> ICoxIntvApiService.GetVehicles(string datasetId)
        {
            HttpClient client = createClient();
            string path = $"/api/{datasetId}/vehicles";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<Vehicles>();
        }

        /// <inheritdoc/>
        async Task<Model.Vehicles.Vehicle> ICoxIntvApiService.GetVehicle(string datasetId, int vehicleId)
        {
            HttpClient client = createClient();
            string path = $"/api/{datasetId}/vehicles/{vehicleId}";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<Model.Vehicles.Vehicle>();
        }

        /// <inheritdoc/>
        async Task<Model.Dealers.Dealer> ICoxIntvApiService.GetDealer(string datasetId, int dealerId)
        {
            HttpClient client = createClient();
            string path = $"/api/{datasetId}/dealers/{dealerId}";

            HttpResponseMessage response = await client.GetAsync(path);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<Model.Dealers.Dealer>();

        }

        /// <inheritdoc/>
        async Task<AnswerResponse> ICoxIntvApiService.PostAnswer(string datasetId, Answer answer)
        {
            HttpClient client = createClient();
            string path = $"/api/{datasetId}/answer";

            HttpResponseMessage response = await client.PostAsJsonAsync(path, answer);
            response.EnsureSuccessStatusCode();

            return await response.Content.ReadAsAsync<AnswerResponse>();
        }

        private HttpClient createClient()
        {
            HttpClient client = new HttpClient
            {
                BaseAddress = BaseUri
            };
            return client;
        }
    }
}
