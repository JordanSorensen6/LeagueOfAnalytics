using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HtmlAgilityPack;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Net;
using System.Diagnostics;

namespace LossPreventionv4
{
    public partial class Form1 : Form
    {

        private TextBox[] TB = new TextBox[5];
        private String[] MasteryData = new String[5];
        private String[] MatchupData = new String[5];
        private Double[] MasteryScores = new Double[] {0,0,0,0,0};
        private Double[] MatchupScores = new Double[] {0,0,0,0,0};
        private int previousSelected;
        private HtmlAgilityPack.HtmlDocument htmlDocument;

        public Form1()
        {
            InitializeComponent();

            comboBox1.Items.Add("Bronze");
            comboBox1.Items.Add("Silver");
            comboBox1.Items.Add("Gold");
            comboBox1.Items.Add("Platinum");
            comboBox1.Items.Add("Diamond");
            comboBox1.SelectedIndex = 2;
            comboBox1.DropDownStyle = ComboBoxStyle.DropDownList;


            TB[0] = textBox1;
            TB[1] = textBox2;
            TB[2] = textBox3;
            TB[3] = textBox4;
            TB[4] = textBox5;
            textBox1.MouseClick += markForSwap;
            textBox2.MouseClick += markForSwap;
            textBox3.MouseClick += markForSwap;
            textBox4.MouseClick += markForSwap;
            textBox5.MouseClick += markForSwap;

            textBox6.GotFocus += getMasteryInfo;
            textBox7.GotFocus += getMasteryInfo;
            textBox8.GotFocus += getMasteryInfo;
            textBox9.GotFocus += getMasteryInfo;
            textBox10.GotFocus += getMasteryInfo;

            textBox6.TextChanged += printMasteryInfo;
            textBox7.TextChanged += printMasteryInfo;
            textBox8.TextChanged += printMasteryInfo;
            textBox9.TextChanged += printMasteryInfo;
            textBox10.TextChanged += printMasteryInfo;

            textBox6.LostFocus += getMatchupInformation;
            textBox7.LostFocus += getMatchupInformation;
            textBox8.LostFocus += getMatchupInformation;
            textBox9.LostFocus += getMatchupInformation;
            textBox10.LostFocus += getMatchupInformation;

            textBox16.LostFocus += checkForMatchup;
            textBox17.LostFocus += checkForMatchup;
            textBox18.LostFocus += checkForMatchup;
            textBox19.LostFocus += checkForMatchup;
            textBox20.LostFocus += checkForMatchup;
        }


        private void checkForMatchup(object sender, EventArgs e)
        {
            TextBox send = sender as TextBox;
            if (send.Name == "textBox16")
            {
                if (textBox6.Text != "" && textBox16.Text != "")
                    extractMatchupInformation(0);
            }
            else if (send.Name == "textBox17")
            {
                if (textBox7.Text != "" && textBox17.Text != "")
                    extractMatchupInformation(1);
            }
            else if (send.Name == "textBox18")
            {
                if (textBox8.Text != "" && textBox18.Text != "")
                    extractMatchupInformation(2);
            }
            else if (send.Name == "textBox19")
            {
                if (textBox9.Text != "" && textBox19.Text != "")
                    extractMatchupInformation(3);
            }
            else if (send.Name == "textBox20")
            {
                if (textBox10.Text != "" && textBox20.Text != "")
                    extractMatchupInformation(4);
            }
        }

