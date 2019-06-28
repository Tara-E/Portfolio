using System.Threading.Tasks;

namespace CoxIntv.ApiService
{
    public class AnswerServiceResponse
    {
        public string JsonRequest { get; set; }
        public bool Success { get; set; }
        public string Message { get; set; }
        public int TotalMilliseconds { get; set; }
    }

    public interface ISubmitAnswerService
    {
        Task<AnswerServiceResponse> SubmitNewAnswer();
    }
}