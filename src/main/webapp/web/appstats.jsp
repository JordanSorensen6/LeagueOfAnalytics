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

<span>
    <label>Dataset:</label>
    <select id="dataset" onchange="changeData()">
        <option value="anscombe_I">Anscombe's Quartet I</option>
        <option selected value="anscombe_II">Anscombe's Quartet II</option>
        <option value="anscombe_III">Anscombe's Quartet III</option>
        <option value="anscombe_IV">Anscombe's Quartet IV</option>
    </select>
</span>

<div>
    <h2>Bar Charts</h2>
    <svg width="200" height="200">
        <g id = "barChart1" transform="translate(0, 200) scale(1, -1)" class="barChart" >
            <rect x="10" y="0" width="10" height="100"></rect>
            <rect x="20" y="0" width="10" height="80"></rect>
            <rect x="30" y="0" width="10" height="130"></rect>
            <rect x="40" y="0" width="10" height="90"></rect>
            <rect x="50" y="0" width="10" height="110"></rect>
            <rect x="60" y="0" width="10" height="140"></rect>
            <rect x="70" y="0" width="10" height="60"></rect>
            <rect x="80" y="0" width="10" height="40"></rect>
            <rect x="90" y="0" width="10" height="120"></rect>
            <rect x="100" y="0" width="10" height="70"></rect>
            <rect x="110" y="0" width="10" height="50"></rect>
        </g>
    </svg>

    <svg width="200" height="200">
        <g id = "barChart2" transform="translate(0, 200) scale(1, -1)" class="barChart" >
            <rect x="10" y="0" width="10" height="91"></rect>
            <rect x="20" y="0" width="10" height="81"></rect>
            <rect x="30" y="0" width="10" height="87"></rect>
            <rect x="40" y="0" width="10" height="88"></rect>
            <rect x="50" y="0" width="10" height="93"></rect>
            <rect x="60" y="0" width="10" height="81"></rect>
            <rect x="70" y="0" width="10" height="61"></rect>
            <rect x="80" y="0" width="10" height="31"></rect>
            <rect x="90" y="0" width="10" height="91"></rect>
            <rect x="100" y="0" width="10" height="73"></rect>
            <rect x="110" y="0" width="10" height="47"></rect>
        </g>
    </svg>
</div>

</body>

<script>
    $('#nav-app').addClass('active');

    // d3.json("/resources/data/gold_data.json", function (error, data) {
    //
    //     data.forEach(function (d) {
    //         d["total games"] = d.wins + d.losses;
    //         d["percentage"] = d.wins/d["total games"]*100;
    //     });
    //
    //     let chart = new BarChart(data);
    //     chart.updateChart();
    //
    // });


</script>
</html>