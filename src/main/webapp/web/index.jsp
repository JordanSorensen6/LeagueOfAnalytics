<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>League Of Analytics</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/resources/css/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="/resources/css/layout.css"/>
    <link rel="stylesheet" href="/resources/css/champSelectModal.css"/>

    <script src="/resources/javascript/popper.min.js"></script>
    <script src="/resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="/resources/javascript/bootstrap/bootstrap.min.js"></script>
    <script src="https://d3js.org/d3.v4.js"></script>

    <script type="text/javascript" src="resources/javascript/championSelectModal.js"></script>
    <script type="text/javascript" src="resources/javascript/home.js"></script>
    <script type="text/javascript" src="resources/javascript/cookie.js"></script>
</head>
<body onload="checkCookies()" onbeforeunload="setCookies()">

<jsp:include page="navbar.jsp"/>

<div class="container">

    <div class="row">
        <div class="col-sm-2">
            <u>Team Summoners</u>
        </div>

        <div class="col-sm-2">
            <u>Team Champions</u>
        </div>

        <div class="col-sm">
            <u>Rank</u>
        </div>

        <div class="col-sm">
            <u>Champion Mastery</u>
        </div>

        <div class="col-sm">
            <u>Hot Streak</u>
        </div>

        <div class="col-sm">
            <u>Player Win Percentage</u>
        </div>

        <div class="col-sm">
            <u>Champion Win Percentage</u>
        </div>

        <div class="col-sm">
            <u>Opponent Champions</u>
        </div>

        <div class="col-sm">
            <u>Lane Score</u>
        </div>
    </div>

    <div class="row bk1">

        <div class="col-sm">
            <div class="summonerTop"><div class="positionAndName"><div class="position">Top</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner1" placeholder="summoner1" oninput="displayButton('1')"><button id="button1" onclick="home.summonerIdLookup('1')">Search</button></div></div></div>
        </div>

        <div class="col-sm">
            <div class="champPic teamChampTopPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg1" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm">
            <div class="tier"><img id="tier1" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm">
            <div class="mastery"><img id="mastery1" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm">
            <div class="hotStreak"><img id="hot1" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm">
            <div class="playerWinPerTop" id="playerPercentage1"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="winPerTop" id="percentage1"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="champPic oppChampTopPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg1" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm">
            <div class="scoreTop" id="score1">0</div>
        </div>

    </div>

    <div class="row bk2">

        <div class="col-sm">
            <div class="summonerJungle"><div class="positionAndName"><div class="position">Jungle</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner2" placeholder="summoner2" oninput="displayButton('2')"><button id="button2" onclick="home.summonerIdLookup('2')">Search</button></div></div></div>
        </div>

        <div class="col-sm">
            <div class="champPic teamChampJunglePic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg2" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm">
            <div class="tier"><img id="tier2" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm">
            <div class="mastery"><img id="mastery2" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm">
            <div class="hotStreak"><img id="hot2" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm">
            <div class="playerWinPerJungle" id="playerPercentage2"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="winPerJungle" id="percentage2"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="champPic oppChampJunglePic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg2" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm">
            <div class="scoreJungle" id="score2">0</div>
        </div>

    </div>

    <div class="row bk1">

        <div class="col-sm">
            <div class="summonerMid"><div class="positionAndName"><div class="position">Mid</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner3" placeholder="summoner3" oninput="displayButton('3')"><button id="button3" onclick="home.summonerIdLookup('3')">Search</button></div></div></div>
        </div>

        <div class="col-sm">
            <div class="champPic teamChampMidPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg3" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm">
            <div class="tier"><img id="tier3" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm">
            <div class="mastery"><img id="mastery3" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm">
            <div class="hotStreak"><img id="hot3" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm">
            <div class="playerWinPerMid" id="playerPercentage3"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="winPerJungle" id="percentage3"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="champPic oppChampMidPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg3" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm">
            <div class="scoreMid" id="score3">0</div>
        </div>

    </div>

    <div class="row bk2">

        <div class="col-sm">
            <div class="summonerBot"><div class="positionAndName"><div class="position">Bot</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner4" placeholder="summoner4" oninput="displayButton('4')"><button id="button4" onclick="home.summonerIdLookup('4')">Search</button></div></div></div>
        </div>

        <div class="col-sm">
            <div class="champPic teamChampBotPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg4" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm">
            <div class="tier"><img id="tier4" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm">
            <div class="mastery"><img id="mastery4" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm">
            <div class="hotStreak"><img id="hot4" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm">
            <div class="playerWinPerBot" id="playerPercentage4"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="winPerBot" id="percentage4"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="champPic oppChampBotPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg4" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm">
            <div class="scoreBot" id="score4">0</div>
        </div>

    </div>

    <div class="row bk1">

        <div class="col-sm">
            <div class="summonerSupport"><div class="positionAndName"><div class="position">Support</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner5" placeholder="summoner5" oninput="displayButton('5')"><button id="button5" onclick="home.summonerIdLookup('5')">Search</button></div></div></div>
        </div>

        <div class="col-sm">
            <div class="champPic teamChampSupportPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg5" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm">
            <div class="tier"><img id="tier5" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm">
            <div class="mastery"><img id="mastery5" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm">
            <div class="hotStreak"><img id="hot5" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm">
            <div class="playerWinPerSupport" id="playerPercentage5"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="winPerSupport" id="percentage5"><b>00.00%</b></div>
        </div>

        <div class="col-sm">
            <div class="champPic oppChampSupportPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg5" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm">
            <div class="scoreSupport" id="score5">0</div>
        </div>

    </div>
