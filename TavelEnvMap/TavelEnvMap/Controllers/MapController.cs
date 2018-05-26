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

        [HttpGet]
        public IActionResult Trash(float lan, float lng)
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
            return Json(list);
        }
    }


}