using CoxIntv.ApiService;
using CoxIntv.Model.DataSet;
using CoxIntv.Model.Vehicles;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;
using DtoVehicle = CoxIntv.Model.DataSet.Vehicle;
using Vehicle = CoxIntv.Model.Vehicles.Vehicle;
using Dealer = CoxIntv.Model.Dealers.Dealer;
using DtoDealer = CoxIntv.Model.DataSet.Dealer;
using Newtonsoft.Json;

namespace UnitTests
{
    [TestClass]
    public class SubmitAnswerServiceTests
    {
        private Mock<ICoxIntvApiService> ApiService;
        private DataSet ExpectedDataSet;
        private Answer ExpectedAnswer;
        private AnswerServiceResponse ExpectedResponse;

        [TestInitialize]
        public void TestInitialize()
        {
            //Set up Expected DataSet
            ExpectedDataSet = new DataSet
            {
                DatasetId = "datasetId"
            };

            //Set up Expected Answer
            ExpectedAnswer = new Answer
            {
                Dealers = new List<DtoDealer>()
                {
                    new DtoDealer
                    {
                        DealerId = 1,
                        Name = "Name1",
                        Vehicles = new List<DtoVehicle>
                        {
                            new DtoVehicle
                            {
                                VehicleId = 01,
                                Make = "Make1",
                                Year = 2001,
                                Model = "Model1"
                            },
                            new DtoVehicle
                            {
                                VehicleId = 02,
                                Make = "Make2",
                                Year = 2002,
                                Model = "Model2"
                            }
                        }
                    },
                    new DtoDealer
                    {
                        DealerId = 2,
                        Name = "Name1",
                        Vehicles = new List<DtoVehicle>
                        {
                            new DtoVehicle
                            {
                                VehicleId = 03,
                                Make = "Make3",
                                Year = 2003,
                                Model = "Model3"
                            },
                            new DtoVehicle
                            {
                                VehicleId = 04,
                                Make = "Make4",
                                Year = 2004,
                                Model = "Model4"
                            }
                        }
                    }
                }
            };

            //Set up Expected Response
            ExpectedResponse = new AnswerServiceResponse
            {
                JsonRequest = "Test Answer",
                Message = "Test Message",
                Success = true,
                TotalMilliseconds = 99
            };

            //Set up ApiService Mock
            Vehicles vehicles = new Vehicles
            {
                VehicleIds = new List<int>()
            };
            List<Vehicle> vehicleList = new List<Vehicle>();
            foreach (DtoDealer d in ExpectedAnswer.Dealers)
            {
                foreach (DtoVehicle v in d.Vehicles)
                {
                    vehicles.VehicleIds.Add(v.VehicleId);
                    vehicleList.Add(
                        new Vehicle
                        {
                            DealerId = d.DealerId,
                            Make = v.Make,
                            Model = v.Model,
                            Year = v.Year,
                            VehicleId = v.VehicleId
                        });
                }
            }

            ApiService = new Mock<ICoxIntvApiService>();
            ApiService.Setup(x => x.CreateDataSet()).Returns(Task.FromResult(ExpectedDataSet));
            ApiService.Setup(x => x.GetVehicles(ExpectedDataSet.DatasetId))
                .Returns(Task.FromResult(vehicles));

            foreach (Vehicle v in vehicleList)
            {
                ApiService.Setup(x => x.GetVehicle(ExpectedDataSet.DatasetId, v.VehicleId))
                    .Returns(Task.FromResult(v));
            }

            foreach (DtoDealer d in ExpectedAnswer.Dealers)
            {
                Dealer dealer = new Dealer
                {
                    DealerId = d.DealerId,
                    Name = d.Name
                };
                ApiService.Setup(x => x.GetDealer(ExpectedDataSet.DatasetId, dealer.DealerId))
                    .Returns(Task.FromResult(dealer));
            }

            AnswerResponse response = new AnswerResponse
            {
                Message = ExpectedResponse.Message,
                Success = ExpectedResponse.Success,
                TotalMilliseconds = ExpectedResponse.TotalMilliseconds
            };

            ApiService.Setup(x => x.PostAnswer(ExpectedDataSet.DatasetId, It.IsAny<Answer>()))
                .Returns(Task.FromResult(response));
        }