        private void getMatchupInformation(object sender, EventArgs e)
        {
            //Debug.WriteLine("Getting champion.gg info.");
            TextBox caller = sender as TextBox;

            if (caller.Text == "")
                return;

            int index = 0;
            String champName = caller.Text;
            String role = "";
            String league = comboBox1.Text;
            if (caller.Name == "textBox6")
            {
                role = "Top";
                index = 0;
            }
            else if (caller.Name == "textBox7")
            {
                role = "Jungle";
                index = 1;
            }
            else if (caller.Name == "textBox8")
            {
                role = "Middle";
                index = 2;
            }
            else if (caller.Name == "textBox9")
            {
                role = "ADC";
                index = 3;
            }
            else if (caller.Name == "textBox10")
            {
                role = "Support";
                index = 4;
            }
            String htmlCode;
            using (WebClient client = new WebClient())
            {
                champName = getEdgeName(champName);
            
                if (league != "Diamond")
                    htmlCode = client.DownloadString("http://champion.gg/champion/"+champName+"/"+role+"?league="+league.ToLower());
                else
                    htmlCode = client.DownloadString("http://champion.gg/champion/"+champName+"/"+role);
            }
            MatchupData[index] = htmlCode;

            if (index == 0 && textBox16.Text != "")
                extractMatchupInformation(index);
            else if (index == 1 && textBox17.Text != "")
                extractMatchupInformation(index);
            else if (index == 2 && textBox18.Text != "")
                extractMatchupInformation(index);
            else if (index == 3 && textBox19.Text != "")
                extractMatchupInformation(index);
            else if (index == 4 && textBox20.Text != "")
                extractMatchupInformation(index);
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
            else if (champ.StartsWith("Rek"))
                newName = "RekSai";
            else if (champ.StartsWith("Vel"))
                newName = "Velkoz";

            return newName;
        }

        private void extractMatchupInformation(int index)
        {
            List <String> l = new List<String>();
            String champ = "";
            Double stat = 0.0;
            if (index == 0)
                champ = textBox16.Text;
            else if (index == 1)
                champ = textBox17.Text;
            else if (index == 2)
                champ = textBox18.Text;
            else if (index == 3)
                champ = textBox19.Text;
            else if (index == 4)
                champ = textBox20.Text;

            champ = getEdgeName(champ);

            foreach (Match m in Regex.Matches(MatchupData[index], ",\"winRate\":(.*?),\"statScore\":"))
            {
                l.Add(m.Value);
            }
            foreach (String s in l)
            {
                if(s.Contains(champ))
                {
                    stat = Convert.ToDouble(Regex.Match(s, "\"winRate\":(.*?),").Value.Replace("\"winRate\":", "").Replace(",", ""));
                    break;
                }
            }

            stat *= 100;
            stat = Math.Round(stat, 2);
            if (stat != 0)//There is data on the matchup.
            {
                if (index == 0)
                {
                    textBox21.Text = stat + "";
                    setMatchupScore(stat, label6);
                }
                else if (index == 1)
                {
                    textBox22.Text = stat + "";
                    setMatchupScore(stat, label7);
                }
                else if (index == 2)
                {
                    textBox23.Text = stat + "";
                    setMatchupScore(stat, label8);
                }
                else if (index == 3)
                {
                    textBox24.Text = stat + "";
                    setMatchupScore(stat, label9);
                }
                else if (index == 4)
                {
                    textBox25.Text = stat + "";
                    setMatchupScore(stat, label10);
                }
            }
            else//No data on the matchup.
            {
                if (index == 0)
                {
                    textBox21.Text = "?";
                }
                else if (index == 1)
                {
                    textBox22.Text = "?";
                }
                else if (index == 2)
                {
                    textBox23.Text = "?";
                }
                else if (index == 3)
                {
                    textBox24.Text = "?";
                }
                else if (index == 4)
                {
                    textBox25.Text = "?";
                }
            }
        }

        private void setMatchupScore(Double stat, Label l)
        {
            int index = 0;
            if (l.Name == "label6")
                index = 0;
            else if (l.Name == "label7")
                index = 1;
            else if (l.Name == "label8")
                index = 2;
            else if (l.Name == "label9")
                index = 3;
            else if (l.Name == "label10")
                index = 4;

            Double num = 0;
            if (stat < 48)
                num -= 1;
            else if (stat < 50)
                num -= .5;
            else if (stat < 52)
                num += .5;
            else
                num += 1;

            MatchupScores[index] = num;
            Double compound = checkForCompound(index);
            l.Text = MasteryScores[index] + MatchupScores[index] + compound + "";

            calculateTotalScore();
        }

