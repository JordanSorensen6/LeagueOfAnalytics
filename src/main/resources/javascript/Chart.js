
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

    /**
     * Renders the HTML content for tool tip.
     *
     * @param tooltip_data information that needs to be populated in the tool tip
     * @return text HTML content for tool tip
     */
    tooltip_render(tooltip_data) {
        let text = "<h3 class =" + this.chooseClass(tooltip_data["result"]) + " >" + tooltip_data["game id"] + "</h3>";
        //text += "Electoral Votes: " + tooltip_data.electoralVotes;

        return text;
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

        let tip = d3.tip().attr('class', 'd3-tip')
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
            let tooltip_data = {
                "game id": d["game"],
                "score":d["score"],
                "result":d["result"]
            };
        return this.tooltip_render(tooltip_data);
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
            })
            .on("mouseover", tip.show)
            .on("mouseout", tip.hide);
        d3.select("#plotChart").call(tip);

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
