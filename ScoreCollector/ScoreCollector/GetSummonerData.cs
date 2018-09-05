using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace ScoreCollector
{
    class GetSummonerData
    {
        public Data[] getData(String[] summoners)
        {
            Data[] teamSummonerData = new Data[5];

            WebClient w = new WebClient();
            w.Encoding = Encoding.UTF8;

            String scrape = w.DownloadString("http://na.op.gg/multi/query="+summoners[0]+ "%2C" + summoners[1]+ "%2C" + summoners[2]+ "%2C" + summoners[3]+ "%2C" + summoners[4]);
            HtmlAgilityPack.HtmlDocument htmlDocument = new HtmlAgilityPack.HtmlDocument();
            htmlDocument.LoadHtml(scrape);
            HtmlNodeCollection summonerData = htmlDocument.DocumentNode.SelectNodes("//div[@class='MultiSearchResultRow tabWrap']");
            if (summonerData == null)
                return null;
            int i = 0;
            foreach(HtmlNode n in summonerData)
            {
                if (i >= 5)//summonerData can be null.
                    break;
                teamSummonerData[i] = new Data();
                teamSummonerData[i].summonerName = n.SelectSingleNode(".//span[@class='SummonerName']").InnerText;
                int wins, losses;
                try { wins = Convert.ToInt16(n.SelectSingleNode(".//span[@class='Wins']").InnerText.Replace("W", "")); } catch (NullReferenceException) { wins = 0; }
                try { losses = Convert.ToInt16(n.SelectSingleNode(".//span[@class='Losses']").InnerText.Replace("L", "")); } catch (NullReferenceException) { losses = 0; }
                teamSummonerData[i].gameNum = wins + losses;
                teamSummonerData[i].winRate = (Double)wins / (wins + losses);
                try
                {
                    string hotStreak = n.SelectSingleNode(".//span[@class='WinStreak Wins']").InnerText.Trim();
                    if (Convert.ToInt16(hotStreak[0]+"") >= 3)
                        teamSummonerData[i].hotstreak = true;
                }
                catch(NullReferenceException){ teamSummonerData[i].hotstreak = false; }
                i++;
            }

            Data swap;
            Data temp;
            for(int k = 0; k < 5; k++)
            {
                for (int l = 0; l < 5; l++)
                {
                    if(teamSummonerData[l].summonerName.Equals(summoners[k]))
                    {
                        swap = teamSummonerData[l];
                        temp = teamSummonerData[k];
                        teamSummonerData[k] = swap;
                        teamSummonerData[l] = temp;
                        break;
                    }
                }
            }

            return teamSummonerData;

        }

   }

    class Data
    {
        public String summonerName;

        public Double winRate;

        public int gameNum;

        public bool hotstreak;
    }
}
