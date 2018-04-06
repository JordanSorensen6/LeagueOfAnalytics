<html><head>
    <script type="text/javascript" src="./resources/javascript/test.js"></script>
    <link rel="stylesheet" href="layout.css">
</head>

<body><p>Hello World!</p>
<p>test variable</p>


<div class="gridLayout">
    <div class="teamSummoners">Team Summoners</div>
    <div class="teamChampions">Team Champions</div>
    <div class="championMastery">Mastery Level</div>
    <div class="opponentChampions">Opponent Champions</div>
    <div class="matchRating">Win Percentage</div>

    <div class="top">Top</div>
    <div class="jungle">Jungle</div>
    <div class="mid">Mid</div>
    <div class="bot">Bot</div>
    <div class="support">Support</div>

    <div class="summonerTop"><input type="text" id="summoner1" placeholder="summoner1" onclick="markForSwap('summoner1')"></div>
    <div class="summonerJungle"><input type="text" id="summoner2" placeholder="summoner2" onclick="markForSwap('summoner2')"></div>
    <div class="summonerMid"><input type="text" id="summoner3" placeholder="summoner3" onclick="markForSwap('summoner3')"></div>
    <div class="summonerBot"><input type="text" id="summoner4" placeholder="summoner4" onclick="markForSwap('summoner4')"></div>
    <div class="summonerSupport"><input type="text" id="summoner5" placeholder="summoner5" onclick="markForSwap('summoner5')"></div>

    <div class="teamChampTop"><input type="text" id="champion1" placeholder="Top Champion"></div>
    <div class="teamChampJungle"><input type="text" id="champion2" placeholder="Jungle Champion"></div>
    <div class="teamChampMid"><input type="text" id="champion3" placeholder="Mid Champion"></div>
    <div class="teamChampBot"><input type="text" id="champion4" placeholder="Bot Champion"></div>
    <div class="teamChampSupport"><input type="text" id="champion5" placeholder="Support Champion"></div>

    <div class="masteryTop"><input type="number" id="mastery1" placeholder="0" readonly=""></div>
    <div class="masteryJungle"><input type="number" id="mastery2" placeholder="0" readonly=""></div>
    <div class="masteryMid"><input type="number" id="mastery3" placeholder="0" readonly=""></div>
    <div class="masteryBot"><input type="number" id="mastery4" placeholder="0" readonly=""></div>
    <div class="masterySupport"><input type="number" id="mastery5" placeholder="0" readonly=""></div>

    <div class="oppChampTop"><input type="text" id="opponent1" placeholder="Opponent Top"></div>
    <div class="oppChampJungle"><input type="text" id="opponent2" placeholder="Opponent Jungle"></div>
    <div class="oppChampMid"><input type="text" id="opponent3" placeholder="Opponent Mid"></div>
    <div class="oppChampBot"><input type="text" id="opponent4" placeholder="Opponent Bot"></div>
    <div class="oppChampSupport"><input type="text" id="opponent5" placeholder="Opponent Support"></div>

    <div class="winPerTop"><input type="text" id="percentage1" placeholder="00.00%" readonly=""></div>
    <div class="winPerJungle"><input type="text" id="percentage2" placeholder="00.00%" readonly=""></div>
    <div class="winPerMid"><input type="text" id="percentage3" placeholder="00.00%" readonly=""></div>
    <div class="winPerBot"><input type="text" id="percentage4" placeholder="00.00%" readonly=""></div>
    <div class="winPerSupport"><input type="text" id="percentage5" placeholder="00.00%" readonly=""></div>
</div><br><br>

<textarea id="textBox" cols="50" rows="5" placeholder="summoner1 joined the lobby
summoner2 joined the lobby
summoner3 joined the lobby
summoner4 joined the lobby
summoner5 joined the lobby"></textarea><br>
<button type="button" name="populateButton" onclick="populateSummonerNames()">Populate</button>
<button type="button" name="swapButton" onclick="">Swap Position</button>





<script>
    function populateSummonerNames() {
        var lines = document.getElementById("textBox").value.split('\n');
        for(var i = 0;i < lines.length;i++){
            lines[i] = lines[i].replace(" joined the lobby", "");
        }
        document.getElementById("summoner1").value = lines[0];
        document.getElementById("summoner2").value = lines[1];
        document.getElementById("summoner3").value = lines[2];
        document.getElementById("summoner4").value = lines[3];
        document.getElementById("summoner5").value = lines[4];
    }

    function markForSwap(id)
    {
        var box  = document.getElementById(id);
        box.style.backgroundColor = "yellow";
    }
</script>





</body></html>