using CoxIntv.ApiService;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using System;
using System.IO;
using System.Threading.Tasks;

namespace UnitTests
{
    [TestClass]
    public class SubmitAnswerServiceTests
    {
        [TestMethod]
        public void SubmitNewAnswer_CreateDataSet_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            Mock<ICoxIntvApiService> apiService = new Mock<ICoxIntvApiService>();
            apiService.Setup(x => x.CreateDataSet()).Returns(Task.FromException<DataSet>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(apiService.Object);


            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.IsNotNull(e);
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        [TestMethod]
        public void SubmitNewAnswer_GetVehicles_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            Mock<ICoxIntvApiService> apiService = new Mock<ICoxIntvApiService>();
            apiService.Setup(x => x.GetVehicles(It.IsAny<string>()))
                .Returns(Task.FromException<Vehicles>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(apiService.Object);


            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.IsNotNull(e);
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        [TestMethod]
        public void SubmitNewAnswer_GetVehicle_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            Mock<ICoxIntvApiService> apiService = new Mock<ICoxIntvApiService>();
            apiService.Setup(x => x.GetVehicle(It.IsAny<string>(), It.IsAny<int>()))
                .Returns(Task.FromException<CoxIntv.Model.Vehicles.Vehicle>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(apiService.Object);


            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.IsNotNull(e);
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        [TestMethod]
        public void SubmitNewAnswer_GetDealer_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            Mock<ICoxIntvApiService> apiService = new Mock<ICoxIntvApiService>();
            apiService.Setup(x => x.GetDealer(It.IsAny<string>(), It.IsAny<int>()))
                .Returns(Task.FromException<CoxIntv.Model.Dealers.Dealer>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(apiService.Object);


            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.IsNotNull(e);
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }
    }
}