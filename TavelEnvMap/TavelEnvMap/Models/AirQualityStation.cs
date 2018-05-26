using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Models
{
    public class AirQualityStation
    {
        public LatLng Position { get; set; }
        public float AirQualityIndex { get; set; }
        public string Source { get; set; }
    }
}

