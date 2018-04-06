class Chart{

    constructor(data){
        this.data = data;
    }

    chooseClass(score){
        if(score < 0){
            return "loss";
        }
        else if(score > 0){
            return "win";
        }
        else{
            return "dodge";
        }
    }

    updateChart(){
        let padding = 60;
        let width = 500 - 2 * padding;
        let height = 400 - 2 * padding;
        let radius = 8;
        let clickRadius = 10;

        let maxGame = d3.max(this.data, function (d){
            return d["g"];
        });

        let xScale = d3.scaleLinear()
            .domain([0, maxGame])
            .range([0, width]);
        let xAxis = d3.axisBottom();
        xAxis.scale(xScale);
        d3.select('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height/2 + padding) + ")")
            .call(xAxis);

        let yScale = d3.scaleLinear()
            .domain([10, -10])
            .range([0, height]);
        let yAxis = d3.axisLeft();
        yAxis.scale(yScale);
        let y = d3.select('#yAxis')
            .transition()
            .duration(3000)
            .attr("transform", "translate(" + padding + "," + padding + ")")
            .call(yAxis);

        let newYScale = d3.scaleLinear()
            .domain([-10, 10])
            .range([0, height]);

        let chart = d3.select('#plot').selectAll("circle")
            .data(this.data);
        chart = chart
            .enter()
            .append("circle")
            .merge(chart);

        chart
            .transition()
            .duration(3000)
            .attr("transform", "translate(" + padding + "," + (height + padding) + ") scale(1, -1)")
            .attr("r", radius)
            .attr("cy", function (d) {
                return newYScale(d.s);
            })
            .attr("cx", function (d) {
                return xScale(d.g);
            })
            .attr("class", function(d){
                return d["result"];
            });

        d3.select("#plot").selectAll("circle")
            .on("click", function (d){
                d3.select("#plot").selectAll("circle")
                    .classed("highlighted", false)
                    .attr("r", radius);

                d3.select(this)
                    .classed("highlighted", true)
                    .attr("r", clickRadius);
            });

    }

}