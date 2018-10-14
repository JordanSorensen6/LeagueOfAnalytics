<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Stats - ${username}</title>

    <link rel="stylesheet" href="/resources/css/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="/resources/css/styles.css"/>
    <link rel="stylesheet" href="/resources/css/temp.css"/>
    <link rel="stylesheet" href="/resources/css/statsLayout.css">

    <script src="https://d3js.org/d3.v4.js"></script>
    <script src="/resources/javascript/d3-tip.js"></script>
    <script src="/resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="/resources/javascript/bootstrap/bootstrap.min.js"></script>

    <script src="/resources/javascript/PlotChart.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>

${username} stats


<div class="chart">
    <button type="button" onclick="moreGames()" id = "add">Add More Games</button>
    <button type="button" onclick="chart.lessGames()" id = "delete">Take Away Games</button>
    <h4>Assigned Scores For Past Games</h4>
    <div class = "row">
    <svg width="800" height="600" id="plotChart">
        <g id="xAxis"></g>
        <g id="yAxis"></g>
        <g id="plot"></g>
    </svg>
        <div>
            <h4>Legend</h4>
            <svg width="200" height="400" id="legend">

            </svg>
        </div>
    </div>

</div>


<div id="gameStats" style="display: none">
    Blue Team
    <div id="gameStats100" class="gridLayout">
        <div class="summoner"><u>Summoner</u></div>
        <div class="summoner1"></div>
        <div class="summoner2"></div>
        <div class="summoner3"></div>
        <div class="summoner4"></div>
        <div class="summoner5"></div>

        <div class="kda"><u>KDA</u></div>
        <div class="kda1"></div>
        <div class="kda2"></div>
        <div class="kda3"></div>
        <div class="kda4"></div>
        <div class="kda5"></div>

        <div class="damage"><u>Damage</u></div>
        <div class="damage1"></div>
        <div class="damage2"></div>
        <div class="damage3"></div>
        <div class="damage4"></div>
        <div class="damage5"></div>

        <div class="tier"><u>Tier</u></div>
        <div class="tier1"></div>
        <div class="tier2"></div>
        <div class="tier3"></div>
        <div class="tier4"></div>
        <div class="tier5"></div>
    </div>
    Red Team
    <div id="gameStats200" class="gridLayout">
        <div class="summoner"><u>Summoner</u></div>
        <div class="summoner1"></div>
        <div class="summoner2"></div>
        <div class="summoner3"></div>
        <div class="summoner4"></div>
        <div class="summoner5"></div>

        <div class="kda"><u>KDA</u></div>
        <div class="kda1"></div>
        <div class="kda2"></div>
        <div class="kda3"></div>
        <div class="kda4"></div>
        <div class="kda5"></div>

        <div class="damage"><u>Damage</u></div>
        <div class="damage1"></div>
        <div class="damage2"></div>
        <div class="damage3"></div>
        <div class="damage4"></div>
        <div class="damage5"></div>

        <div class="tier"><u>Tier</u></div>
        <div class="tier1"></div>
        <div class="tier2"></div>
        <div class="tier3"></div>
        <div class="tier4"></div>
        <div class="tier5"></div>
    </div>
    <%--<div class="column" style="left:1000px">--%>
        <%--<div id="header">--%>

        <%--</div>--%>
        <%--<div id="Team100" class="column">--%>
            <%--<div id="1" class="row">--%>
                <%--<text id="summoner1"></text>--%>
                <%--<text id="KDA1"></text>--%>
                <%--<text id="Damage1"></text>--%>
                <%--<text id="Tier1"></text>--%>
            <%--</div>--%>
            <%--<div id="2" class="row">--%>
                <%--<text id="summoner2"></text>--%>
                <%--<text id="KDA2"></text>--%>
                <%--<text id="Damage2"></text>--%>
                <%--<text id="Tier2"></text>--%>
            <%--</div>--%>
            <%--<div id="3" class="row">--%>
                <%--<text id="summoner3"></text>--%>
                <%--<text id="KDA3"></text>--%>
                <%--<text id="Damage3"></text>--%>
                <%--<text id="Tier3"></text>--%>
            <%--</div>--%>
            <%--<div id="4" class="row">--%>
                <%--<text id="summoner4"></text>--%>
                <%--<text id="KDA4"></text>--%>
                <%--<text id="Damage4"></text>--%>
                <%--<text id="Tier4"></text>--%>
            <%--</div>--%>
            <%--<div id="5" class="row">--%>
                <%--<text id="summoner5"></text>--%>
                <%--<text id="KDA5"></text>--%>
                <%--<text id="Damage5"></text>--%>
                <%--<text id="Tier5"></text>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div id="Team200" class="column">--%>
            <%--<div id="6" class="row">--%>
                <%--<text id="summoner6"></text>--%>
                <%--<text id="KDA6"></text>--%>
                <%--<text id="Damage6"></text>--%>
                <%--<text id="Tier6"></text>--%>
            <%--</div>--%>
            <%--<div id="7" class="row">--%>
                <%--<text id="summoner7"></text>--%>
                <%--<text id="KDA7"></text>--%>
                <%--<text id="Damage7"></text>--%>
                <%--<text id="Tier7"></text>--%>
            <%--</div>--%>
            <%--<div id="8" class="row">--%>
                <%--<text id="summoner8"></text>--%>
                <%--<text id="KDA8"></text>--%>
                <%--<text id="Damage8"></text>--%>
                <%--<text id="Tier8"></text>--%>
            <%--</div>--%>
            <%--<div id="9" class="row">--%>
                <%--<text id="summoner9"></text>--%>
                <%--<text id="KDA9"></text>--%>
                <%--<text id="Damage9"></text>--%>
                <%--<text id="Tier9"></text>--%>
            <%--</div>--%>
            <%--<div id="10" class="row">--%>
                <%--<text id="summoner10"></text>--%>
                <%--<text id="KDA10"></text>--%>
                <%--<text id="Damage10"></text>--%>
                <%--<text id="Tier10"></text>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--&lt;%&ndash;<div id="PlayerWinPic" class="PlayerStatPic" style="display: none;">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<img id="PlayerLossImg" src="/resources/images/victory.png" style="left:500px">&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div id="PlayerLossPic" class="PlayerStatPic" style="display: none;">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<img id="PlayerStatImg" src="/resources/images/SampleGame.png" style="left:500px">&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--</div>--%>
</div>

<div class="chart">
    <svg width="100" height="600">

    </svg>
</div>
</body>
</html>
<script>
    var chart;
    $('#nav-search').addClass('active');

    $.get('/history?user=' + "${username}", function(data){
        console.log(data);
        data.forEach(function (d) {
            d.s = +d.score;
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
            $.get('/history?user=' + "${username}" + "&match=" + matchId, function(data){
                data.forEach(function (d) {
                    d.s = +d.score;
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