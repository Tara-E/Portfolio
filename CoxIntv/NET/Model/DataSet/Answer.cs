using System.Collections.Generic;

namespace CoxIntv.Model.DataSet
{
    public class Answer
    {
        public Answer()
        {
            Dealers = new List<Dealer>();
        }
        public virtual ICollection<Dealer> Dealers { get; set; }
    }
}
