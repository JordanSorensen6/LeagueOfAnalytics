<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>League Of Analytics</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-theme.css"/>
    <link rel="stylesheet" href="resources/css/layout.css"/>
    <link rel="stylesheet" href="resources/css/champSelectModal.css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">

    <script src="resources/javascript/popper.min.js"></script>
    <script src="resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>
    <script src="https://d3js.org/d3.v4.js"></script>

    <script type="text/javascript" src="resources/javascript/championSelectModal.js"></script>
    <script type="text/javascript" src="resources/javascript/roleBestFit.js"></script>
    <script type="text/javascript" src="resources/javascript/home.js"></script>
    <script type="text/javascript" src="resources/javascript/cookie.js"></script>
</head>
<body onload="checkCookies()" onbeforeunload="setCookies()">

<jsp:include page="navbar.jsp"/>
<div>
    <span id="exit" class="tutorialExit" style="font-size: 2em; display: none" onclick="exitTutorial()">
        <i class="fas fa-times">

        </i>
    </span>
</div>

<div class="container">

    <div id="loader" style="visibility: hidden"></div>
    <div class="row">
        <div class="col-sm-3 my-auto">
            <u>Team Summoners</u>
        </div>

        <div class="col-sm-1 my-auto">
            <u>Team Champions</u>
        </div>

        <div class="col-sm my-auto">
            <u>Rank</u>
        </div>

        <div class="col-sm my-auto">
            <u>Champion Mastery</u>
        </div>

        <div class="col-sm my-auto">
            <u>Hot Streak</u>
        </div>

        <div class="col-sm my-auto">
            <u>Player Win Percentage</u>
        </div>

        <div class="col-sm my-auto">
            <u>Champion Win Percentage</u>
        </div>

        <div class="col-sm my-auto">
            <u>Opponent Champions</u>
        </div>

        <div class="col-sm my-auto">
            <u>Lane Score</u>
        </div>
    </div>



    <div class="row bk1">


        <div class="col-sm-3 my-auto">
            <div class="summoner summonerTop"><div class="positionAndName"><div class="position">Top</div><div class="name"><span class="tooltiptext">This column is where you input summoner names.  There is drag and drop functionality if the summoner is in the wrong position.</span><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner1" placeholder="summoner1" oninput="displayButton('1')"><button id="button1" onclick="home.summonerIdLookup('1')">Search</button></div></div></div>
        </div>

        <div class="col-sm-1 my-auto">
            <div class="champPic teamChampTopPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg1" src="/resources/images/champion/placeholderTeam.png"><span class="tooltiptext">Click the icons to select each position's champion.  If the champion is in the wrong position utilize the drag and drop functionality.</span></div>
        </div>

        <div class="col-sm my-auto">
            <div class="tier"><span class="tooltiptext">This represents the summoner's tier</span><img id="tier1" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="mastery"><img id="mastery1" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="hotStreak"><img class="smallImg" id="hot1" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="playerWinPerTop" id="playerPercentage1"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="winPerTop" id="percentage1"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="champPic oppChampTopPic"><img class="champImg oppChampImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg1" src="/resources/images/champion/placeholderOpponent.png"><span class="tooltiptext">Click the icons to select each opponent's champion.  If the champion is in the wrong position utilize the drag and drop functionality.</span></div>
        </div>

        <div class="col-sm my-auto">
            <div class="scoreTop" id="score1"><span class="tooltiptext">This score represents your chances of winning</span>0</div>
        </div>

    </div>

    <div class="row bk2">

        <div class="col-sm-3 my-auto">
            <div class="summoner summonerJungle"><div class="positionAndName"><div class="position">Jungle</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner2" placeholder="summoner2" oninput="displayButton('2')"><button id="button2" onclick="home.summonerIdLookup('2')">Search</button></div></div></div>
        </div>

        <div class="col-sm-1">
            <div class="champPic teamChampJunglePic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg2" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="tier"><img id="tier2" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="mastery"><img id="mastery2" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="hotStreak"><img class="smallImg" id="hot2" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="playerWinPerJungle" id="playerPercentage2"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="winPerJungle" id="percentage2"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="champPic oppChampJunglePic"><img class="champImg oppChampImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg2" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="scoreJungle" id="score2">0</div>
        </div>

    </div>

    <div class="row bk1">

        <div class="col-sm-3 my-auto">
            <div class="summoner summonerMid"><div class="positionAndName"><div class="position">Mid</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner3" placeholder="summoner3" oninput="displayButton('3')"><button id="button3" onclick="home.summonerIdLookup('3')">Search</button></div></div></div>
        </div>

        <div class="col-sm-1 my-auto">
            <div class="champPic teamChampMidPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg3" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="tier"><img id="tier3" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="mastery"><img id="mastery3" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="hotStreak"><img class="smallImg" id="hot3" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="playerWinPerMid" id="playerPercentage3"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="winPerJungle" id="percentage3"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="champPic oppChampMidPic"><img class="champImg oppChampImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg3" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="scoreMid" id="score3">0</div>
        </div>

    </div>

    <div class="row bk2">

        <div class="col-sm-3 my-auto">
            <div class="summoner summonerBot"><div class="positionAndName"><div class="position">Bot</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner4" placeholder="summoner4" oninput="displayButton('4')"><button id="button4" onclick="home.summonerIdLookup('4')">Search</button></div></div></div>
        </div>

        <div class="col-sm-1 my-auto">
            <div class="champPic teamChampBotPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg4" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="tier"><img id="tier4" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="mastery"><img id="mastery4" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="hotStreak"><img class="smallImg" id="hot4" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="playerWinPerBot" id="playerPercentage4"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="winPerBot" id="percentage4"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="champPic oppChampBotPic"><img class="champImg oppChampImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg4" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="scoreBot" id="score4">0</div>
        </div>

    </div>

    <div class="row bk1">

        <div class="col-sm-3 my-auto">
            <div class="summoner summonerSupport"><div class="positionAndName"><div class="position">Support</div><div class="name"><input type="text" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="summoners" id="summoner5" placeholder="summoner5" oninput="displayButton('5')"><button id="button5" onclick="home.summonerIdLookup('5')">Search</button></div></div></div>
        </div>

        <div class="col-sm-1 my-auto">
            <div class="champPic teamChampSupportPic"><img class="champImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="teamChamps" id="teamImg5" src="/resources/images/champion/placeholderTeam.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="tier"><img id="tier5" src="/resources/images/tier-icons/provisional.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="mastery"><img id="mastery5" src="/resources/images/L0.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="hotStreak"><img class="smallImg" id="hot5" src="/resources/images/hotStreakFalse.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="playerWinPerSupport" id="playerPercentage5"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="winPerSupport" id="percentage5"><b>00.00%</b></div>
        </div>

        <div class="col-sm my-auto">
            <div class="champPic oppChampSupportPic"><img class="champImg oppChampImg" ondragstart="onDragStart(event)" ondrop="onDrop(event)" ondragover="onDragOver(event)" draggable="true" name="oppChamps" id="oppImg5" src="/resources/images/champion/placeholderOpponent.png"></div>
        </div>

        <div class="col-sm my-auto">
            <div class="scoreSupport" id="score5">0</div>
        </div>

    </div>
    <div class="row">
        <div class="offset-9 roleBestFit">
            <a class="btn btn-primary" id="roleBestFitBtn">
                <i class="fas fa-sort-amount-up">Best</i>
            </a>
        </div>
    </div>
