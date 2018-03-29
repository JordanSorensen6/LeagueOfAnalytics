using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;

public class GetMatchData
{
    private String summoner;
    private String scrape;
    public String rank;

    public GetMatchData()
    {

    }

    public List<data> getAllMatchInfo(String summ)
    {
        summoner = summ;
        WebClient w = new WebClient();
        w.Encoding = Encoding.UTF8;
        scrape = w.DownloadString("http://na.op.gg/summoner/userName=" + summoner);
        HtmlAgilityPack.HtmlDocument htmlDocument = new HtmlAgilityPack.HtmlDocument();
        htmlDocument.LoadHtml(scrape);

        HtmlNodeCollection allMatches = htmlDocument.DocumentNode.SelectNodes("//div[@class='GameItemWrap']");
        List<data> matchInfo = new List<data>();

        try
        {
            rank = htmlDocument.DocumentNode.SelectSingleNode("//div[@class='TierRank']").InnerText.Trim();
        }
        catch(NullReferenceException)
        {
            return matchInfo;
        }

        HtmlNodeCollection gt;

        HtmlNodeCollection names;

        HtmlNodeCollection champs;


        data d;

        String[] allSummoners;

        try
        {
            foreach (HtmlNode n in allMatches)
            {
                gt = n.SelectNodes(".//div[@class='GameType']");
                if (gt[0].InnerText.Trim() == "Ranked Solo")
                {
                    d = new data();
                    d.result = n.SelectSingleNode(".//div[@class='GameResult']").InnerText.Trim();

                    names = n.SelectNodes(".//div[@class='SummonerName']");
                    int count = 0;
                    allSummoners = new String[10];
                    foreach (HtmlNode node in names)//Get summoner names.
                    {
                        allSummoners[count++] = node.InnerText.Trim();
                    }

                    String[] team = getTeam(allSummoners, summ);
                    d.teamSummonerTop = team[0];
                    d.teamSummonerJungle = team[1];
                    d.teamSummonerMid = team[2];
                    d.teamSummonerBot = team[3];
                    d.teamSummonerSupp = team[4];

                    String[] allChamps = new String[10];

                    champs = n.SelectNodes(".//div[@class='ChampionImage']");//Get champion names.
                    for (int i = 1; i < 11; i++)
                    {
                        allChamps[i - 1] = champs[i].InnerText.Remove(0, champs[i].InnerText.Length / 2).Trim().Replace("&#039;", "'");
                    }

                    if (team[0] == allSummoners[0])
                    {
                        d.teamChampionTop = allChamps[0];
                        d.teamChampionJungle = allChamps[1];
                        d.teamChampionMid = allChamps[2];
                        d.teamChampionBot = allChamps[3];
                        d.teamChampionSupp = allChamps[4];

                        d.opponentChampionTop = allChamps[5];
                        d.opponentChampionJungle = allChamps[6];
                        d.opponentChampionMid = allChamps[7];
                        d.opponentChampionBot = allChamps[8];
                        d.opponentChampionSupp = allChamps[9];
                    }
                    else
                    {

                        d.opponentChampionTop = allChamps[0];
                        d.opponentChampionJungle = allChamps[1];
                        d.opponentChampionMid = allChamps[2];
                        d.opponentChampionBot = allChamps[3];
                        d.opponentChampionSupp = allChamps[4];

                        d.teamChampionTop = allChamps[5];
                        d.teamChampionJungle = allChamps[6];
                        d.teamChampionMid = allChamps[7];
                        d.teamChampionBot = allChamps[8];
                        d.teamChampionSupp = allChamps[9];
                    }
                    matchInfo.Add(d);
                }
            }
        }
        catch (NullReferenceException) { }

        return matchInfo;
    }

    private String[] getTeam(String[] summoners, String summ)
    {
        int pos = 0;
        for(int i = 0; i < 10; i++)
        {
            if (summoners[i] == summ)
            {
                pos = i;
                break;
            }
        }

        String[] team = new String[5];

        if(pos < 5)
        {
            for(int i = 0; i < 5; i++)
            {
                team[i] = summoners[i];
            }
            return team;
        }

        else
        {
            for (int i = 5; i < 10; i++)
            {
                team[i-5] = summoners[i];
            }
            return team;
        }
    }


    public class data
    {
        public String result;

        public String teamSummonerTop;
        public String teamSummonerJungle;
        public String teamSummonerMid;
        public String teamSummonerBot;
        public String teamSummonerSupp;

        public String teamChampionTop;
        public String teamChampionJungle;
        public String teamChampionMid;
        public String teamChampionBot;
        public String teamChampionSupp;

        public String opponentChampionTop;
        public String opponentChampionJungle;
        public String opponentChampionMid;
        public String opponentChampionBot;
        public String opponentChampionSupp;
    }
}