        /*
         * This spreads the scores out a little more. I might not do this.         
         */
        private Double checkForCompound(int index)
        {
            Double mastery = 0;
            Double matchup = 0;

            try
            {
                if (index == 0)
                {
                    mastery = Convert.ToDouble(textBox11.Text);
                    matchup = Convert.ToDouble(textBox21.Text);
                }
                else if (index == 1)
                {
                    mastery = Convert.ToDouble(textBox12.Text);
                    matchup = Convert.ToDouble(textBox22.Text);
                }
                else if (index == 2)
                {
                    mastery = Convert.ToDouble(textBox13.Text);
                    matchup = Convert.ToDouble(textBox23.Text);
                }
                else if (index == 3)
                {
                    mastery = Convert.ToDouble(textBox14.Text);
                    matchup = Convert.ToDouble(textBox24.Text);
                }
                else if (index == 4)
                {
                    mastery = Convert.ToDouble(textBox15.Text);
                    matchup = Convert.ToDouble(textBox25.Text);
                }
            }
            catch(FormatException)
            {
                return 0;
            }


            Double compound = 0;
            if (mastery < 3 && matchup < 48)//Lane is looking really bad.
                compound -= 1;
            else if (mastery == 3 && matchup < 50)//Lane is looking bad.
                compound -= .5;
            else if (mastery == 4 && matchup < 45)//Bad
                compound -= .5;
            else if (mastery == 4 && matchup > 55)//Good
                compound += .5;
            else if (mastery >= 5 && matchup >= 50 && matchup <= 52)//Lane is looking good.
                compound += .5;
            else if (mastery > 5 && matchup > 52)//Lane is looking really good.
                compound += 1;

            Debug.WriteLine("mastery: " + mastery + " matchup: " + matchup);
            return compound;
        }

        private void printMasteryInfo(object sender, EventArgs e)
        {
            TextBox caller = sender as TextBox;



            HtmlNodeCollection tags = htmlDocument.DocumentNode.SelectNodes("//a");
            Boolean notContain = true;

            foreach (HtmlNode n in tags)
            {
                String name = n.InnerText;
                
                if (name.Contains(caller.Text))
                {
                    //caller.Text = name;
                    HtmlNodeCollection level = htmlDocument.DocumentNode.SelectNodes(".//td");
                    String l;
                    foreach(HtmlNode n2 in level)
                    {
                        if(n2.InnerText.Contains(name))
                        {
                            notContain = false;
                            l = n2.NextSibling.InnerText;

                            if (caller.Name == "textBox6")
                            {
                                textBox11.Text = l;
                                setMasteryScore(l, label6);
                            }
                            else if (caller.Name == "textBox7")
                            {
                                textBox12.Text = l;
                                setMasteryScore(l, label7);
                            }
                            else if (caller.Name == "textBox8")
                            {
                                textBox13.Text = l;
                                setMasteryScore(l, label8);
                            }
                            else if (caller.Name == "textBox9")
                            {
                                textBox14.Text = l;
                                setMasteryScore(l, label9);
                            }
                            else if (caller.Name == "textBox10")
                            {
                                textBox15.Text = l;
                                setMasteryScore(l, label10);
                            }
                            break;
                        }
                    }

                    break;

                }
            }

            if (notContain)
            {
                if (caller.Name == "textBox6")
                {
                    textBox11.Text = "0";
                    setMasteryScore("0", label6);
                }
                else if (caller.Name == "textBox7")
                {
                    textBox12.Text = "0";
                    setMasteryScore("0", label7);
                }
                else if (caller.Name == "textBox8")
                {
                    textBox13.Text = "0";
                    setMasteryScore("0", label8);
                }
                else if (caller.Name == "textBox9")
                {
                    textBox14.Text = "0";
                    setMasteryScore("0", label9);
                }
                else if (caller.Name == "textBox10")
                {
                    textBox15.Text = "0";
                    setMasteryScore("0", label10);
                }
            }
        }

