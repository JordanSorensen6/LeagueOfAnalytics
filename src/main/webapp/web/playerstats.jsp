<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Stats - ${username}</title>

    <link rel="stylesheet" href="/resources/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/resources/bootstrap/css/bootstrap-theme.css"/>
    <link rel="stylesheet" href="/resources/css/layout.css"/>
    <link rel="stylesheet" href="/resources/css/styles.css"/>
    <link rel="stylesheet" href="/resources/css/temp.css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">

    <script src="https://d3js.org/d3.v4.js"></script>
    <script src="/resources/javascript/d3-tip.js"></script>
    <script src="/resources/javascript/popper.min.js"></script>
    <script src="/resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="/resources/bootstrap/js/bootstrap.min.js"></script>

    <script src="/resources/javascript/PlotChart.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>

<div>
    <span id="exit" class="tutorialExit" style="font-size: 2em; display: none" onclick="chart.exitTutorial()">
        <i class="fas fa-times">

        </i>
    </span>
</div>

${username} stats


<div class="row">
    <div class="chart col-sm-8">
        <a class="btn btn-primary moreButton" type="button" onclick="moreGames()" id = "add"><span class="tooltiptext">This button is for adding games to the graph</span>Add More Games</a>
        <a class="btn btn-primary deleteButton" type="button" onclick="chart.lessGames()" id = "delete"><span class="tooltiptext">This button is for deleting games from the graph</span>Take Away Games</a>
        <h4>Assigned Scores For Past Games</h4>
        <div class = "row">
            <div class="graph">
                <span class="tooltiptext">The Graph</span>
                <div id="loader" style="visibility: hidden"></div>
                <svg width="800" height="600" id="plotChart">
                    <g id="xAxis"></g>
                    <g id="yAxis"></g>
                    <g id="plot"></g>
                </svg>
            </div>
            <div class="legend">
                <span class="tooltiptext">A legend for the graph</span>
                <h4>Legend</h4>
                <svg width="200" height="400" id="legend">

                </svg>
            </div>
        </div>

    </div>
    <div class="col-sm-4">
        <img class="champ-background-sm" src="/resources/images/backgrounds/zoe-transparent.png">
    </div>
</div>

<div id="arrows" style="display: none">
    <span class="arrowLeft" style="font-size: 3em" onclick="chart.goBackTutorial()">
        <i class="fas fa-arrow-alt-circle-left"></i>
    </span>
    <span class="arrowRight" style="font-size: 3em" onclick="chart.continueTutorial()">
        <i class="fas fa-arrow-alt-circle-right"></i>
    </span>
</div>

