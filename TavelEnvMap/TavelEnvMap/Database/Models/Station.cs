using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Database.Models
{
    public class Station
    {
        public String Name { get; set; }
        public int Id { get; set; }
        public String Latitude { get; set; }
        public String Longitude { get; set; }
    }
}
