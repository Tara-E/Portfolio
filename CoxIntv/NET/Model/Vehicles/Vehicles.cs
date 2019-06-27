using System;
using System.Collections.Generic;

namespace CoxIntv.Model.Vehicles
{
    public class Vehicles
    {
        public Vehicles()
        {
            VehicleIds = new List<int>();
        }
        public virtual ICollection<int> VehicleIds { get; set; }
    }
}
