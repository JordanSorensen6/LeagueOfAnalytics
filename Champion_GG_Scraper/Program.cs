using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Champion_GG_Scraper
{
    class Program
    {
        static String currentChampion;
        static List<String> currentRoles;

        static void Main(string[] args)
        {
            WebClient w = new WebClient();
            w.Encoding = Encoding.UTF8;
            String scrape = w.DownloadString("https://champion.gg/");
            scrape = System.Net.WebUtility.HtmlDecode(scrape);//Convert html to unicode.

            HtmlAgilityPack.HtmlDocument htmlDocument = new HtmlAgilityPack.HtmlDocument();
            htmlDocument.LoadHtml(scrape);

            HtmlNodeCollection allChampions = htmlDocument.DocumentNode.SelectNodes("//div[@class='champ-height']");
            foreach (HtmlNode champion in allChampions)
            {
                currentChampion = champion.ChildNodes[1].ChildNodes[1].ChildNodes[1].ChildNodes[3].InnerText;
                Console.WriteLine(currentChampion);
                printPositions(champion.ChildNodes[1]);
                Console.WriteLine();
            }
            Console.ReadLine();

        }

        static void printPositions(HtmlNode n)
        {
            currentRoles = new List<String>();
            for(int i = 3; i < n.ChildNodes.Count; i++)
            {
                if(n.ChildNodes[i].Name.Equals("a"))
                {
                    currentRoles.Add(n.ChildNodes[i].InnerText.Trim());
                    Console.WriteLine(n.ChildNodes[i].InnerText.Trim());
                }
            }

            GetAllStats GAS = new GetAllStats(currentChampion, currentRoles);
        }
    }


}