</div>

<br><br>

<div class="scoreDiv" style="display: inline-block"> <%--Here--%>
    <span class="tooltiptext">Your score will show up here.  The higher your score the better chance you have of winning</span>
    <h1 class="totalScore" id="totalScore">Your Score: 0</h1>
    <p class="userMessage" id="userMessage"><b>Based on (number) games, players with this score win (some percentage) of their games.</b></p>
</div>


<div class="textBox" style="display: inline-block">
    <span class="tooltiptext">You can copy and paste the summoners from the champion select page and paste here.  The summoner names will auto-fill.</span>
    <textarea id="textBox" cols="50" rows="5" placeholder="summoner1 joined the lobby
summoner2 joined the lobby
summoner3 joined the lobby
summoner4 joined the lobby
summoner5 joined the lobby" id="textBox" oninput="clearAll(); home.populateSummonerNames()"></textarea>
</div>

<div id="arrows" style="display: none">
    <span class="arrowLeft" style="font-size: 3em" onclick="goBackTutorial()">
        <i class="fas fa-arrow-alt-circle-left"></i>
    </span>
    <span class="arrowRight" style="font-size: 3em" onclick="continueTutorial()">
        <i class="fas fa-arrow-alt-circle-right"></i>
    </span>
</div>

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
                                <a href="#" data-filter="counters">Counter Picks</a>
                                <input id="championSearch" type="text" placeholder="Search Champions...">
                            </div>
                        </div>
                        <div id="championImages" class="champion-container pre-scrollable"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="selectChampBtn" type="button" class="btn btn-primary">Select</button>
                <button id="closeModalBtn" type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script>
    home.init();
</script>





</body>
</html>