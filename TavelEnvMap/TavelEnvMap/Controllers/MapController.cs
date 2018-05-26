using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using TavelEnvMap.Models;

namespace TavelEnvMap.Controllers
{
    [Produces("application/json")]
    //[Route("api/Map")]
    public class MapController : Controller
    {


        [HttpGet]
        public IActionResult Trash(float lan, float lng)
        {
            var list = DataBaseHelper.GetEnvIssues(lan, lng);
            return Json(list);
        }

        [HttpGet]
        public IActionResult AirQualityStations(float lat, float lng)
        {
            var stations = DataBaseHelper.GetAirQualityStations(lat, lng);
            return Json(stations);
        }

        public IActionResult Politician(float lan, float lng)
        {
            var politicians = DataBaseHelper.GetPoliticans(lan, lng);
            return Json(politicians);
        }
    }


}