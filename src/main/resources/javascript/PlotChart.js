class PlotChart{

    constructor(data){
        this.data = data;

        document.getElementById("PlayerStatImg").style.visibility = 'hidden';
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

    /**
     * Renders the HTML content for tool tip.
     *
     * @param tooltip_data information that needs to be populated in the tool tip
     * @return text HTML content for tool tip
     */
    tooltip_render(tooltip_data) {
        var text = "<h4 class =" + tooltip_data["result"] + " >" + tooltip_data["game id"] + "</h4>";
        //text += "Electoral Votes: " + tooltip_data.electoralVotes;

        return text;
    }

    updateChart(){
        var padding = 60;
        var width = 500 - 2 * padding;
        var height = 400 - 2 * padding;
        var radius = 8;
        var clickRadius = 10;

        var maxGame = d3.max(this.data, function (d){
            return d["g"];
        });

        var tip = d3.tip().attr('class', 'd3-tip')
            .direction('se')
            .offset(function () {
                return [0, 0];
            })
            .html((d) => {
            /* populate data in the following format
             * tooltip_data = {
             * "state": State,
             * "winner":d.State_Winner
             * "electoralVotes" : Total_EV
             * "result":[
             * {"nominee": D_Nominee_prop,"votecount": D_Votes,"percentage": D_Percentage,"party":"D"} ,
             * {"nominee": R_Nominee_prop,"votecount": R_Votes,"percentage": R_Percentage,"party":"R"} ,
             * {"nominee": I_Nominee_prop,"votecount": I_Votes,"percentage": I_Percentage,"party":"I"}
             * ]
             * }
             * pass this as an argument to the tooltip_render function then,
             * return the HTML content returned from that method.
             * */
            var tooltip_data = {
                "game id": d["game"],
                "score":d["score"],
                "result":d["result"]
            };
        return this.tooltip_render(tooltip_data);
    });

        var xScale = d3.scaleLinear()
            .domain([0, maxGame])
            .range([0, width]);
        var xAxis = d3.axisBottom();
        xAxis.scale(xScale);
        d3.select('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height/2 + padding) + ")")
            .call(xAxis);

        var yScale = d3.scaleLinear()
            .domain([10, -10])
            .range([0, height]);
        var yAxis = d3.axisLeft();
        yAxis.scale(yScale);
        var y = d3.select('#yAxis')
            .transition()
            .duration(3000)
            .attr("transform", "translate(" + padding + "," + padding + ")")
            .call(yAxis);

        var newYScale = d3.scaleLinear()
            .domain([-10, 10])
            .range([0, height]);

        var chart = d3.select('#plot').selectAll("circle")
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
                document.getElementById("PlayerStatImg").style.visibility = "visible";

            })
            .on("mouseover", tip.show)
            .on("mouseout", tip.hide);
        d3.select("#plotChart").call(tip);

    }

}