</div>

<%--<div class="gridLayout">--%>
    <%--<div class="teamSummoners"><u>Team Summoners</u></div>--%>
    <%--<div class="teamChampions"><u>Team Champions</u></div>--%>
    <%--<div class="playerInfo"><u>Player Info</u></div>--%>
    <%--&lt;%&ndash;<div class="championMastery"><u>Mastery Level</u></div>&ndash;%&gt;--%>
    <%--&lt;%&ndash;<div class="hotStreak"><u>Hot Streak</u></div>&ndash;%&gt;--%>
    <%--<div class="playerWinPer"><u>Player Win Percentage</u></div>--%>
    <%--<div class="opponentChampions"><u>Opponent Champions</u></div>--%>
    <%--<div class="matchRating"><u>Champion Win Percentage</u></div>--%>

    <%--&lt;%&ndash;<div class="box">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="summonerTop"><div class="positionAndName"><div class="position">Top</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner1" placeholder="summoner1" oninput="displayButton('1')"><button id="button1" onclick="home.summonerIdLookup('1')">Search</button></div></div></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="teamChamp teamChampTop"><input type="text" ondrop="return false;" id="champion1" placeholder="Top Champion"></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="champPic teamChampTopPic"><img ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg1" src="/resources/images/champion/placeholderTeam.png"></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="tierTop"><img id="tier1" src="/resources/images/tier-icons/provisional.png"></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="masteryTop"><img id="mastery1" src="/resources/images/L0.png"></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="hotStreakTop"><img id="hot1" src="/resources/images/hotStreakFalse.png"></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="playerWinPerTop" id="playerPercentage1"><b>00.00%</b></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="champPic oppChampTopPic"><img ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg1" src="/resources/images/champion/placeholderOpponent.png"></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="oppChamp oppChampTop"><div class="positionAndName"><div class="position">Top</div><div class="name"><input type="text" ondrop="return false;" id="opponent1" placeholder="Opponent Top"></div></div></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="winPerTop" id="percentage1"><b>00.00%</b></div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="scoreTop" id="score1">0</div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<hr>&ndash;%&gt;--%>
    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>


    <%--<div class="summonerTop"><div class="positionAndName"><div class="position">Top</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner1" placeholder="summoner1" oninput="displayButton('1')"><button id="button1" onclick="home.summonerIdLookup('1')">Search</button></div></div></div>--%>
    <%--<div class="summonerJungle"><div class="positionAndName"><div class="position">Jungle</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner2" placeholder="summoner2" oninput="displayButton('2')"><button id="button2" onclick="home.summonerIdLookup('2')">Search</button></div></div></div>--%>
    <%--<div class="summonerMid"><div class="positionAndName"><div class="position">Mid</div><div class="name"><input type="text" draggable="true" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" name="summoners" id="summoner3" placeholder="summoner3" oninput="displayButton('3')"><button id="button3" onclick="home.summonerIdLookup('3')">Search</button></div></div></div>--%>
    <%--<div class="summonerBot"><div class="positionAndName"><div class="position">Bot</div><div class="name"><input type="text" draggable="true" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" name="summoners" id="summoner4" placeholder="summoner4" oninput="displayButton('4')"><button id="button4" onclick="home.summonerIdLookup('4')">Search</button></div></div></div>--%>
    <%--<div class="summonerSupport"><div class="positionAndName"><div class="position">Support</div><div class="name"><input type="text" draggable="true" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" name="summoners" id="summoner5" placeholder="summoner5" oninput="displayButton('5')"><button id="button5" onclick="home.summonerIdLookup('5')">Search</button></div></div></div>--%>

    <%--<div class="teamChamp teamChampTop"><input type="text" ondrop="return false;" id="champion1" placeholder="Top Champion"></div>--%>
    <%--<div class="teamChamp teamChampJungle"><input type="text" ondrop="return false;" id="champion2" placeholder="Jungle Champion"></div>--%>
    <%--<div class="teamChamp teamChampMid"><input type="text" ondrop="return false;" id="champion3" placeholder="Mid Champion"></div>--%>
    <%--<div class="teamChamp teamChampBot"><input type="text" ondrop="return false;" id="champion4" placeholder="Bot Champion"></div>--%>
    <%--<div class="teamChamp teamChampSupport"><input type="text" ondrop="return false;" id="champion5" placeholder="Support Champion"></div>--%>

    <%--<div class="champPic teamChampTopPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg1" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="champPic teamChampJunglePic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg2" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="champPic teamChampMidPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg3" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="champPic teamChampBotPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg4" src="/resources/images/champion/placeholderTeam.png"></div>--%>
    <%--<div class="champPic teamChampSupportPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg5" src="/resources/images/champion/placeholderTeam.png"></div>--%>


    <%--<div class="dataTop">--%>
        <%--<div class="playerData">--%>
            <%--<div class="tier"><img id="tier1" src="/resources/images/tier-icons/provisional.png"></div>--%>
            <%--<div class="mastery"><img id="mastery1" src="/resources/images/L0.png"></div>--%>
            <%--<div class="hotStreak"><img id="hot1" src="/resources/images/hotStreakFalse.png"></div>--%>
        <%--</div>--%>
    <%--</div>--%>

    <%--<div class="dataJungle">--%>
        <%--<div class="playerData">--%>
            <%--<div class="tier"><img id="tier2" src="/resources/images/tier-icons/provisional.png"></div>--%>
            <%--<div class="mastery"><img id="mastery2" src="/resources/images/L0.png"></div>--%>
            <%--<div class="hotStreak"><img id="hot2" src="/resources/images/hotStreakFalse.png"></div>--%>
        <%--</div>--%>
    <%--</div>--%>

    <%--<div class="dataMid">--%>
        <%--<div class="playerData">--%>
            <%--<div class="tier"><img id="tier3" src="/resources/images/tier-icons/provisional.png"></div>--%>
            <%--<div class="mastery"><img id="mastery3" src="/resources/images/L0.png"></div>--%>
            <%--<div class="hotStreak"><img id="hot3" src="/resources/images/hotStreakFalse.png"></div>--%>
        <%--</div>--%>
    <%--</div>--%>

    <%--<div class="dataBot">--%>
        <%--<div class="playerData">--%>
            <%--<div class="tier"><img id="tier4" src="/resources/images/tier-icons/provisional.png"></div>--%>
            <%--<div class="mastery"><img id="mastery4" src="/resources/images/L0.png"></div>--%>
            <%--<div class="hotStreak"><img id="hot4" src="/resources/images/hotStreakFalse.png"></div>--%>
        <%--</div>--%>
    <%--</div>--%>

    <%--<div class="dataSupport">--%>
        <%--<div class="playerData">--%>
            <%--<div class="tier"><img id="tier5" src="/resources/images/tier-icons/provisional.png"></div>--%>
            <%--<div class="mastery"><img id="mastery5" src="/resources/images/L0.png"></div>--%>
            <%--<div class="hotStreak"><img id="hot5" src="/resources/images/hotStreakFalse.png"></div>--%>
        <%--</div>--%>
    <%--</div>--%>

    <%--<div class="playerWinPerTop" id="playerPercentage1"><b>00.00%</b></div>--%>
    <%--<div class="playerWinPerJungle" id="playerPercentage2"><b>00.00%</b></div>--%>
    <%--<div class="playerWinPerMid" id="playerPercentage3"><b>00.00%</b></div>--%>
    <%--<div class="playerWinPerBot" id="playerPercentage4"><b>00.00%</b></div>--%>
    <%--<div class="playerWinPerSupport" id="playerPercentage5"><b>00.00%</b></div>--%>

    <%--<div class="champPic oppChampTopPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg1" src="/resources/images/champion/placeholderOpponent.png"></div>--%>
    <%--<div class="champPic oppChampJunglePic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg2" src="/resources/images/champion/placeholderOpponent.png"></div>--%>
    <%--<div class="champPic oppChampMidPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg3" src="/resources/images/champion/placeholderOpponent.png"></div>--%>
    <%--<div class="champPic oppChampBotPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg4" src="/resources/images/champion/placeholderOpponent.png"></div>--%>
    <%--<div class="champPic oppChampSupportPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg5" src="/resources/images/champion/placeholderOpponent.png"></div>--%>

    <%--<div class="oppChamp oppChampTop"><div class="positionAndName"><div class="position">Top</div><div class="name"><input type="text" ondrop="return false;" id="opponent1" placeholder="Opponent Top"></div></div></div>--%>
    <%--<div class="oppChamp oppChampJungle"><div class="positionAndName"><div class="position">Jungle</div><div class="name"><input type="text" ondrop="return false;" id="opponent2" placeholder="Opponent Jungle"></div></div></div>--%>
    <%--<div class="oppChamp oppChampMid"><div class="positionAndName"><div class="position">Mid</div><div class="name"><input type="text" ondrop="return false;" id="opponent3" placeholder="Opponent Mid"></div></div></div>--%>
    <%--<div class="oppChamp oppChampBot"><div class="positionAndName"><div class="position">Bot</div><div class="name"><input type="text" ondrop="return false;" id="opponent4" placeholder="Opponent Bot"></div></div></div>--%>
    <%--<div class="oppChamp oppChampSupport"><div class="positionAndName"><div class="position">Support</div><div class="name"><input type="text" ondrop="return false;" id="opponent5" placeholder="Opponent Support"></div></div></div>--%>

    <%--<div class="winPerTop" id="percentage1"><b>00.00%</b></div>--%>
    <%--<div class="winPerJungle" id="percentage2"><b>00.00%</b></div>--%>
    <%--<div class="winPerMid" id="percentage3"><b>00.00%</b></div>--%>
    <%--<div class="winPerBot" id="percentage4"><b>00.00%</b></div>--%>
    <%--<div class="winPerSupport" id="percentage5"><b>00.00%</b></div>--%>

    <%--<div class="scoreTop" id="score1">0</div>--%>
    <%--<div class="scoreJungle" id="score2">0</div>--%>
    <%--<div class="scoreMid" id="score3">0</div>--%>
    <%--<div class="scoreBot" id="score4">0</div>--%>
    <%--<div class="scoreSupport" id="score5">0</div>--%>
