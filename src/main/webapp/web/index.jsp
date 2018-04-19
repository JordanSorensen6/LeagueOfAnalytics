<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>League Of Analytics</title>
    <link rel="stylesheet" href="/resources/css/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="/resources/css/styles.css"/>
    <link rel="stylesheet" href="/resources/css/layout.css"/>

    <script src="https://d3js.org/d3.v4.js"></script>
    <script src="/resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="/resources/javascript/bootstrap/bootstrap.min.js"></script>

    <script type="text/javascript" src="resources/javascript/home.js"></script>
    <script src="/resources/javascript/Chart.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>


<div class="gridLayout">
    <div class="teamSummoners">Team Summoners</div>
    <div class="teamChampions">Team Champions</div>
    <div class="championMastery">Mastery Level</div>
    <div class="opponentChampions">Opponent Champions</div>
    <div class="matchRating">Win Percentage</div>
    <%--<div class="score">Score</div>--%>

    <div class="top">Top</div>
    <div class="jungle">Jungle</div>
    <div class="mid">Mid</div>
    <div class="bot">Bot</div>
    <div class="support">Support</div>

    <%--<div class="summonerTop"><input type="text" name="summoners" id="summoner1" placeholder="summoner1" onclick="home.markForSwap('summoner1')"></div>--%>
    <%--<div class="summonerJungle"><input type="text" name="summoners" id="summoner2" placeholder="summoner2" onclick="home.markForSwap('summoner2')"></div>--%>
    <%--<div class="summonerMid"><input type="text" name="summoners" id="summoner3" placeholder="summoner3" onclick="home.markForSwap('summoner3')"></div>--%>
    <%--<div class="summonerBot"><input type="text" name="summoners" id="summoner4" placeholder="summoner4" onclick="home.markForSwap('summoner4')"></div>--%>
    <%--<div class="summonerSupport"><input type="text" name="summoners" id="summoner5" placeholder="summoner5" onclick="home.markForSwap('summoner5')"></div>--%>

    <div class="summonerTop"><div class="positionAndName"></div></div>
    <div class="summonerJungle"><input type="text" name="summoners" id="summoner2" placeholder="summoner2" onclick="home.markForSwap('summoner2')"></div>
    <div class="summonerMid"><input type="text" name="summoners" id="summoner3" placeholder="summoner3" onclick="home.markForSwap('summoner3')"></div>
    <div class="summonerBot"><input type="text" name="summoners" id="summoner4" placeholder="summoner4" onclick="home.markForSwap('summoner4')"></div>
    <div class="summonerSupport"><input type="text" name="summoners" id="summoner5" placeholder="summoner5" onclick="home.markForSwap('summoner5')"></div>

    <div class="teamChamp teamChampTop"><input type="text" id="champion1" placeholder="Top Champion"></div>
    <div class="teamChamp teamChampJungle"><input type="text" id="champion2" placeholder="Jungle Champion"></div>
    <div class="teamChamp teamChampMid"><input type="text" id="champion3" placeholder="Mid Champion"></div>
    <div class="teamChamp teamChampBot"><input type="text" id="champion4" placeholder="Bot Champion"></div>
    <div class="teamChamp teamChampSupport"><input type="text" id="champion5" placeholder="Support Champion"></div>

    <div class="teamChampTopPic"><img id="teamImg1" src="/resources/images/champion/placeholderTeam.png"></div>
    <div class="teamChampJunglePic"><img id="teamImg2" src="/resources/images/champion/placeholderTeam.png"></div>
    <div class="teamChampMidPic"><img id="teamImg3" src="/resources/images/champion/placeholderTeam.png"></div>
    <div class="teamChampBotPic"><img id="teamImg4" src="/resources/images/champion/placeholderTeam.png"></div>
    <div class="teamChampSupportPic"><img id="teamImg5" src="/resources/images/champion/placeholderTeam.png"></div>

    <%--<div class="teamChamp teamChampTop"><input type="text" id="champion1" placeholder="Top Champion"><img id="teamImg1" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="teamChamp teamChampJungle"><input type="text" id="champion2" placeholder="Jungle Champion"><img id="teamImg2" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="teamChamp teamChampMid"><input type="text" id="champion3" placeholder="Mid Champion"><img id="teamImg3" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="teamChamp teamChampBot"><input type="text" id="champion4" placeholder="Bot Champion"><img id="teamImg4" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="teamChamp teamChampSupport"><input type="text" id="champion5" placeholder="Support Champion"><img id="teamImg5" src="/resources/images/champion/placeholderTeam.png"></div>--%>

    <%--<div class="masteryTop"><input type="number" id="mastery1" placeholder="0" readonly=""></div>--%>
    <%--<div class="masteryJungle"><input type="number" id="mastery2" placeholder="0" readonly=""></div>--%>
    <%--<div class="masteryMid"><input type="number" id="mastery3" placeholder="0" readonly=""></div>--%>
    <%--<div class="masteryBot"><input type="number" id="mastery4" placeholder="0" readonly=""></div>--%>
    <%--<div class="masterySupport"><input type="number" id="mastery5" placeholder="0" readonly=""></div>--%>

    <div class="masteryTop"><img id="mastery1" src="/resources/images/L0.png"></div>
    <div class="masteryJungle"><img id="mastery2" src="/resources/images/L0.png"></div>
    <div class="masteryMid"><img id="mastery3" src="/resources/images/L0.png"></div>
    <div class="masteryBot"><img id="mastery4" src="/resources/images/L0.png"></div>
    <div class="masterySupport"><img id="mastery5" src="/resources/images/L0.png"></div>

    <%--<div class="oppChamp oppChampTop"><img id="oppImg1" src="/resources/images/champion/placeholderOpponent.png"><input type="text" id="opponent1" placeholder="Opponent Top"></div>--%>
    <%--<div class="oppChamp oppChampJungle"><img id="oppImg2" src="/resources/images/champion/placeholderOpponent.png"><input type="text" id="opponent2" placeholder="Opponent Jungle"></div>--%>
    <%--<div class="oppChamp oppChampMid"><img id="oppImg3" src="/resources/images/champion/placeholderOpponent.png"><input type="text" id="opponent3" placeholder="Opponent Mid"></div>--%>
    <%--<div class="oppChamp oppChampBot"><img id="oppImg4" src="/resources/images/champion/placeholderOpponent.png"><input type="text" id="opponent4" placeholder="Opponent Bot"></div>--%>
    <%--<div class="oppChamp oppChampSupport"><img id="oppImg5" src="/resources/images/champion/placeholderOpponent.png"><input type="text" id="opponent5" placeholder="Opponent Support"></div>--%>

    <div class="oppChampTopPic"><img id="oppImg1" src="/resources/images/champion/placeholderOpponent.png"></div>
    <div class="oppChampJunglePic"><img id="oppImg2" src="/resources/images/champion/placeholderOpponent.png"></div>
    <div class="oppChampMidPic"><img id="oppImg3" src="/resources/images/champion/placeholderOpponent.png"></div>
    <div class="oppChampBotPic"><img id="oppImg4" src="/resources/images/champion/placeholderOpponent.png"></div>
    <div class="oppChampSupportPic"><img id="oppImg5" src="/resources/images/champion/placeholderOpponent.png"></div>

    <div class="oppChamp oppChampTop"><input type="text" id="opponent1" placeholder="Opponent Top"></div>
    <div class="oppChamp oppChampJungle"><input type="text" id="opponent2" placeholder="Opponent Jungle"></div>
    <div class="oppChamp oppChampMid"><input type="text" id="opponent3" placeholder="Opponent Mid"></div>
    <div class="oppChamp oppChampBot"><input type="text" id="opponent4" placeholder="Opponent Bot"></div>
    <div class="oppChamp oppChampSupport"><input type="text" id="opponent5" placeholder="Opponent Support"></div>

    <div class="winPerTop"><input type="text" id="percentage1" placeholder="00.00%" readonly=""></div>
    <div class="winPerJungle"><input type="text" id="percentage2" placeholder="00.00%" readonly=""></div>
    <div class="winPerMid"><input type="text" id="percentage3" placeholder="00.00%" readonly=""></div>
    <div class="winPerBot"><input type="text" id="percentage4" placeholder="00.00%" readonly=""></div>
    <div class="winPerSupport"><input type="text" id="percentage5" placeholder="00.00%" readonly=""></div>

    <div class="scoreTop" id="score1">0</div>
    <div class="scoreJungle" id="score2">0</div>
    <div class="scoreMid" id="score3">0</div>
    <div class="scoreBot" id="score4">0</div>
    <div class="scoreSupport" id="score5">0</div>
</div><br><br>

    <h1 class="totalScore" id="totalScore">Your Score: 0</h1><hr>
    <p class="userMessage" id="userMessage">Based on (number) games, players with this score win (some percentage) of their games.</p>

<textarea id="textBox" cols="50" rows="5" placeholder="summoner1 joined the lobby
summoner2 joined the lobby
summoner3 joined the lobby
summoner4 joined the lobby
summoner5 joined the lobby" id="textBox" oninput="home.populateSummonerNames()"></textarea><br>
<%--<button type="button" id="populate" name="populateButton" onclick="home.populateSummonerNames()">Populate</button>--%>
<%--<button type="button" name="swapButton" onclick="home.swapRoles()">Swap Position</button>--%>

<div>
    <svg width="500" height="400" id="plotChart">
        <g id="xAxis"></g>
        <g id="yAxis"></g>
        <g id="plot"></g>
    </svg>
</div>





<script>
    home.init();

    d3.json("resources/data/fake_data.json", function (error, data) {
        data.forEach(function (d) {
            d.g = +d.game;
            d.s = +d.score;
        });
        let chart = new Chart(data);
        chart.updateChart();
    });
</script>





</body>
</html>
