using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ScoreCollector
{
    class Log
    {
        public void logInfo(String[] teamSummoners, String[] teamChampions, String[] opponentChampions, String mainSummoner, Data[] teamData, int[] masteries, double[] matchups, int gameNum, String matchResult, String rank, StreamWriter io)
        {
            String[] positions = { "Top", "Jungle", "Middle", "Bottom", "Support" };
            String line1 = "Summoner: " + mainSummoner + "   Rank: " + rank + "   Result: " + matchResult + "   Game#: " + gameNum + "\r\n";
            String line2 = "-----------------------------------------------------------------------------------------------------------------------------------------------------" + "\r\n";
            String body = "";
            for(int i = 0; i < 5; i++)
                body += positions[i]+ ": " + teamSummoners[i] + "   Playing: " + teamChampions[i] + "   Opponent: " + opponentChampions[i] + "   Mastery: " + masteries[i] + "   Matchup: " + matchups[i] + "   WinRate: " + normalize(teamData[i].winRate) + "   NumGames: " + teamData[i].gameNum + "   HotStreak: " + teamData[i].hotstreak + "\r\n";

            line1 += line2 += body += "\r\n";

            Console.WriteLine(line1);
            io.WriteLine(line1);
        }

        private double normalize(double num)
        {
            return Math.Round(num *= 100, 2);
        }
    }
}
