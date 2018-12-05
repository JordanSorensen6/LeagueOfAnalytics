class PlotChart{

    constructor(data){
        this.data = data;
        this.unusedData = [];
        this.currentTip = 0;
        this.highlightElements = ["moreButton", "deleteButton", "graph", "legend"];

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
            .attr("r", function(d){
                if(d.highlighted){
                    return clickRadius
                }
                return radius
            })
            .attr("class", function(d){
                return d["outcome"];
            })
            .classed("highlighted", function(d){
                return d.highlighted;
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
                    .classed("highlighted", function (d){
                        d.highlighted = false;
                        return false;
                    })
                    .attr("r", radius);

                d3.select(this)
                    .classed("highlighted", true)
                    .attr("r", clickRadius);
                d.highlighted = true;

                $.get('/match?matchID=' + d["matchId"], function(data){

                    document.getElementById("gameStats").style.display = "inline";

                    var players = data["participants"];
                    var playerIds = data["participantIdentities"];

                    players.forEach(function (d, i) {
                        d["summoner"] = playerIds[i]["player"]["summonerName"];
                        var id = d["participantId"];
                        var stats = d["stats"];
                        var teamId = d["teamId"];
                        var teamDiv = document.getElementById("gameStats" + teamId);
                        var divId = (id % 5 == 0) ? 5 : (id % 5);

                        var summonerDiv = teamDiv.getElementsByClassName("summoner" + divId)[0];
                        summonerDiv.innerHTML = d["summoner"];

                        var kdaDiv = teamDiv.getElementsByClassName("kda" + divId)[0];
                        var deaths = stats["deaths"];
                        var kills = stats["kills"];
                        var assists = stats["assists"];
                        kdaDiv.innerHTML = (deaths == 0) ? "Perfect" : ((kills + assists) / deaths).toFixed(2);

                        var damageDiv = teamDiv.getElementsByClassName("damage" + divId)[0];
                        var damage = stats["totalDamageDealt"];
                        damageDiv.innerHTML = damage;

                        var tierDiv = teamDiv.getElementsByClassName("tier" + divId)[0];
                        var tier = d["highestAchievedSeasonTier"];
                        tierDiv.innerHTML = tier;
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

    startTutorial(){
        document.getElementsByClassName("help-tip")[0].style.visibility = "hidden";
        var tips = document.getElementsByClassName("tooltiptext");
        tips[0].style.visibility = "visible";
        document.getElementsByTagName("body")[0].classList.add("highlight-is-active");
        var highlightedElements = document.getElementsByClassName(this.highlightElements[0]);
        highlightedElements[0].classList.add("highlight");
        document.getElementById("arrows").style.display = "block";
        document.getElementsByClassName("arrowLeft")[0].style.visibility = "hidden";
        document.getElementsByClassName("arrowRight")[0].style.visibility = "visible";
        var exit = document.getElementById("exit");
        exit.style.display = "block";
    }

    continueTutorial(){
        var notHighlightedElements = document.getElementsByClassName(this.highlightElements[this.currentTip]);
        notHighlightedElements[0].classList.remove("highlight");
        var tips = document.getElementsByClassName("tooltiptext");
        document.getElementsByClassName("arrowLeft")[0].style.visibility = "visible";
        if(this.currentTip < tips.length - 1) {
            this.currentTip++;
            for (var i = 0; i < tips.length; i++) {
                tips[i].style.visibility = "hidden";
            }
            tips[this.currentTip].style.visibility = "visible";
            var elementsToHighlight = document.getElementsByClassName(this.highlightElements[this.currentTip]);
            elementsToHighlight[0].classList.add("highlight");
        }
        if(this.currentTip == tips.length - 1) {
            document.getElementsByClassName("arrowRight")[0].style.visibility = "hidden";
        }
    }

    goBackTutorial(){
        var notHighlightedElements = document.getElementsByClassName(this.highlightElements[this.currentTip]);
        notHighlightedElements[0].classList.remove("highlight");
        var tips = document.getElementsByClassName("tooltiptext");
        document.getElementsByClassName("arrowRight")[0].style.visibility = "visible";
        if(this.currentTip > 0){
            this.currentTip--;
            for (var i = 0; i < tips.length; i++) {
                tips[i].style.visibility = "hidden";
            }
            tips[this.currentTip].style.visibility = "visible";
            var elementsToHighlight = document.getElementsByClassName(this.highlightElements[this.currentTip]);
            elementsToHighlight[0].classList.add("highlight");
        }
        if(this.currentTip == 0){
            document.getElementsByClassName("arrowLeft")[0].style.visibility = "hidden";
        }
    }

    exitTutorial(){
        var exit = document.getElementById("exit");
        exit.style.display = "none"; // get rid of exit
        document.getElementById("arrows").style.display = "none";
        document.getElementsByClassName("help-tip")[0].style.visibility = "visible";
        var highlightedElements = document.getElementsByClassName(this.highlightElements[this.currentTip]);
        highlightedElements[0].classList.remove("highlight");
        document.getElementsByTagName("body")[0].classList.remove("highlight-is-active");
        var tips = document.getElementsByClassName("tooltiptext");
        for(var i = 0; i < tips.length; i++){
            tips[i].style.visibility = "hidden";
        }
        this.currentTip = 0;
    }
}
