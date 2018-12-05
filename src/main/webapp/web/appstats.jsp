<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>App Stats</title>

    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/css/styles.css"/>
    <link rel="stylesheet" href="resources/css/temp.css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">

    <script src="resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>

    <script src="https://d3js.org/d3.v4.js"></script>
    <script src="resources/javascript/d3-tip.js"></script>
    <script type="application/javascript" src="resources/javascript/BarChart.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>

<div>
    <span id="exit" class="tutorialExit" style="font-size: 2em; display: none" onclick="exitTutorial()">
        <i class="fas fa-times">

        </i>
    </span>
</div>


<div style="position: relative">

    <div class="menu">
        <span class="tooltiptext">Use this button to view data based on different ELO's</span>
        <span>
            <label>ELO:</label>
            <select id="dataset" onchange="changeData()">
                <option value="bronze">Bronze</option>
                <option value="silver">Silver</option>
                <option selected value="gold">Gold</option>
                <option value="platinum">Platinum</option>
                <option value="diamond">Diamond</option>
            </select>
        </span>
    </div>

    <div class="row">
        <div class="col-sm-6 chart chart1">
            <span class="tooltiptext">A graph to see your probability of winning for all possible scores</span>
            <header>
                <h2>Probability of winning based on assigned score</h2>
            </header>

            <svg width="700" height="400" id="barChart">
                <g id="xAxis"></g>
                <g id="yAxis"></g>
                <g id="bars"></g>
            </svg>
        </div>

        <div class="col-sm-6 chart chart2">
            <span class="tooltiptext">A graph to see your probability of getting assigned a score</span>
            <header>
                <h2>Probability of getting assigned certain scores</h2>
            </header>

            <svg width="700" height="400" id="barChart">
                <g id="xAxis"></g>
                <g id="yAxis1"></g>
                <g id="bars1"></g>
            </svg>
        </div>
    </div>

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


</body>

<script>
    $('#nav-app').addClass('active');

    d3.json("/resources/data/gold_data.json", function (error, data) {

        data.forEach(function (d) {
            d["total games"] = d.wins + d.losses;
            d["percentage"] = d.wins / d["total games"] * 100;
        });

        updateChart(data);

    });



</script>
</html>