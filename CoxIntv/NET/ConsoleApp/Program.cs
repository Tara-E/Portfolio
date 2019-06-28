using CoxIntv.ApiService;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
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
            Task<AnswerServiceResponse> answerTask = submitAnswerService.SubmitNewAnswer();
            answerTask.Wait();
            AnswerServiceResponse what = answerTask.Result;
        }
    }
}
