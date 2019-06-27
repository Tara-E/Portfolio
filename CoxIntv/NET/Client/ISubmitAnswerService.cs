using System.Threading.Tasks;
using CoxIntv.Model.DataSet;

namespace CoxIntv.ApiService
{
    public class AnswerServiceResponse
    {
        public AnswerResponse answerResponse;
        public Answer answer;
    }

    public interface ISubmitAnswerService
    {
        Task<AnswerServiceResponse> SubmitNewAnswer();
    }
}