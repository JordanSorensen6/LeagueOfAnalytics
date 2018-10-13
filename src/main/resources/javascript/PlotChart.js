class PlotChart{

    constructor(data){
        this.data = data;
        this.unusedData = [];
        //document.getElementById("PlayerStatImg").style.visibility = 'hidden';
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
        if(this.data.length === 5){
            document.getElementById("delete").disabled = true;
        }
        var padding = 80;
        var svg = d3.select("#plotChart");
        var width = +svg.attr("width") - 2 * padding;
        var height = +svg.attr("height") - 2 * padding;
        var radius = 8;
        var clickRadius = 10;
        // svg.append("text")
        //     .attr("transform", "rotate(-90) translate(" + padding + "," + padding + ")")
        //     .attr("y", padding - 120)
        //     .attr("x", -1 *(height / 2) - 150)
        //     .attr("dy", "1em")
        //     .style("text-anchor", "middle")
        //     .text("Assigned Score");
        //
        // svg.append("text")
        //     .attr("transform",
        //         "translate(" + (width/2 + 80) + " ," +
        //         (height/2 + padding + 30) + ")")
        //     .style("text-anchor", "middle")
        //     .text("Game #");

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
        var xAxis = d3.axisBottom().ticks(this.data.length - 1);
        xAxis.scale(xScale);
        d3.select('#xAxis')
            .classed("axis", true)
            .attr("transform", "translate(" + padding + "," + (height/2 + padding) + ")")
            .call(xAxis);

        var newxScale = d3.scaleLinear()
            .domain([0, this.data.length])
            .range([0, width]);

        var scores = [];
        for(var j = -15; j <= 15; j = j+1){
            scores.push(j);
        }
        var yScale = d3.scaleLinear()
            .domain([15, -15])
            .range([0, height]);
        var yAxis = d3.axisLeft().ticks(scores.length);
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

        var chartEnter = chart
            .enter()
            .append("circle");

        chartEnter
            .attr("transform", "translate(" + padding + "," + (height + padding) + ") scale(1, -1)")
            .attr("r", radius)
            .attr("class", function(d){
                return d["outcome"];
            });

        chart.exit().remove();

        chart = chart.merge(chartEnter);

        chart
            .attr("cy", function (d) {
                return newYScale(d.s);
            })
            .attr("cx", function (d) {
                return newxScale(d.g);
            });

        d3.select("#plot").selectAll("circle")
            .on("click", function (d){
                d3.select("#plot").selectAll("circle")
                    .classed("highlighted", false)
                    .attr("r", radius);

                d3.select(this)
                    .classed("highlighted", true)
                    .attr("r", clickRadius);
                $.get('/match?matchID=' + d["matchId"], function(data){

                    document.getElementById("gameStats").style.display = "inline";

                    var players = data["participants"];
                    var playerIds = data["participantIdentities"];

                    players.forEach(function (d, i) {
                        d["summoner"] = playerIds[i]["player"]["summonerName"];
                        var id = d["participantId"];
                        var stats = d["stats"];
                        console.log(d);
                        document.getElementById("summoner" + id).innerHTML = d["summoner"];

                        var KDAdiv = document.getElementById("KDA" + id);
                        if(d["stats"]["deaths"] === 0){
                            KDAdiv.innerHTML = "Perfect";
                        }
                        else{
                            KDAdiv.innerHTML = ((stats["kills"] + stats["assists"])/stats["deaths"]).toFixed(2);
                        }

                        var damageDiv = document.getElementById("Damage" + id);
                        damageDiv.innerHTML = stats["totalDamageDealt"];

                        var tierDiv = document.getElementById("Tier" + id);
                        tierDiv.innerHTML = d["highestAchievedSeasonTier"];
                    })
                });
                // if(d.outcome === "Win") {
                //     $('#PlayerLossPic').hide();
                //     $('#PlayerWinPic').show();
                // }
                // else {
                //     $('#PlayerLossPic').show();
                //     $('#PlayerWinPic').hide();
                // }
                // document.getElementById("PlayerStatImg").style.visibility = "visible";

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

    alreadyHasGames() {
        if (this.unusedData.length === 0){
            return false;
        }
        return true;
    }

    addOldGames(){
        var deleteButton = document.getElementById("delete");
        if(deleteButton.disabled){
            deleteButton.disabled = false;
        }
        
        var hiData = this.unusedData.splice(this.unusedData.length - 5);

        this.data = this.data.concat(hiData);

        this.updateChart();
    }

    newGames(data) {
        var deleteButton = document.getElementById("delete");
        if(deleteButton.disabled){
            deleteButton.disabled = false;
        }
        var lastGameNumb = this.data[this.data.length - 1];
        data.forEach((d) => {
            d["g"] = d["g"] + lastGameNumb["g"];
            this.data.push(d);
        });

        this.updateChart();

    }

    lessGames(){
        var byeData = this.data.splice(this.data.length - 5);
        this.unusedData = this.unusedData.concat(byeData);
        this.updateChart();
    }

    getLowestMatchId(){
        return d3.min(this.data, function(d){
            return d["matchId"];
        })
    }

}
