using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TavelEnvMap.Models;

namespace TavelEnvMap
{
    public class DataBaseHelper
    {
        static LatLng WannSee = new LatLng()
        {
            Lat = 52.436495,
            Lng = 13.178738
        };

        static LatLng WannSee2 = new LatLng()
        {
            Lat = 52.435495,
            Lng = 13.178738
        };

        static LatLng GrunewaldAirStation = new LatLng()
        {
            Lat = 52.473192,
            Lng = 13.225144
        };

        public static EnvIssue[] GetEnvIssues(float lat, float lng)
        {
            var issue = new EnvIssue();
            issue.Position = WannSee;
            issue.Rating = 3;
            issue.Issue = Issue.Trash;

            var issue2 = new EnvIssue()
            {
                Position = WannSee2,
                Rating = 1,
                Issue = Issue.Shit
            };
            //issue2.Position.Lat += 0.1 ;
            var list = new List<EnvIssue>();
            list.Add(issue);
            list.Add(issue2);
            return list.ToArray();
        }
        public static Politician[] GetPoliticans(float lat, float lng)
        {
            var politican = new Politician()
            {
                Address = "TestAdress",
                Authority = "Bezirksbürgermeister",
                Name = "Arne Hendrick",
                Number = "+491658432",
                Url = "arnewäreguterpolitiker.de"
            };
            var politicians = new List<Politician>();
            politicians.Add(politican);
            return politicians.ToArray();
        }

        public static AirQualityStation[] GetAirQualityStations(float lat, float lng)
        {
            var stations = new List<AirQualityStation>();
            var station = new AirQualityStation();

            station.AirQualityIndex = 49.1f;
            station.Position = GrunewaldAirStation;
            station.Source = "http://aqicn.org/?city=Germany/Berlin/Grunewald%283.5m%29";

            stations.Add(station);
            return stations.ToArray();
        }
    }
}
