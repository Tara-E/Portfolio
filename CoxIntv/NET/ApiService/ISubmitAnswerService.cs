using System.Threading.Tasks;

namespace CoxIntv.ApiService
{
    /// <summary>
    /// ISubmitAnswerService Interface
    /// </summary>
    public class AnswerServiceResponse
    {
        /// <summary>
        /// The submitted Answer as a json string.
        /// </summary>
        public string JsonRequest { get; set; }

        /// <summary>
        /// True if the submission was succesful, otherwise false.
        /// </summary>
        public bool Success { get; set; }

        /// <summary>
        /// The message returned in the response.
        /// </summary>
        public string Message { get; set; }

        /// <summary>
        /// The total number of milliseconds that it took to create and submit the Answer.
        /// </summary>
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