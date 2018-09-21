<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Stats - ${username}</title>

    <link rel="stylesheet" href="/resources/css/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="/resources/css/styles.css"/>
    <link rel="stylesheet" href="/resources/css/temp.css"/>

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

<div id="PlayerWinPic" class="PlayerStatPic" style="display: none;">
    <img id="PlayerLossImg" src="/resources/images/victory.png" style="left:500px">
</div>
<div id="PlayerLossPic" class="PlayerStatPic" style="display: none;">
    <img id="PlayerStatImg" src="/resources/images/SampleGame.png" style="left:500px">
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

    function moreGames() {
        if(!chart.alreadyHasGames()) {
            var data = [];
            var possScores = [];
            for (var i = -15; i <= 15; i += .5) {
                possScores.push(i);
            }
            for (var i = 0; i < 5; i++) {
                var outcome = generateRandom(["Win", "Fail", "Dodge"]);
                var score = generateRandom(possScores);
                var g = i + 1;
                var repGame = {
                    "g": g,
                    "outcome": outcome,
                    "s": score,
                    "score": score
                };
                data.push(repGame);
            }
            chart.newGames(data);
        }
        else {

        }
    }

    function generateRandom(possVals){
        var val;
        var valRand = Math.floor(Math.random() * (possVals.length));
        for (var i = 0; i < possVals.length; i++){
            if(valRand === i){
                val = possVals[i];
                break;
            }
        }
        return val;
    }

</script>