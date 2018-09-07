using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace ScoreCreator
{
    class Program
    {
        static void Main(string[] args)
        {
            StreamReader sr = File.OpenText(@"C:\Users\Mitchell\Desktop\LOGGOLD02.txt");
            Dictionary<Double, Pair> scoreCard = new Dictionary<Double, Pair>();
            string line = sr.ReadLine();
            String[] matchData = new String[5];
            int gameNum = 0;
            String result = "";
            String[] masteries;
            String[] matchups;
            String[] winrates;
            String[] numGames;
            String[] hotStreaks;


            while (line != null)
            {
                if (line.Equals(""))
                {
                    line = sr.ReadLine();
                    continue;
                }

                if (line.Contains("Game#: "))
                {
                    Console.WriteLine("Game: " + gameNum++);
                    result = getBetweenString(line, "Result: ", "Game#:");
                    sr.ReadLine();

                    for (int i = 0; i < 5; i++)
                        matchData[i] = sr.ReadLine();
                }

                masteries = getAttributes(matchData, "Mastery: ", "Matchup:");
                matchups = getAttributes(matchData, "Matchup: ", "WinRate:");
                winrates = getAttributes(matchData, "WinRate: ", "NumGames:");
                numGames = getAttributes(matchData, "NumGames: ", "HotStreak");
                hotStreaks = getHotStreak(matchData);

                Double score = calculateScore(masteries, matchups, winrates, numGames, hotStreaks);


                if (scoreCard.ContainsKey(score))
                {
                    if (result == "Victory")
                        scoreCard[score].wins += 1;
                    else if (result == "Defeat")
                        scoreCard[score].losses += 1;
                }
                else
                {
                    scoreCard.Add(score, new Pair());

                    if (result == "Victory")
                        scoreCard[score].wins += 1;
                    else if (result == "Defeat")
                        scoreCard[score].losses += 1;
                }

                line = sr.ReadLine();
            }

            //For Graph
            //foreach (Double score in scoreCard.Keys.OrderByDescending(key => key))
            //{
            //    Console.WriteLine(score);
            //}

            //Console.WriteLine();
            //For Graph




            String results;
            foreach (Double score in scoreCard.Keys.OrderByDescending(key => key))
            {
                //For standard
                results = score + " --- " + scoreCard[score].wins + ":" + scoreCard[score].losses + " --- " + Math.Round(((((Double)scoreCard[score].wins)/(scoreCard[score].wins + scoreCard[score].losses))*100), 2);
                //For standard

                //For Graph
                //results = Math.Round(((((Double)scoreCard[score].wins) / (scoreCard[score].wins + scoreCard[score].losses)) * 100), 2) + "";
                //For Graph
                Console.WriteLine(results);
            }

            Console.ReadLine();
        }

        static Double calculateScore(String[] masteries, String[] matchups, String[] winrates, String[] numGames, String[] hotStreaks)
        {
            Double[] masteryScores = new Double[5];
            Double[] matchupScores = new Double[5];
            Double[] winrateScores = new Double[5];
            Double[] hotStreakScores = new Double[5];

            for (int i = 0; i < 5; i++)//Score champion mastery
            {
                switch (Convert.ToInt16(masteries[i]))
                {
                    case 1:
                        masteryScores[i] -= 1;
                        break;
                    case 2:
                        masteryScores[i] -= 1;
                        break;
                    case 3:
                        masteryScores[i] -= .5;
                        break;
                    case 4:
                        masteryScores[i] -= .5;
                        break;
                    case 5:
                        masteryScores[i] += 0;
                        break;
                    case 6:
                        masteryScores[i] += .5;
                        break;
                    case 7:
                        masteryScores[i] += 1;
                        break;
                }
            }

            Double matchup;
            for(int i = 0; i < 5; i++)//Score champion matchup
            {
                matchup = Convert.ToDouble(matchups[i]);
                if (matchup != 0)
                {
                    if (matchup < 50)
                        matchupScores[i] -= .5;
                    else
                        matchupScores[i] += .5;

                    //Test in depth
                    //if (matchup < 47.5)
                    //    matchupScores[i] -= 1;
                    //else if (matchup < 50)
                    //    matchupScores[i] -= .5;
                    //else if (matchup < 52.5)
                    //    matchupScores[i] += .5;
                    //else
                    //    matchupScores[i] += 1;
                    //Test in depth
                }
            }

            Double winrate;
            for(int i = 0; i < 5; i++)//Score player winrate
            {
                if (!winrates[i].Equals("NaN"))
                {
                    winrate = Convert.ToDouble(winrates[i]);
                    if (winrate < 50)
                        winrateScores[i] -= .5;
                    else
                        winrateScores[i] += .5;

                    //Test in depth
                    //if (winrate < 45)
                    //    winrateScores[i] -= 1;
                    //else if (winrate < 50)
                    //    winrateScores[i] -= .5;
                    //else if (winrate < 55)
                    //    winrateScores[i] += .5;
                    //else
                    //    winrateScores[i] += 1;
                    //Test in depth
                }
            }

            for(int i = 0; i < 5; i++)//Score hotstreak
            {
                if (hotStreaks[i].Equals("True"))
                    hotStreakScores[i] += .5;
            }

            Double score = 0;

            for (int i = 0; i < 5; i++)//Add
            {
                //Test compound
                //if (Convert.ToInt16(masteries[i]) != 0 && Convert.ToDouble(matchups[i]) != 0)
                //{
                //    if (Convert.ToInt16(masteries[i]) > 5 && Convert.ToDouble(matchups[i]) > 52.5)
                //        score += 1;

                //    if (Convert.ToInt16(masteries[i]) < 5 && Convert.ToDouble(matchups[i]) < 47.5)
                //        score -= 1;
                //}
                //Test compound

                score += (masteryScores[i] + matchupScores[i] + winrateScores[i] + hotStreakScores[i]);
            }
            return score;
        }



        static String[] getAttributes(String[] data, String before, String after)
        {
            String[] attributes = new String[5];
            for (int i = 0; i < 5; i++)
            {
                attributes[i] = getBetweenString(data[i], before, after);
            }

            return attributes;
        }

        static String getBetweenString(String s, String firstString, String secondString)
        {
            int pFrom = s.IndexOf(firstString) + firstString.Length;
            int pTo = s.LastIndexOf(secondString);

            return s.Substring(pFrom, pTo - pFrom).Trim();
        }

        static String[] getHotStreak(String[] data)
        {
            Regex reg = new Regex("(?<=HotStreak:).*$");
            String[] hotStreaks = new String[5];
            for (int i = 0; i < 5; i++)
            {
                hotStreaks[i] = reg.Match(data[i]).Value.Trim();
            }

            return hotStreaks;
        }
    }

    public class Pair
    {
        public int wins = 0;
        public int losses = 0;
    }
}
