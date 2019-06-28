using System.Threading.Tasks;

namespace CoxIntv.ApiService
{
    /// <summary>
    /// ISubmitAnswerService Interface
    /// </summary>
    public class AnswerServiceResponse
    {
        public string JsonRequest { get; set; }
        public bool Success { get; set; }
        public string Message { get; set; }
        public int TotalMilliseconds { get; set; }
    }

    /// <summary>
    /// ISubmitAnswerService Interface
    /// </summary>
    public interface ISubmitAnswerService
    {
        /// <summary>
        /// Creates a new Answer, submits it, and returns the response.
        /// </summary>
        /// <returns>
        /// Returns an async Task to fetch the response.
        /// </returns>
        /// <seealso cref="AnswerServiceResponse"></seealso>
        Task<AnswerServiceResponse> SubmitNewAnswer();
    }
}