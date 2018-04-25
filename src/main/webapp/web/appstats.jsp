<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Player Search</title>

    <link rel="stylesheet" href="/resources/css/bootstrap/bootstrap.min.css"/>

    <script src="/resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="/resources/javascript/bootstrap/bootstrap.min.js"></script>

    <script src="https://d3js.org/d3.v4.js"></script>
    <script type="application/javascript" src="/resources/javascript/BarChart.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>


<div>
    <header>
        <h2>Probability of winning based on assigned score</h2>
    </header>

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

    <svg width="700" height="400" id="barChart">
        <g id="xAxis"></g>
        <g id="yAxis"></g>
        <g id="bars"></g>
    </svg>
</div>


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