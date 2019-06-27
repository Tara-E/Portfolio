using System;
using System.Collections.Generic;

namespace CoxIntv.Model.DataSet
{
    public class Dealer
    {
        public Dealer()
        {
            Vehicles = new List<Vehicle>();
        }

        public int DealerId { get; set; }
        public string Name { get; set; }
        public virtual ICollection<Vehicle> Vehicles { get; set; }
    }
}
