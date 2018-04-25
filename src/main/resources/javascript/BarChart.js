class BarChart{


    constructor(data){
        this.data = data;
    }

    updateChart(){
        var padding = 60;
        var width = 700 - 2 * padding;
        var height = 400 - 2 * padding;
        var numbScores = this.data.length;

        var maxScore = d3.max(this.data, function (d){
            return d["score"];
        });
        var minScore = d3.min(this.data, function (d){
            return d["score"];
        });

        var maxPercentage = d3.max(this.data, function (d){
            return d["percentage"];
        });
        var minPercentage = d3.min(this.data, function (d){
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
        d3.select('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height + padding) + ")")
            .call(xAxis);

        var yScale = d3.scaleLinear()
            .domain([100, 0])
            .range([0, height]);
        var yAxis = d3.axisLeft();
        yAxis.scale(yScale);
        var y = d3.select('#yAxis')
            .attr("transform", "translate(" + padding + "," + padding + ")")
            .call(yAxis);

        var newYScale = d3.scaleLinear()
            .domain([0, 100])
            .range([0, height]);

        var barChart = d3.select('#bars').selectAll("rect")
            .data(this.data);
        barChart = barChart
            .enter()
            .append("rect")
            .merge(barChart);

        barChart
            .attr("transform", "translate(" + padding + "," + (height + padding) + ") scale(1, -1)")
            .attr("height", function(d, i){
                return newYScale(d["percentage"]);
            })
            .attr("width", width/numbScores - 2)
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

    changeData(){
        console.log("hi");
    }
}