        private void setMasteryScore(String score, Label l)
        {
            int index = 0;
            if (l.Name == "label6")
                index = 0;
            else if (l.Name == "label7")
                index = 1;
            else if (l.Name == "label8")
                index = 2;
            else if (l.Name == "label9")
                index = 3;
            else if (l.Name == "label10")
                index = 4;

            int s = Convert.ToInt32(score);
            Double num = 0;
            if (s <= 2)
                num -= 1;
            else if (s == 3)
                num -= .5;
            else if (s == 4)
                num += 0;
            else if (s == 5)
                num += .5;
            else if (s >= 6)
                num += 1;

            MasteryScores[index] = num;
            Double compound = checkForCompound(index);
            l.Text = MasteryScores[index] + MatchupScores[index] + compound + "";

            calculateTotalScore();
        }

        private void calculateTotalScore()
        {
            Double totalScore = Convert.ToDouble(label6.Text) + Convert.ToDouble(label7.Text) + Convert.ToDouble(label8.Text) + Convert.ToDouble(label9.Text) + Convert.ToDouble(label10.Text);
            label11.Text = totalScore + "";
        }

        private void getMasteryInfo(object sender, EventArgs e)
        {
            TextBox champName = sender as TextBox;
            champName.Text = "";
            String summonerName = "";
            int index = 0;
            if (champName.Name == "textBox6")
            {
                summonerName = TB[0].Text;
                textBox11.Text = "0";
                index = 0;
            }
            else if (champName.Name == "textBox7")
            {
                summonerName = TB[1].Text;
                textBox12.Text = "0";
                index = 1;
            }
            else if (champName.Name == "textBox8")
            {
                summonerName = TB[2].Text;
                textBox13.Text = "0";
                index = 2;
            }
            else if (champName.Name == "textBox9")
            {
                summonerName = TB[3].Text;
                textBox14.Text = "0";
                index = 3;
            }
            else if (champName.Name == "textBox10")
            {
                summonerName = TB[4].Text;
                textBox15.Text = "0";
                index = 4;
            }
            String htmlCode;
            using (WebClient client = new WebClient())
            {
                htmlCode = client.DownloadString("http://championmasterylookup.derpthemeus.com/summoner?summoner="+summonerName+"&region=NA");
            }
            MasteryData[index] = htmlCode;

            htmlDocument = new HtmlAgilityPack.HtmlDocument();
            htmlDocument.LoadHtml(htmlCode);
        }

        private void markForSwap(object sender, EventArgs e)
        {
            int count = 0;
            int prev = previousSelected;
            TextBox s = sender as TextBox;

            if (s.Text == "")
                return;

            foreach (TextBox tb in TB)
            {
                if (tb.BackColor == Color.Aqua && count != prev)
                    tb.BackColor = Color.White;

                if (tb == s)
                    previousSelected = count;

                count++;
            }

            
            s.BackColor = Color.Aqua;

        }

        private void button1_Click(object sender, EventArgs e)
        {
            String bulk = richTextBox1.Text;
            String[] lines = new String[5];
            int i = 0;

            foreach(char c in bulk)
            {
                if (c != '\n')
                {
                    lines[i] += c;
                }
                else
                {
                    i++;
                }
            }
            i = 0;

            try
            {
                foreach (String s in lines)
                {
                    lines[i] = Regex.Match(s, "^.*(?=( joined the lobby))").Value;
                    i++;
                }
            }
            catch (ArgumentNullException) { }

            textBox1.Text = lines[0];
            textBox2.Text = lines[1];
            textBox3.Text = lines[2];
            textBox4.Text = lines[3];
            textBox5.Text = lines[4];
        }

        private void button2_Click(object sender, EventArgs e)
        {
            TextBox tb1 = null;
            TextBox tb2 = null;

            String temp;
            foreach (TextBox tb in TB)
            {    
                if(tb.BackColor == Color.Aqua)
                {
                    if (tb1 == null)
                        tb1 = tb;
                    else
                        tb2 = tb;

                    tb.BackColor = Color.White;
                }
            }

            if(tb1 != null && tb2 != null)
            {
                temp = tb1.Text;
                tb1.Text = tb2.Text;
                tb2.Text = temp;
            }
        }
    }
}
