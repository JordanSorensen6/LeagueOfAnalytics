class BarChart{


    constructor(data){
        this.data = data;
    }

    updateChart(){
        let padding = 60;
        let width = 500 - 2 * padding;
        let height = 400 - 2 * padding;

        let maxScore = d3.max(this.data, function (d){
            return d["score"];
        });
        let minScore = d3.min(this.data, function (d){
            return d["score"];
        });

        let maxPercentage = d3.max(this.data, function (d){
            return d["percentage"];
        });
        let minPercentage = d3.min(this.data, function (d){
            return d["percentage"];
        });

        let xScale = d3.scaleLinear()
            .domain([minScore, maxScore])
            .range([0, width]);
        let xAxis = d3.axisBottom();
        xAxis.scale(xScale);
        d3.select('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height + padding) + ")")
            .call(xAxis);

        //let yScale =
    }
}