using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace ScoreCollector
{
    class Program
    {
        static void Main(string[] args)
        {
            ///////////////////////////////////////////////////////////////////////////////////////////////
            String currentRank = "Silver";//Enter the rank you would like to gather data on. (Diamond, Platinum, Gold, Silver, Bronze) This should corrilate with the rank file. 
            StreamReader sr = File.OpenText(@"C:\Users\Mitchell\Desktop\score_collector\LeagueOfAnalytics\LOA Data\Silver.txt");//Enter the file location of the list of summoners in a particular rank. (Diamond.txt, Platinum.txt, Gold.txt, Silver.txt, Bronze.txt)
            StreamWriter scoreLog = File.CreateText(@"C:\Users\Mitchell\Desktop\LOGSILVER02.txt");//Enter the file location for log file. You can call the file whatever you want. Make sure you change the name after each run to avoid overwriting data.
            String startingSummoner = "hackedagen";//Enter a summoner from the list of summoners to start collecting data or continue from last point.
            ///////////////////////////////////////////////////////////////////////////////////////////////


            Dictionary<Double, Pair> scoreCard = new Dictionary<Double, Pair>();
            Log log = new Log();

            int gameNum = 1;

            String summoner = Regex.Match(sr.ReadLine(), "^.*(?=( --- ))").Value;
            while (summoner != startingSummoner)
                summoner = Regex.Match(sr.ReadLine(), "^.*(?=( --- ))").Value;
            while (summoner != null)
            {   
                GetMatchData d = new GetMatchData();
                List<GetMatchData.data> matches = d.getAllMatchInfo(summoner);
                Double totalScore;
                foreach (GetMatchData.data match in matches)
                {

                    GetStats gs = new GetStats();
                    double[] stats = gs.getAllStats(getTeamChampions(match), getOpponentChampions(match), d.rank);

                    if (checkIncorrectPositions(stats) && d.rank.Contains(currentRank))
                    {
                        GetMasteries gm = new GetMasteries();
                        int[] masteries = gm.getAllMasteries(getTeamSummonerNames(match), getTeamChampions(match));

                        GetSummonerData gsd = new GetSummonerData();
                        Data[] teamData = gsd.getData(getTeamSummonerNames(match));
                        if (teamData == null)
                            continue;
                        log.logInfo(getTeamSummonerNames(match), getTeamChampions(match), getOpponentChampions(match), summoner, teamData, masteries, stats, gameNum++, match.result, d.rank, scoreLog);
                        //double topScore = getScore(masteries[0], stats[0]);
                        //double jungleScore = getScore(masteries[1], stats[1]);
                        //double midScore = getScore(masteries[2], stats[2]);
                        //double botScore = getScore(masteries[3], stats[3]);
                        //double supportScore = getScore(masteries[4], stats[4]);

                        //String line1 = "Testing Summoner: " + summoner + "   Rank: " + d.rank + "   Result: " + match.result;
                        //Console.WriteLine(line1);
                        //scoreLog.WriteLine(line1);
                        //String line2 = "------------------------------------------------------------------------------";
                        //Console.WriteLine(line2);
                        //scoreLog.WriteLine(line2);
                        //String line3 = match.teamSummonerTop + " - " + match.teamChampionTop + "(" + masteries[0] + ")" + "  vs  " + match.opponentChampionTop + "  Matchup: " + stats[0] + "  Score: " + topScore;
                        //Console.WriteLine(line3);
                        //scoreLog.WriteLine(line3);
                        //String line4 = match.teamSummonerJungle + " - " + match.teamChampionJungle + "(" + masteries[1] + ")" + "  vs  " + match.opponentChampionJungle + "  Matchup: " + stats[1] + "  Score: " + jungleScore;
                        //Console.WriteLine(line4);
                        //scoreLog.WriteLine(line4);
                        //String line5 = match.teamSummonerMid + " - " + match.teamChampionMid + "(" + masteries[2] + ")" + "  vs  " + match.opponentChampionMid + "  Matchup: " + stats[2] + "  Score: " + midScore;
                        //Console.WriteLine(line5);
                        //scoreLog.WriteLine(line5);
                        //String line6 = match.teamSummonerBot + " - " + match.teamChampionBot + "(" + masteries[3] + ")" + "  vs  " + match.opponentChampionBot + "  Matchup: " + stats[3] + "  Score: " + botScore;
                        //Console.WriteLine(line6);
                        //scoreLog.WriteLine(line6);
                        //String line7 = match.teamSummonerSupp + " - " + match.teamChampionSupp + "(" + masteries[4] + ")" + "  vs  " + match.opponentChampionSupp + "  Matchup: " + stats[4] + "  Score: " + supportScore;
                        //Console.WriteLine(line7);
                        //scoreLog.WriteLine(line7);
                        //totalScore = topScore + jungleScore + midScore + botScore + supportScore;
                        //Console.WriteLine("Total Score: " + totalScore);
                        //scoreLog.WriteLine("Total Score: " + totalScore);
                        //Console.WriteLine();
                        //scoreLog.WriteLine();

                        //if (scoreCard.ContainsKey(totalScore))
                        //{
                        //    if (match.result == "Victory")
                        //        scoreCard[totalScore].wins += 1;
                        //    else if (match.result == "Defeat")
                        //        scoreCard[totalScore].losses += 1;
                        //}
                        //else
                        //{
                        //    scoreCard.Add(totalScore, new Pair());

                        //    if (match.result == "Victory")
                        //        scoreCard[totalScore].wins += 1;
                        //    else if (match.result == "Defeat")
                        //        scoreCard[totalScore].losses += 1;
                        //}

                        //String results;
                        //foreach (Double score in scoreCard.Keys.OrderByDescending(key => key))
                        //{
                        //    results = score + " --- " + scoreCard[score].wins + ":" + scoreCard[score].losses + " --- " + (scoreCard[score].wins + scoreCard[score].losses);
                        //    Console.WriteLine(results);
                        //    scoreLog.WriteLine(results);
                        //}

                        //Console.WriteLine();
                        //scoreLog.WriteLine();
                    }
                }

                summoner = Regex.Match(sr.ReadLine(), "^.*(?=( --- ))").Value;
            }

            Console.ReadLine();
        }

        private static bool checkIncorrectPositions(double[] stats)//If champions are in incorret positions, throw out the data.
        {
            int count = 0;
            foreach (double d in stats)
                if (d == 0)
                    count++;
            return count <= 2;
        }

        private static double getScore(int mastery, double matchup)
        {
            Double score = 0;

            if (mastery <= 2)
                score -= 1;
            else if (mastery == 3)
                score -= .5;
            else if (mastery == 4)
                score += 0;
            else if (mastery == 5)
                score += .5;
            else if (mastery >= 6)
                score += 1;

            if (matchup != 0)
            {
                if (matchup < 48)
                    score -= 1;
                else if (matchup < 50)
                    score -= .5;
                else if (matchup < 52)
                    score += .5;
                else
                    score += 1;

                if (mastery < 3 && matchup < 48)//Lane is looking really bad.
                    score -= 1;
                else if (mastery == 3 && matchup < 50)//Lane is looking bad.
                    score -= .5;
                else if (mastery == 4 && matchup < 45)//Bad
                    score -= .5;
                else if (mastery == 4 && matchup > 55)//Good
                    score += .5;
                else if (mastery >= 5 && matchup >= 50 && matchup <= 52)//Lane is looking good.
                    score += .5;
                else if (mastery > 5 && matchup > 52)//Lane is looking really good.
                    score += 1;
            }

            return score;
        }

        public static String[] getTeamSummonerNames(GetMatchData.data info)
        {
            String[] names = new String[5];

            names[0] = info.teamSummonerTop;
            names[1] = info.teamSummonerJungle;
            names[2] = info.teamSummonerMid;
            names[3] = info.teamSummonerBot;
            names[4] = info.teamSummonerSupp;

            return names;
        }

        public static String[] getTeamChampions(GetMatchData.data info)
        {
            String[] champs = new String[5];

            champs[0] = info.teamChampionTop;
            champs[1] = info.teamChampionJungle;
            champs[2] = info.teamChampionMid;
            champs[3] = info.teamChampionBot;
            champs[4] = info.teamChampionSupp;

            return champs;
        }

        public static String[] getOpponentChampions(GetMatchData.data info)
        {
            String[] champs = new String[5];

            champs[0] = info.opponentChampionTop;
            champs[1] = info.opponentChampionJungle;
            champs[2] = info.opponentChampionMid;
            champs[3] = info.opponentChampionBot;
            champs[4] = info.opponentChampionSupp;

            return champs;
        }

        public class Pair
        {
            public int wins = 0;
            public int losses = 0;
        }

    }
}
