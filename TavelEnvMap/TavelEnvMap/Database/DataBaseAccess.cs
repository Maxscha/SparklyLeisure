using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TavelEnvMap.Database.Models;
using TavelEnvMap.Models;

namespace TavelEnvMap.Database
{
    public class DataBaseAccess
    {
        public static DataBaseAccess DataBase { get; set; }

        private NpgsqlConnection connection;
        
        public DataBaseAccess()
        {
            var connString = "Host=localhost;Username=postgres;Password=admin;Database=postgres";
            connection = new NpgsqlConnection(connString);
            connection.Open();
        }

        public IEnumerable<EnvIssue> GetEnvIssue()
        {
            var issues = new List<EnvIssue>();
            var connString = "Host=localhost;Username=postgres;Password=admin;Database=postgres";
            var connection = new NpgsqlConnection(connString);
            connection.Open();
            var i = 0;
            using (var cmd = new NpgsqlCommand("SELECT * FROM environmental_issues", connection))
            {
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        var t = new EnvIssue()
                        {
                            Id = i,
                            Rating = reader.GetDouble(4),
                            Issue = (Issue)Convert.ToInt32(reader.GetString(0)),
                            Position = new LatLng()
                            {
                                Lat = reader.GetDouble(1),
                                Lng = reader.GetDouble(2)
                            }
                        };
                        i++;
                        issues.Add(t);
                    }
            }

            return issues;
        }

        public IEnumerable<CoalPlant> GetCoal()
        {
            var plant = new List<CoalPlant>();
            var connString = "Host=localhost;Username=postgres;Password=admin;Database=postgres";
            var connection = new NpgsqlConnection(connString);
            connection.Open();
            var i = 0;
            using (var cmd = new NpgsqlCommand("SELECT * FROM coal_power_plant", connection))
            {
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        var t = new CoalPlant()
                        {
                            
                            Position = new LatLng()
                            {
                                Lat = reader.GetDouble(1),
                                Lng = reader.GetDouble(0)
                            },
                            Name = reader.GetString(2),
                            CO2 = reader.GetDouble(3)
                        };
                        i++;
                        plant.Add(t);
                    }
            }

            return plant;
        }

        public IEnumerable<Leisure> GetLeisures()
        {
             var leisures = new List<Leisure>();
            var connString = "Host=localhost;Username=postgres;Password=admin;Database=postgres";
            var connection = new NpgsqlConnection(connString);
            connection.Open();
            var i = 0;
            using (var cmd = new NpgsqlCommand("SELECT * FROM leisure", connection))
             {
                 using (var reader = cmd.ExecuteReader())
                     while (reader.Read())
                     {
                         var t = new Leisure()
                         {
                             Id=i,
                         Name = reader.GetString(7),
                         Type = (LeisureType)Convert.ToInt32(reader.GetString(1)),
                         Position = new LatLng()
                         {
                             Lat = reader.GetDouble(2),
                             Lng = reader.GetDouble(3)
                         }
                         };
                        i++;
                        leisures.Add(t);
                     }
             }
             return leisures;
            return null;
        }

        public IEnumerable<AirQualityStation> GetAirQuality()
        {
            var quality = new List<AirQualityStation>();
            var stations = GetAllStations();
            using (var cmd = new NpgsqlCommand("SELECT * FROM air_quality", connection))
            {
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        var t =  new AirQualityStation()
                        {
                            AirQualityIndex = reader.GetDouble(13),                           
                        };
                        int stationId = reader.GetInt32(12);
                        var station = stations.FirstOrDefault(x => x.Id == stationId);
                        t.Position = new LatLng()
                        {
                            Lat = Convert.ToDouble(station.Latitude),
                            Lng = Convert.ToDouble(station.Longitude)
                        };
                        quality.Add(t);
                    }
            }
            return quality;
        }

        public IEnumerable<Station> GetAllStations()
        {

            var stations = new List<Station>();

            using (var cmd = new NpgsqlCommand("SELECT * FROM recording_stations", connection))
            {
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        stations.Add(new Station()
                        {
                            Name = reader.GetString(0),
                            Id = reader.GetInt32(1),
                            Latitude = Convert.ToString(reader.GetDouble(2)),
                            Longitude = Convert.ToString(reader.GetDouble(3))
                        });
                        string test = reader.GetString(0);
                        Console.WriteLine(test);
                    }
            }

            return stations;
        }


        public void Test()
        {

            /*
                        using ()
                        {
                            conn.Open();

                            // Insert some data
                            /*   using (var cmd = new NpgsqlCommand())
                               {
                                   cmd.Connection = conn;
                                   cmd.CommandText = "INSERT INTO data (some_field) VALUES (@p)";
                                   cmd.Parameters.AddWithValue("p", "Hello world");
                                   cmd.ExecuteNonQuery();
                               }

                            // Retrieve all rows
                            using (var cmd = new NpgsqlCommand("SELECT * FROM test", conn))
                            using (var reader = cmd.ExecuteReader())
                                while (reader.Read())
                                {
                                    string test = reader.GetString(0);
                                    Console.WriteLine(test);
                                }
                        }*/
        }




    }
}
