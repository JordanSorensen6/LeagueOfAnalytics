using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Web.Script.Serialization;


namespace Champion_GG_Scraper
{
    class GetAllStats
    {
        public Role R;
        public String currentRole;

        public GetAllStats(String champion, List<String> roles)
        {
            R = new Role();
            String url;
            champion = champion.Replace(".", "");
            foreach (String role in roles)
            {
                currentRole = role;
                url = "https://champion.gg/champion/" + champion + "/" + role + "?&league=";
                switch (role)
                {
                    case "Top":
                        R.top = new Elo();
                        getEloData(url, R.top);
                        break;
                    case "Jungle":
                        R.jungle = new Elo();
                        getEloData(url, R.jungle);
                        break;
                    case "Middle":
                        R.middle = new Elo();
                        getEloData(url, R.middle);
                        break;
                    case "ADC":
                        R.bottom = new Elo();
                        getEloData(url, R.bottom);
                        break;
                    case "Support":
                        R.support = new Elo();
                        getEloData(url, R.support);
                        break;
                }
            }
            String json = new JavaScriptSerializer().Serialize(R);
            System.IO.File.WriteAllText(@"C:\Users\Mitchell\Desktop\Champion_GG_JSON\"+champion+".json", json);
        }

        public void getEloData(String url, Elo e)
        {
            WebClient w = new WebClient();
            w.Encoding = Encoding.UTF8;
            String platPlus = w.DownloadString(url + "platplus");
            String plat = w.DownloadString(url + "plat");
            String gold = w.DownloadString(url + "gold");
            String silver = w.DownloadString(url + "silver");
            String bronze = w.DownloadString(url + "bronze");

            extractData(platPlus, e, "platplus");
            extractData(plat, e, "plat");
            extractData(gold, e, "gold");
            extractData(silver, e, "silver");
            extractData(bronze, e, "bronze");

        }

        public void extractData(String scrape, Elo e, String elo)
        {
            String jsonStr = getBetweenString(scrape, "matchupData.championData = ", "matchupData.patchHistory = ");
            JObject details = JObject.Parse(jsonStr);
            int size = details["matchups"].Count();

            switch(elo)
            {
                case "platplus":
                    e.platinumPlus = new WinRate[size];
                    fillData(e.platinumPlus, details, size);
                    break;
                case "plat":
                    e.platinum = new WinRate[size];
                    fillData(e.platinum, details, size);
                    break;
                case "gold":
                    e.gold = new WinRate[size];
                    fillData(e.gold, details, size);
                    break;
                case "silver":
                    e.silver = new WinRate[size];
                    fillData(e.silver, details, size);
                    break;
                case "bronze":
                    e.bronze = new WinRate[size];
                    fillData(e.bronze, details, size);
                    break;
            }
           // var games = details["matchups"][1]["games"];
        }

        public void fillData(WinRate[] matchupData, JObject extractedData, int size)
        {
            for(int i = 0; i < size; i++)
            {
                matchupData[i] = new WinRate();
                matchupData[i].opponentChampion = (String)extractedData["matchups"][i]["key"];
                matchupData[i].winRate = (Double)extractedData["matchups"][i]["winRate"];
            }
        }

        public String getBetweenString(String s, String firstString, String secondString)
        {
            int pFrom = s.IndexOf(firstString) + firstString.Length;
            int pTo = s.LastIndexOf(secondString);

            return s.Substring(pFrom, pTo - pFrom);
        }
    }

    class Role
    {
        public Elo top;
        public Elo jungle;
        public Elo middle;
        public Elo bottom;
        public Elo support;
    }

    class Elo
    {
        public WinRate[] platinumPlus;
        public WinRate[] platinum;
        public WinRate[] gold;
        public WinRate[] silver;
        public WinRate[] bronze;
    }

    class WinRate
    {
        public String opponentChampion;
        public Double winRate;
    }
}
