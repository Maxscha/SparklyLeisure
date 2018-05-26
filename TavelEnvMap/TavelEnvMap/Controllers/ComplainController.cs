using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using TavelEnvMap.Models;

namespace TavelEnvMap.Controllers
{
    public class ComplainController : Controller
    {
        public IActionResult Index()
        {
            ComplainModel model = new ComplainModel()
            {
                Politician = DataBaseHelper.GetPoliticans(0,0)
            };
            return View(model);
        }
    }
}