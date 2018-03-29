using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ScoreCollector
{
    class GetMasteries
    {
        private int[] masteries = new int[5];

        public int[] getAllMasteries(String[] names, String[] champions)
        {
            Thread t1 = new Thread(() => getLevel(names[0], champions[0], 0));
            t1.Start();
            Thread t2 = new Thread(() => getLevel(names[1], champions[1], 1));
            t2.Start();
            Thread t3 = new Thread(() => getLevel(names[2], champions[2], 2));
            t3.Start();
            Thread t4 = new Thread(() => getLevel(names[3], champions[3], 3));
            t4.Start();
            Thread t5 = new Thread(() => getLevel(names[4], champions[4], 4));
            t5.Start();

            t1.Join();
            t2.Join();
            t3.Join();
            t4.Join();
            t5.Join();

            return masteries;
        }

        private void getLevel(String name, String champion, int index)//&#x27;
        {
            int level = 0;
            champion = champion.Replace("'", "&#x27;");
            WebClient w = new WebClient();
            w.Encoding = Encoding.UTF8;
            try
            {
                String scrape = w.DownloadString("http://championmasterylookup.derpthemeus.com/summoner?summoner=" + name + "&region=NA");

                HtmlAgilityPack.HtmlDocument htmlDocument = new HtmlAgilityPack.HtmlDocument();
                htmlDocument.LoadHtml(scrape);

                HtmlNodeCollection allChamps = htmlDocument.DocumentNode.SelectNodes("//tr");
                HtmlNodeCollection champData;
                foreach(HtmlNode node in allChamps)
                {
                    try
                    {
                        champData = node.SelectNodes(".//td");
                        if (champData[0].InnerText.Trim() == champion)
                        {
                            level = Convert.ToInt32(champData[1].InnerText.Trim());
                            break;
                        }
                    }
                    catch (NullReferenceException) { }
                }

                masteries[index] = level;
            }
            catch (WebException)
            {
                masteries[index] = 4;//No info on this summoner. 4 gives 0 points.
            }
        }
    }

}
