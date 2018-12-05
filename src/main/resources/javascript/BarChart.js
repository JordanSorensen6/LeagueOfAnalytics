
    function tooltip_render(tooltip_data){
        var text = "<h4 style = color:black > Score: " + tooltip_data["score"] + "</h4> " +
            "<h5 style = color:black > Probability of winning: " + (tooltip_data["percentage"]).toFixed(1) + "% </h5>" +
            "<h5 style = color:black > Chance of getting a score: " + (tooltip_data["chance"]).toFixed(1) + "% </h5>";
        //text += "Electoral Votes: " + tooltip_data.electoralVotes;

        return text;
    }



    function updateChart(data){



        console.log(data)
        var padding = 60;
        var width = 700 - 2 * padding;
        var height = 400 - 2 * padding;
        var numbGames = d3.sum(data, function (d) {
            return d["total games"];
        });

        var svg = d3.selectAll("#barChart");

        svg.append("text")
            .attr("transform", "rotate(-90) translate(" + padding + "," + padding + ")")
            .attr("y", padding - 100)
            .attr("x", -1 *(height / 2) - 100)
            .attr("dy", "1em")
            .style("text-anchor", "middle")
            .text("Probability (%)");

        svg.append("text")
            .attr("transform",
                "translate(" + (width/2 + 60) + " ," +
                (height + padding + 30) + ")")
            .style("text-anchor", "middle")
            .text("Score");

        var tip = d3.tip().attr('class', 'd3-tip')
            .direction('se')
            .offset(function () {
                return [0, 0];
            })
            .html((d) => {
            console.log(d);
            var tooltip_data = {
                "score":d["score"],
                "percentage":d["percentage"],
                "chance":d["chance"]
            };
        return this.tooltip_render(tooltip_data);
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

        var scores = [];
        for(var j = -15; j <= 15; j = j+1){
            scores.push(j);
        }
        console.log(scores.length)
        var xScale = d3.scaleLinear()
            .domain([-15, 15])
            .range([0, width]);
        var xAxis = d3.axisBottom().ticks(scores.length);
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
                if(d["score"].toString() == sessionStorage.getItem('score')){
                    return "orange"
                }
                return colorScale(d["percentage"]);
            })
            .attr("stroke", "darkgray")
            .attr("stroke-width", "1px");








        var maxChance = d3.max(data, function (d){
           return d["chance"]
        });
        var maxTick = Math.ceil(maxChance)
        var colorScale1 = d3.scaleLinear()
            .domain([0, maxChance])
            .range(["lightblue", "steelblue"]);
        var yScale1 = d3.scaleLinear()
            .domain([maxTick, 0])
            .range([0, height]);
        var yAxis1 = d3.axisLeft().ticks(maxTick);
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
                if(d["score"].toString() == sessionStorage.getItem('score')){
                    return "orange"
                }
                return colorScale1(d["chance"]);
            })
            .attr("stroke", "darkgray")
            .attr("stroke-width", "1px");

        d3.selectAll("#barChart").selectAll("rect")
            .on("mouseover", tip.show)
            .on("mouseout", tip.hide);

        d3.selectAll("#barChart").call(tip);


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

    var currentTip = 0;
    var highlightElements = ["menu", "chart1", "chart2"];

    function startTutorial() {
        document.getElementsByClassName("help-tip")[0].style.visibility = "hidden";
        var tips = document.getElementsByClassName("tooltiptext");
        tips[0].style.visibility = "visible";
        document.getElementsByTagName("body")[0].classList.add("highlight-is-active");
        var highlightedElements = document.getElementsByClassName(highlightElements[0]);
        highlightedElements[0].classList.add("highlight");
        document.getElementById("arrows").style.display = "block";
        var exit = document.getElementById("exit");
        exit.style.display = "block";
        document.getElementsByClassName("arrowLeft")[0].style.visibility = "hidden";
        document.getElementsByClassName("arrowRight")[0].style.visibility = "visible";
    }
    
    function continueTutorial() {
        var notHighlightedElements = document.getElementsByClassName(highlightElements[currentTip]);
        for(var i = 1; i < notHighlightedElements.length; i++){
            notHighlightedElements[i].classList.remove("alsoHighlighted");
        }
        notHighlightedElements[0].classList.remove("highlight");
        var tips = document.getElementsByClassName("tooltiptext");
        document.getElementsByClassName("arrowLeft")[0].style.visibility = "visible";
        if(currentTip < tips.length - 1) {
            currentTip++;
            for (var i = 0; i < tips.length; i++) {
                tips[i].style.visibility = "hidden";
            }
            tips[currentTip].style.visibility = "visible";
            var elementsToHighlight = document.getElementsByClassName(highlightElements[currentTip]);
            elementsToHighlight[0].classList.add("highlight");
        }
        if(currentTip == tips.length - 1) {
            document.getElementsByClassName("arrowRight")[0].style.visibility = "hidden";
        }
    }
    
    function goBackTutorial() {
        var notHighlightedElements = document.getElementsByClassName(highlightElements[currentTip]);
        for(var i = 1; i < notHighlightedElements.length; i++){
            notHighlightedElements[i].classList.remove("alsoHighlighted");
        }
        notHighlightedElements[0].classList.remove("highlight");
        var tips = document.getElementsByClassName("tooltiptext");
        document.getElementsByClassName("arrowRight")[0].style.visibility = "visible";
        if(currentTip > 0){
            currentTip--;
            for (var i = 0; i < tips.length; i++) {
                tips[i].style.visibility = "hidden";
            }
            tips[currentTip].style.visibility = "visible";
            var elementsToHighlight = document.getElementsByClassName(highlightElements[currentTip]);
            elementsToHighlight[0].classList.add("highlight");
        }
        if(currentTip == 0){
            document.getElementsByClassName("arrowLeft")[0].style.visibility = "hidden";
        }
    }

    function exitTutorial() {
        var exit = document.getElementById("exit");
        exit.style.display = "none"; // get rid of exit
        document.getElementById("arrows").style.display = "none";
        document.getElementsByClassName("help-tip")[0].style.visibility = "visible";
        var highlightedElements = document.getElementsByClassName(highlightElements[currentTip]);
        highlightedElements[0].classList.remove("highlight");
        document.getElementsByTagName("body")[0].classList.remove("highlight-is-active");
        var tips = document.getElementsByClassName("tooltiptext");
        for(var i = 0; i < tips.length; i++){
            tips[i].style.visibility = "hidden";
        }
        currentTip = 0;
    }
