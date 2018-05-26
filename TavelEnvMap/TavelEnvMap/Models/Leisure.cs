using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TavelEnvMap.Models
{
    public enum LeisureType
    {
        Swimming
    }
    public class Leisure
    {
        public bool ToPolluted { get; set; }
        public String Name { get; set; }
        public LatLng Position { get; set; }
        public LeisureType Type { get; set; }

    }
}