<%--</div>--%>
<br><br>

    <h1 class="totalScore" id="totalScore">Your Score: 0</h1>
<p class="userMessage" id="userMessage"><b>Based on (number) games, players with this score win (some percentage) of their games.</b></p>




<textarea id="textBox" cols="50" rows="5" placeholder="summoner1 joined the lobby
summoner2 joined the lobby
summoner3 joined the lobby
summoner4 joined the lobby
summoner5 joined the lobby" id="textBox" oninput="clearAll(); home.populateSummonerNames()"></textarea>

<div class="help-tip" onclick="startTutorial()">
    <p>Click here to start a tutorial for the web site</p>
</div><br>

<div class="modal" id="championSelectModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content champ-modal">
            <div class="modal-header">
                <h5 class="modal-title">Select a Champion</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="filter-options clearfix">
                                <a href="#" data-filter="*" class="current-filter">All</a>
                                <a href="#" data-filter="fighter">Fighter</a>
                                <a href="#" data-filter="tank">Tank</a>
                                <a href="#" data-filter="mage">Mage</a>
                                <a href="#" data-filter="assassin">Assassin</a>
                                <a href="#" data-filter="marksman">Marksman</a>
                                <a href="#" data-filter="support">Support</a>
                                <input id="championSearch" type="text" placeholder="Search Champions...">
                            </div>
                        </div>
                        <div id="championImages" class="champion-container pre-scrollable"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="selectChampBtn" type="button" class="btn btn-primary">Select</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script>
    home.init();
</script>





</body>
</html>