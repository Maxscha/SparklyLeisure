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
            var list = Database.DataBaseAccess.DataBase.GetEnvIssue();//DataBaseHelper.GetEnvIssues(lan, lng);
            return Json(list);
        }

        [HttpGet]
        public IActionResult AirQualityStations(float lat, float lng)
        {
            var stations = Database.DataBaseAccess.DataBase.GetAirQuality();// DataBaseHelper.GetAirQualityStations(lat, lng);
            return Json(stations);
        }

        [HttpGet]
        public IActionResult Politician(float lan, float lng)
        {
            var politicians = DataBaseHelper.GetPoliticans(lan, lng);
            return Json(politicians);
        }

        [HttpGet]
        public IActionResult Leissure(float lat, float lng)
        {
            var leissures = Database.DataBaseAccess.DataBase.GetLeisures();//DataBaseHelper.GetLeisures(lat, lng);
            return Json(leissures);
        }

        [HttpGet]
        public IActionResult CoalPlant(float lat, float lng)
        {
            var leissures = Database.DataBaseAccess.DataBase.GetCoal();//DataBaseHelper.GetLeisures(lat, lng);
            return Json(leissures);
        }

        [HttpPost]
        public IActionResult PutData(dynamic all)
        {
            return Json(true);
        }
    }


}