using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace DataAnalyzer
{
    class Program
    {
        static void Main(string[] args)
        {
            StreamReader sr = File.OpenText(@"C:\Users\Mitchell\Desktop\LOGSILVER01.txt");
            string line = sr.ReadLine();
            String[] matchData = new String[5];
            Double winAvg = 0;
            Double lossAvg = 0;
            int wins = 0;
            int losses = 0;
            String result = "";
            Double matchAvg = 0;


            int count = 0;
            while(line != null)
            {
                if (line.Equals(""))
                {
                    line = sr.ReadLine();
                    continue;
                }
                if (line.Contains("Game#: "))
                {
                    Console.WriteLine("Game: " + count++);
                    result = getBetweenString(line, "Result: ", "Game#:");
                    sr.ReadLine();
                    matchData[0] = sr.ReadLine();
                    matchData[1] = sr.ReadLine();
                    matchData[2] = sr.ReadLine();
                    matchData[3] = sr.ReadLine();
                    matchData[4] = sr.ReadLine();
                }

                //String[] attributes = getAttributes(matchData, "NumGames: ", "HotStreak:");//Change this
                String[] attributes = getHotStreak(matchData);
                matchAvg = computeAvg(attributes);
                if(result.Equals("Victory"))
                {
                    winAvg += matchAvg;
                    wins++;
                    Console.WriteLine("Win Average: " + winAvg/wins);
                }
                else if(result.Equals("Defeat"))
                {
                    lossAvg += matchAvg;
                    losses++;
                    Console.WriteLine("Loss Average: " + lossAvg / losses);
                }
                line = sr.ReadLine();
            }

            Console.ReadLine();
        }

        static Double computeAvg(String[] attributes)
        {
            Double avg = 0;
            int div = 0;
            for(int i = 0; i < 5; i++)
            {
                //Hotstreak
                if (attributes[i].Equals("True"))
                {
                    avg += 1;
                    div++;
                }
                else if (attributes[i].Equals("False"))
                {
                    div++;
                }
                //Hotstreak
                //if (!attributes[i].Equals("NaN") && !attributes[i].Equals("0"))
                //{
                //    div++;
                //    avg += Convert.ToDouble(attributes[i]);
                //}

            }
            return avg / div;
        }

        static String[] getAttributes(String[] data, String before, String after)
        {
            String[] attributes = new String[5];
            for(int i = 0; i < 5; i++)
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
            for(int i = 0; i < 5; i++)
            {
                hotStreaks[i] = reg.Match(data[i]).Value.Trim();
            }

            return hotStreaks;
        }
    }
}
