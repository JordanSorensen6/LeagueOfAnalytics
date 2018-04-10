<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Stats - ${username}</title>

    <link rel="stylesheet" href="/resources/css/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="/resources/css/styles.css"/>
    <link rel="stylesheet" href="/resources/css/layout.css"/>

    <script src="https://d3js.org/d3.v4.js"></script>
    <script src="/resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="/resources/javascript/bootstrap/bootstrap.min.js"></script>

    <script src="/resources/javascript/Chart.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>

${username} is very good at the game!


<div>
    <svg width="500" height="400" id="plotChart">
        <g id="xAxis"></g>
        <g id="yAxis"></g>
        <g id="plot"></g>
    </svg>
</div>
</body>
</html>
<script>
    $('#nav-search').addClass('active');



    d3.json("/resources/data/fake_data.json", function (error, data) {

        data.forEach(function (d) {
            d.g = +d.game;
            d.s = +d.score;
        });
        let chart = new Chart(data);
        chart.updateChart();
    });
</script>