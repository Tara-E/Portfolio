using System.Threading.Tasks;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;

namespace CoxIntv.ApiService
{
    /// <summary>
    /// ICoxIntvApiService Interface
    /// </summary>
    public interface ICoxIntvApiService
    {
        /// <summary>
        /// Creates new dataset and returns its ID.
        /// </summary>
        Task<DataSet> CreateDataSet();

        /// <summary>
        /// Get a list of all vehicleids in dataset.
        /// </summary>
        Task<Vehicles> GetVehicles(string datasetId);

        /// <summary>
        /// Get information about a vehicle.
        /// </summary>
        Task<Model.Vehicles.Vehicle> GetVehicle(string datasetId, int vehicleId);

        /// <summary>
        /// Get information about a dealer.
        /// </summary>
        Task<Model.Dealers.Dealer> GetDealer(string datasetId, int dealerId);

        /// <summary>
        /// Submit answer for dataset.
        /// </summary>
        Task<AnswerResponse> PostAnswer(string datasetId, Answer answer);
    }
}
