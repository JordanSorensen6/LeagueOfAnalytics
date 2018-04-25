




    function updateChart(data){
        var padding = 60;
        var width = 700 - 2 * padding;
        var height = 400 - 2 * padding;
        var numbGames = d3.sum(data, function (d) {
            return d["total games"];
        });
        data.forEach(function (d) {
            d["chance"] = d["total games"]/numbGames;
        });

        var maxScore = d3.max(data, function (d){
            return d["score"];
        });
        var minScore = d3.min(data, function (d){
            return d["score"];
        });

        var maxPercentage = d3.max(data, function (d){
            return d["percentage"];
        });
        var minPercentage = d3.min(data, function (d){
            return d["percentage"];
        });

        var colorScale = d3.scaleLinear()
            .domain([0, maxPercentage])
            .range(["lightblue", "steelblue"]);

        var xScale = d3.scaleLinear()
            .domain([-15, 15])
            .range([0, width]);
        var xAxis = d3.axisBottom();
        xAxis.scale(xScale);
        d3.selectAll('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height + padding) + ")")
            .call(xAxis);

        var yScale = d3.scaleLinear()
            .domain([100, 0])
            .range([0, height]);
        var yAxis = d3.axisLeft();
        yAxis.scale(yScale);
        var y = d3.selectAll('#yAxis')
            .attr("transform", "translate(" + padding + "," + padding + ")")
            .call(yAxis);

        var newYScale = d3.scaleLinear()
            .domain([0, 100])
            .range([0, height]);

        var barChart = d3.select('#bars').selectAll("rect")
            .data(data);

        barChart.exit()
            .remove();

        barChart = barChart
            .enter()
            .append("rect")
            .merge(barChart);

        barChart
            .attr("transform", "translate(" + padding + "," + (height + padding) + ") scale(1, -1)")
            .attr("height", function(d, i){
                return newYScale(d["percentage"]);
            })
            .attr("width", width/62 - 1)
            .attr("y", 0)
            .attr("x", function (d, i) {
                return xScale(d["score"]);
            })
            .attr("fill", function (d) {
                return colorScale(d["percentage"]);
            })
            .attr("stroke", "darkgray")
            .attr("stroke-width", "1px");
    }

    function changeData(){

        var dataFile = document.getElementById('dataset').value;

        d3.json("/resources/data/"+dataFile+"_data.json", function (error, data) {

            data.forEach(function (d) {
                d["total games"] = d.wins + d.losses;
                d["percentage"] = d.wins / d["total games"] * 100;
            });

            updateChart(data);

        });
    }
