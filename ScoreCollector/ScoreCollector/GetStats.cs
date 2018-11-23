using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;

namespace ScoreCollector
{
    class GetStats
    {
        private double[] matchups = new double[5];
        private String rank;

        public double[] getAllStats(String[] teamChamps, String[] oppenentChamps, String rank)
        {
            if (rank.ToLower().Contains("diamond"))
                this.rank = "platplus";
            else if (rank.ToLower().Contains("platinum"))
                this.rank = "plat";
            else if (rank.ToLower().Contains("gold"))
                this.rank = "gold";
            else if (rank.ToLower().Contains("silver"))
                this.rank = "silver";
            else if (rank.ToLower().Contains("bronze"))
                this.rank = "bronze";

            Thread t1 = new Thread(() => getMatchup(teamChamps[0], oppenentChamps[0], "Top", 0));
            t1.Start();
            Thread t2 = new Thread(() => getMatchup(teamChamps[1], oppenentChamps[1], "Jungle", 1));
            t2.Start();
            Thread t3 = new Thread(() => getMatchup(teamChamps[2], oppenentChamps[2], "Middle", 2));
            t3.Start();
            Thread t4 = new Thread(() => getMatchup(teamChamps[3], oppenentChamps[3], "ADC", 3));
            t4.Start();
            Thread t5 = new Thread(() => getMatchup(teamChamps[4], oppenentChamps[4], "Support", 4));
            t5.Start();

            t1.Join();
            t2.Join();
            t3.Join();
            t4.Join();
            t5.Join();

            return matchups;
        }

        private void getMatchup(String teamChampion, String opponentChampion, String role, int index)
        {
            WebClient w = new WebClient();
            w.Encoding = Encoding.UTF8;
            teamChampion = teamChampion.Replace(".", "").Replace("Nunu &amp; Willump", "Nunu");//Mundo and Nunu
            opponentChampion = opponentChampion.Replace(".", "").Replace("Nunu &amp; Willump", "Nunu"); ;

            String scrape = w.DownloadString("http://champion.gg/champion/" + teamChampion + "/" + role + "?league=" + rank);
            List<String> l = new List<String>();

            double stat = 0.0;

            foreach (Match m in Regex.Matches(scrape, ",\"winRate\":(.*?),\"statScore\":"))
            {
                l.Add(m.Value);
            }

            opponentChampion = opponentChampion.Replace(" ", "");
            opponentChampion = getEdgeName(opponentChampion);
            foreach (String s in l)
            {
                if (s.Contains(opponentChampion))
                {
                    stat = Convert.ToDouble(Regex.Match(s, "\"winRate\":(.*?),").Value.Replace("\"winRate\":", "").Replace(",", ""));
                    break;
                }
            }

            stat *= 100;
            stat = Math.Round(stat, 2);
            matchups[index] = stat;
        }

        private String getEdgeName(String champ)
        {
            String newName = champ;
            if (champ == "Wukong")
                newName = "MonkeyKing";
            else if (champ.StartsWith("Cho"))
                newName = "Chogath";
            else if (champ.StartsWith("Kai"))
                newName = "Kaisa";
            else if (champ.StartsWith("Kog"))
                newName = "KogMaw";
            else if (champ.StartsWith("Kha"))
                newName = "Khazix";
            else if (champ.StartsWith("Rek"))
                newName = "RekSai";
            else if (champ.StartsWith("Vel"))
                newName = "Velkoz";

            return newName;
        }
    }
}