<div id="gameStats" style="display: none">
    Blue Team
    <div id="gameStats100">
        <div class="container">
            <div class="row">
                <div class="col-sm"><u>Summoner</u></div>
                <div class="col-sm"><u>KDA</u></div>
                <div class="col-sm"><u>Damage</u></div>
                <div class="col-sm"><u>Previous Season's Tier</u></div>
            </div>

            <div class="row bk1">
                <div class="col-sm"><div class="summoner1"></div></div>
                <div class="col-sm"><div class="kda1"></div></div>
                <div class="col-sm"><div class="damage1"></div></div>
                <div class="col-sm"><div class="tier1"></div></div>
            </div>

            <div class="row bk2">
                <div class="col-sm"><div class="summoner2"></div></div>
                <div class="col-sm"><div class="kda2"></div></div>
                <div class="col-sm"><div class="damage2"></div></div>
                <div class="col-sm"><div class="tier2"></div></div>
            </div>

            <div class="row bk1">
                <div class="col-sm"><div class="summoner3"></div></div>
                <div class="col-sm"><div class="kda3"></div></div>
                <div class="col-sm"><div class="damage3"></div></div>
                <div class="col-sm"><div class="tier3"></div></div>
            </div>

            <div class="row bk2">
                <div class="col-sm"><div class="summoner4"></div></div>
                <div class="col-sm"><div class="kda4"></div></div>
                <div class="col-sm"><div class="damage4"></div></div>
                <div class="col-sm"><div class="tier4"></div></div>
            </div>

            <div class="row bk1">
                <div class="col-sm"><div class="summoner5"></div></div>
                <div class="col-sm"><div class="kda5"></div></div>
                <div class="col-sm"><div class="damage5"></div></div>
                <div class="col-sm"><div class="tier5"></div></div>
            </div>

        </div>
    </div>

    Red Team
    <div id="gameStats200" class="gridLayout">
        <div class="container">
            <div class="row">
                <div class="col-sm"><u>Summoner</u></div>
                <div class="col-sm"><u>KDA</u></div>
                <div class="col-sm"><u>Damage</u></div>
                <div class="col-sm"><u>Previous Season's Tier</u></div>
            </div>

            <div class="row bk1">
                <div class="col-sm"><div class="summoner1"></div></div>
                <div class="col-sm"><div class="kda1"></div></div>
                <div class="col-sm"><div class="damage1"></div></div>
                <div class="col-sm"><div class="tier1"></div></div>
            </div>

            <div class="row bk2">
                <div class="col-sm"><div class="summoner2"></div></div>
                <div class="col-sm"><div class="kda2"></div></div>
                <div class="col-sm"><div class="damage2"></div></div>
                <div class="col-sm"><div class="tier2"></div></div>
            </div>

            <div class="row bk1">
                <div class="col-sm"><div class="summoner3"></div></div>
                <div class="col-sm"><div class="kda3"></div></div>
                <div class="col-sm"><div class="damage3"></div></div>
                <div class="col-sm"><div class="tier3"></div></div>
            </div>

            <div class="row bk2">
                <div class="col-sm"><div class="summoner4"></div></div>
                <div class="col-sm"><div class="kda4"></div></div>
                <div class="col-sm"><div class="damage4"></div></div>
                <div class="col-sm"><div class="tier4"></div></div>
            </div>

            <div class="row bk1">
                <div class="col-sm"><div class="summoner5"></div></div>
                <div class="col-sm"><div class="kda5"></div></div>
                <div class="col-sm"><div class="damage5"></div></div>
                <div class="col-sm"><div class="tier5"></div></div>
            </div>
        </div>
    </div>
</div>

<div class="help-tip" onclick="chart.startTutorial()">
    <p>Click here to start a tutorial for the web site</p>
</div><br>
</body>
</html>
<script>
    var chart;
    $('#nav-search').addClass('active');

    document.getElementById("loader").style.visibility = "visible";
    $.get('/history?user=' + "${username}", function(data){
        document.getElementById("loader").style.visibility = "hidden";
        data.forEach(function (d) {
            d.s = +d.score;
            d.highlighted = false;
        });
        chart = new PlotChart(data);
        chart.updateChart();
    });

    var padding = 80;
    var svg = d3.select("#plotChart");
    var width = +svg.attr("width") - 2 * padding;
    var height = +svg.attr("height") - 2 * padding;

    svg.append("text")
        .attr("transform", "rotate(-90) translate(" + padding + "," + padding + ")")
        .attr("y", padding - 120)
        .attr("x", -1 *(height / 2) - 150)
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Assigned Score");

    svg.append("text")
        .attr("transform",
            "translate(" + (width/2 + 80) + " ," +
            (height/2 + padding + 30) + ")")
        .style("text-anchor", "middle")
        .text("Game #");

    function moreGames() {
        if(!chart.alreadyHasGames()) {
            // var data = [];
            // var possScores = [];
            // for (var i = -15; i <= 15; i += .5) {
            //     possScores.push(i);
            // }
            // for (var i = 0; i < 5; i++) {
            //     var outcome = generateRandom(["Win", "Fail", "Dodge"]);
            //     var score = generateRandom(possScores);
            //     var g = i + 1;
            //     var repGame = {
            //         "g": g,
            //         "outcome": outcome,
            //         "s": score,
            //         "score": score
            //     };
            //     data.push(repGame);
            // }
            var matchId = chart.getLowestMatchId();
            document.getElementById("loader").style.visibility = "visible";
            $.get('/history?user=' + "${username}" + "&match=" + matchId, function(data){
                document.getElementById("loader").style.visibility = "hidden";
                data.forEach(function (d) {
                    d.s = +d.score;
                    d.highlighted = false;
                });
                chart.newGames(data);
            });
        }
        else {
            chart.addOldGames();
        }
    }

    // function generateRandom(possVals){
    //     var val;
    //     var valRand = Math.floor(Math.random() * (possVals.length));
    //     for (var i = 0; i < possVals.length; i++){
    //         if(valRand === i){
    //             val = possVals[i];
    //             break;
    //         }
    //     }
    //     return val;
    // }

</script>