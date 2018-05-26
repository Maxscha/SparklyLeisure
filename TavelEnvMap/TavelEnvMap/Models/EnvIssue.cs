using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Models
{

    public enum Issue
    {
        Trash,
        Shit
    }
    public class EnvIssue
    {
        public LatLng Position { get; set; }
        public int Rating { get; set; }
        public Issue Issue { get; set; }
    }
}
