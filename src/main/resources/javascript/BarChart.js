




    function updateChart(col, data){
        var padding = 60;
        var width = 700 - 2 * padding;
        var height = 400 - 2 * padding;
        var numbGames = d3.sum(data, function (d) {
            return d["total games"];
        });

        data.forEach(function (d) {
            d["chance"] = d["total games"]/numbGames*100;
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
        var y = d3.select('#yAxis')
            .classed("axis", true)
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
                if(col == 16) {
                    return colorScale(d["percentage"]);
                }

            })
            .attr("stroke", "darkgray")
            .attr("stroke-width", "1px");








        var maxChance = d3.max(data, function (d){
           return d["chance"]
        });
        var colorScale1 = d3.scaleLinear()
            .domain([0, maxChance])
            .range(["lightblue", "steelblue"]);
        var yScale1 = d3.scaleLinear()
            .domain([maxChance, 0])
            .range([0, height]);
        var yAxis1 = d3.axisLeft();
        yAxis1.scale(yScale1);
        var y1 = d3.select('#yAxis1')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + padding + ")")
            .call(yAxis1);

        var newYScale1 = d3.scaleLinear()
            .domain([0, maxChance])
            .range([0, height]);


        var barChart1 = d3.select('#bars1').selectAll("rect")
            .data(data);

        barChart1.exit()
            .remove();

        barChart1 = barChart1
            .enter()
            .append("rect")
            .merge(barChart1);

        barChart1
            .attr("transform", "translate(" + padding + "," + (height + padding) + ") scale(1, -1)")
            .attr("height", function(d, i){
                return newYScale1(d["chance"]);
            })
            .attr("width", width/62 - 1)
            .attr("y", 0)
            .attr("x", function (d, i) {
                return xScale(d["score"]);
            })
            .attr("fill", function (d) {
                if(col == 16) {
                    return colorScale1(d["chance"]);
                }
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

            updateChart(16, data);

        });
    }
