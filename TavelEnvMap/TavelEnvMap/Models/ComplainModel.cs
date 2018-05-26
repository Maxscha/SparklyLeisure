using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Models
{
    public class ComplainModel
    {
        public LatLng Location { get; set; }
        public Politician[] Politician { get; set; }
        public EnvIssue[] EnvIssues { get; set; }
        public AirQualityStation[] Stations { get; set; }
    }
}
