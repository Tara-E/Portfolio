using CoxIntv.ApiService;
using System;
using System.Threading.Tasks;

namespace ConsoleApp
{
    class Program
    {
        private const string BaseUriString = "http://api.coxauto-interview.com";

        static void Main(string[] args)
        {
            ICoxIntvApiService apiService = new CoxIntvApiService(new Uri(BaseUriString));
            ISubmitAnswerService submitAnswerService = new SubmitAnswerService(apiService);

            Console.WriteLine("Submitting new Answer... ");
            Task<AnswerServiceResponse> answerTask = submitAnswerService.SubmitNewAnswer();

            answerTask.Wait();
            AnswerServiceResponse response = answerTask.Result;

            Console.WriteLine("\n---------- Response ----------");
            Console.WriteLine("Success: " + response.Success);
            Console.WriteLine("Message: " + response.Message);
            Console.WriteLine("TotalMilliseconds: " + response.TotalMilliseconds + "(ms)");
            Console.WriteLine("\n---------- Request ----------");
            Console.WriteLine("AnswerServiceResponse " + response.JsonRequest);
        }
    }
}