        [TestMethod]
        public void SubmitNewAnswer_ValidAnswer()
        {
            // Arrange
            ISubmitAnswerService answerService = new SubmitAnswerService(ApiService.Object);

            // Act
            Task<AnswerServiceResponse> answerTask = null;
            try
            {
                answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
            }
            catch (Exception)
            {
                Assert.Fail();
            }

            // Assert
            ApiService.Verify(x => x.CreateDataSet(), Times.Once());
            ApiService.Verify(x => x.GetVehicles(It.IsAny<string>()), Times.Once());
            ApiService.Verify(x => x.GetDealer(It.IsAny<string>(), It.IsAny<int>()), Times.Exactly(ExpectedAnswer.Dealers.Count));

            int numVehcles = 0;
            foreach (DtoDealer d in ExpectedAnswer.Dealers)
            {
                numVehcles += d.Vehicles.Count;
            }
            ApiService.Verify(x => x.GetVehicle(It.IsAny<string>(), It.IsAny<int>()), Times.Exactly(numVehcles));

            Assert.IsNotNull(answerTask);
            Assert.IsNotNull(answerTask.Result);
            Assert.IsNull(answerTask.Exception);

            AnswerServiceResponse actualResponse = answerTask.Result;
            Answer actualAnswer = JsonConvert.DeserializeObject<Answer>(actualResponse.JsonRequest);
            ValidateDealers(ExpectedAnswer.Dealers, actualAnswer.Dealers);
            Assert.AreEqual(ExpectedResponse.Message, actualResponse.Message);
            Assert.AreEqual(ExpectedResponse.Success, actualResponse.Success);
            Assert.AreEqual(ExpectedResponse.TotalMilliseconds, actualResponse.TotalMilliseconds);
        }

        [TestMethod]
        public void SubmitNewAnswer_CreateDataSet_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            ApiService.Setup(x => x.CreateDataSet()).Returns(Task.FromException<DataSet>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(ApiService.Object);

            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        [TestMethod]
        public void SubmitNewAnswer_GetVehicles_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            ApiService.Setup(x => x.GetVehicles(It.IsAny<string>()))
                .Returns(Task.FromException<Vehicles>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(ApiService.Object);

            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        [TestMethod]
        public void SubmitNewAnswer_GetVehicle_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            ApiService.Setup(x => x.GetVehicle(It.IsAny<string>(), It.IsAny<int>()))
                .Returns(Task.FromException<Vehicle>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(ApiService.Object);

            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        [TestMethod]
        public void SubmitNewAnswer_GetDealer_Throws()
        {
            // Arrange
            IOException expectedException = new IOException("failed...");
            ApiService.Setup(x => x.GetDealer(It.IsAny<string>(), It.IsAny<int>()))
                .Returns(Task.FromException<Dealer>(expectedException));
            ISubmitAnswerService answerService = new SubmitAnswerService(ApiService.Object);

            // Act
            try
            {
                Task<AnswerServiceResponse> answerTask = answerService.SubmitNewAnswer();
                answerTask.Wait();
                Assert.Fail();
            }
            catch (Exception e)
            {
                Assert.AreEqual(expectedException.Message, e.InnerException.Message);
            }
        }

        /////////////////////////////////////////////////////////
        ///////////////////// Helper Methods ////////////////////
        /////////////////////////////////////////////////////////

        private static void ValidateDealers(ICollection<DtoDealer> expected, ICollection<DtoDealer> actual)
        {
            List<DtoDealer> expectedDealers = new List<DtoDealer>(expected);
            expectedDealers.Sort((x, y) => x.DealerId.CompareTo(y.DealerId));
            List<DtoDealer> returnedDealers = new List<DtoDealer>(actual);
            returnedDealers.Sort((x, y) => x.DealerId.CompareTo(y.DealerId));

            Assert.AreEqual(expectedDealers.Count, returnedDealers.Count);
            for (int i = 0; i < expectedDealers.Count; i++)
            {
                DtoDealer expectedD = expectedDealers[i];
                DtoDealer actualD = returnedDealers[i];
                Assert.AreEqual(expectedD.DealerId, actualD.DealerId);
                Assert.AreEqual(expectedD.Name, actualD.Name);
                ValidateVehicles(expectedD.Vehicles, actualD.Vehicles);
            }
        }

        private static void ValidateVehicles(ICollection<DtoVehicle> expected, ICollection<DtoVehicle> actual)
        {
            List<DtoVehicle> expectedVehicles = new List<DtoVehicle>(expected);
            expectedVehicles.Sort((x, y) => x.VehicleId.CompareTo(y.VehicleId));
            List<DtoVehicle> actualVehicles = new List<DtoVehicle>(actual);
            actualVehicles.Sort((x, y) => x.VehicleId.CompareTo(y.VehicleId));

            Assert.AreEqual(expectedVehicles.Count, actualVehicles.Count);
            for (int i = 0; i < expectedVehicles.Count; i++)
            {
                DtoVehicle expectedV = expectedVehicles[i];
                DtoVehicle actualV = actualVehicles[i];
                Assert.AreEqual(expectedV.VehicleId, actualV.VehicleId);
                Assert.AreEqual(expectedV.Make, actualV.Make);
                Assert.AreEqual(expectedV.Year, actualV.Year);
                Assert.AreEqual(expectedV.Model, actualV.Model);
            }
        }
    }
}