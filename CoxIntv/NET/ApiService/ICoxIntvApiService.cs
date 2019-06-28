using System.Threading.Tasks;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;

namespace CoxIntv.ApiService
{
    public interface ICoxIntvApiService
    {
        Task<DataSet> CreateDataSet();

        Task<Vehicles> GetVehicles(string datasetId);

        Task<Model.Vehicles.Vehicle> GetVehicle(string datasetId, int vehicleId);

        Task<Model.Dealers.Dealer> GetDealer(string datasetId, int dealerId);

        Task<AnswerResponse> PostAnswer(string datasetId, Answer answer);
    }
}
