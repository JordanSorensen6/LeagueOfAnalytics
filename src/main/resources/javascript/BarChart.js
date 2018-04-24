class BarChart{

    /**
     * Load the file indicated by the select menu
     */
    function changeData() {
        let dataFile = document.getElementById('dataset').value;
        if (document.getElementById('random').checked) {
            randomSubset();
        }
        else {
            d3.csv('data/' + dataFile + '.csv', update);
        }
    }

    /**
     * Render the visualizations
     * @param error
     * @param data
     */
    function update(error, data) {
        if (error !== null) {
            alert('Could not load the dataset!');
        } else {
            // D3 loads all CSV data as strings;
            // while Javascript is pretty smart
            // about interpreting strings as
            // numbers when you do things like
            // multiplication, it will still
            // treat them as strings where it makes
            // sense (e.g. adding strings will
            // concatenate them, not add the values
            // together, or comparing strings
            // will do string comparison, not
            // numeric comparison).

            // We need to explicitly convert values
            // to numbers so that comparisons work
            // when we call d3.max()

            for (let d of data) {
                d.a = +d.a;
                d.b = +d.b;
            }
        }

        // Set up the scales
        let aScale = d3.scaleLinear()
            .domain([0, d3.max(data, d => d.a)])
    .range([0, 150]);
        let bScale = d3.scaleLinear()
            .domain([0, d3.max(data, d => d.b)])
    .range([0, 150]);
        let iScale = d3.scaleLinear()
            .domain([0, data.length])
            .range([0, 110]);


        // ****** TODO: PART III (you will also edit in PART V) ******

        // TODO: Select and update the 'a' bar chart bars
        var barChart1 = d3.select("#barChart1").selectAll("rect")
            .data(data);


        barChart1.exit()
            .attr("opacity", 1)
            .transition()
            .duration(3000)
            .attr("opacity", 0)
            .remove();

        barChart1 = barChart1.enter().append("rect")
            .merge(barChart1);

        barChart1
            .transition()
            .duration(3000)
            .attr("height", function(d, i){
                return aScale(d.a);
            })
            .attr("width", 10)
            .attr("x",function(d, i){
                return iScale(i);
            })
            .attr("y", 0)
            .attr("fill", "steelblue");

        // TODO: Select and update the 'b' bar chart bars
        var barChart2 = d3.select("#barChart2").selectAll("rect")
            .data(data)
            .attr("height", function(d, i){
                return bScale(d.b);
            })
            .attr("x",function(d, i){
                return iScale(i);
            });

        barChart2.exit()
            .attr("opacity", 1)
            .transition()
            .duration(3000)
            .attr("opacity", 0)
            .remove();

        barChart2 = barChart2.enter().append("rect")
            .merge(barChart2);

        barChart2
            .transition()
            .duration(3000)
            .attr("height", function(d, i){
                return bScale(d.b);
            })
            .attr("width", 10)
            .attr("x",function(d, i){
                return iScale(i);
            })
            .attr("y", 0)
            .attr("fill", "steelblue");

        // TODO: Select and update the 'a' line chart path using this line generator

        let aLineGenerator = d3.line()
            .x((d, i) => iScale(i))
    .y((d) => aScale(d.a));

        var lineChart1 = d3.select("#line1")
            .transition()
            .duration(3000)
            .attr("d", aLineGenerator(data));

        // TODO: Select and update the 'b' line chart path (create your own generator)
        let bLineGenerator = d3.line()
            .x((d, i) => iScale(i))
    .y((d) => bScale(d.b));

        var lineChart2 = d3.select("#line2")
            .transition()
            .duration(3000)
            .attr("d", bLineGenerator(data));

        // TODO: Select and update the 'a' area chart path using this area generator
        let aAreaGenerator = d3.area()
            .x((d, i) => iScale(i))
    .y0(0)
            .y1(d => aScale(d.a));

        var areaChart1 = d3.select("#area1")
            .transition()
            .duration(3000)
            .attr("d", aAreaGenerator(data));

        // TODO: Select and update the 'b' area chart path (create your own generator)
        let bAreaGenerator = d3.area()
            .x((d, i) => iScale(i))
    .y0(0)
            .y1(d => bScale(d.b));

        var areaChart2 = d3.select("#area2")
            .transition()
            .duration(3000)
            .attr("d", bAreaGenerator(data));

        // TODO: Select and update the scatterplot points
        var scatter = d3.select("#scatter").selectAll("circle")
            .data(data);


        scatter.exit()
            .attr("opacity", 1)
            .transition()
            .duration(3000)
            .attr("opacity", 0)
            .remove();

        scatter = scatter.enter().append("circle")
            .merge(scatter);

        scatter
            .transition()
            .duration(3000)
            .attr("cx", function(d, i){
                return aScale(d.a);
            })
            .attr("cy", function(d, i){
                return bScale(d.b);
            })
            .attr("r", 5)
            .style("fill", "steelblue");

        // ****** TODO: PART IV ******

        d3.selectAll("rect")
            .on("mouseover", function(){
                d3.select(this)
                    .attr("fill", "orange");
            })
            .on("mouseout", function(d, i) {
                d3.select(this)
                    .attr("fill", "steelblue");
            });

        var tooltip = d3.select("body").append("div")
            .attr("class", "tooltip")
            .attr("opacity", 0);
        d3.selectAll("circle")
            .on("mouseover", function(){

                tooltip.transition()
                    .duration(300)
                    .attr("border", "0px")
                    .attr("opacity", .9);
                tooltip.html("x: " + d3.select(this).attr("cx") + " " + "y: " + d3.select(this).attr("cy"));
            })
            .on("mouseout", function(){
                tooltip.transition()
                    .duration(400)
                    .attr("opacity", 0);
            });

    }
    // constructor(data){
    //     this.data = data;
    // }
    //
    // updateChart(){
    //     let padding = 60;
    //     let width = 500 - 2 * padding;
    //     let height = 400 - 2 * padding;
    //
    //     let maxScore = d3.max(this.data, function (d){
    //         return d["score"];
    //     });
    //     let minScore = d3.min(this.data, function (d){
    //         return d["score"];
    //     });
    //
    //     let maxPercentage = d3.max(this.data, function (d){
    //         return d["percentage"];
    //     });
    //     let minPercentage = d3.min(this.data, function (d){
    //         return d["percentage"];
    //     });
    //
    //     let xScale = d3.scaleLinear()
    //         .domain([minScore, maxScore])
    //         .range([0, width]);
    //     let xAxis = d3.axisBottom();
    //     xAxis.scale(xScale);
    //     d3.select('#xAxis')
    //         .classed("axis", true)
    //         .attr("transform", "translate(" + padding + "," + (height + padding) + ")")
    //         .call(xAxis);
    //
    //     //let yScale =
    // }
}