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
        var text = "<h4 class =" + tooltip_data["outcome"] + " >" + "Score: " + tooltip_data["score"] + "</h4>";
        //text += "Electoral Votes: " + tooltip_data.electoralVotes;

        return text;
    }

    updateChart(){
        var padding = 60;
        var width = 500 - 2 * padding;
        var height = 400 - 2 * padding;
        var radius = 8;
        var clickRadius = 10;

        var i = 1;
        this.data.forEach(function (d) {
            d["g"] = i;
            i++;
        });


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
             * "outcome":[
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
                "outcome":d["outcome"]
            };
        return this.tooltip_render(tooltip_data);
    });

        var xScale = d3.scaleLinear()
            .domain([0, this.data.length])
            .range([0, width]);
        var xAxis = d3.axisBottom();
        xAxis.scale(xScale);
        d3.select('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height/2 + padding) + ")")
            .call(xAxis);

        var newxScale = d3.scaleLinear()
            .domain([0, this.data.length])
            .range([0, width]);

        var scores = [];
        for(var j = 15; j <= 15; j = j+.5){

        }
        var yScale = d3.scaleLinear()
            .domain([15, -15])
            .range([0, height]);
        var yAxis = d3.axisLeft();
        yAxis.scale(yScale);
        var y = d3.select('#yAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + padding + ")")
            .call(yAxis);

        var newYScale = d3.scaleLinear()
            .domain([-15, 15])
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
                return newxScale(d.g);
            })
            .attr("class", function(d){
                return d["outcome"];
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

        var legend = d3.select("#legend").selectAll("circle")
            .data([{index:0, result:"win"}, {index:1, result:"fail"}, {index:2, result:"dodge"}, {index:3, result:"highlighted"}]);
        legend
            .enter()
            .append("circle")
            .attr("cx", 100)
            .attr("cy", function (d){
                return 40 + 50 * d["index"];
            })
            .attr("r", function (d) {
                if(d["result"] == "highlighted"){
                    return clickRadius;
                }
                return radius;
            })
            .attr("class", function (d) {
                return d["result"];
            });

        legend
            .enter()
            .append("text")
            .text(function (d){
                return d["result"];
            })
            .attr("x", 120)
            .attr("y", function (d){
                return 45 + 50 * d["index"];
            });




    }

}
