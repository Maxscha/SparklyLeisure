using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Models
{
    public class CoalPlant
    {

        public LatLng Position { get; set; }
        public String Name { get; set; }
        public double CO2 { get; set; }
    }
}
