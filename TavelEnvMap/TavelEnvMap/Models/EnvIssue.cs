using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Models
{

    public enum Issue
    {
        Trash,
        Shit,
        Dead
    }
    public class EnvIssue
    {
        public int Id { get; set; }
        public LatLng Position { get; set; }
        public double Rating { get; set; }
        public Issue Issue { get; set; }
    }
}
