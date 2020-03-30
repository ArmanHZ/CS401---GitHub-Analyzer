// dimensions
var width = window.innerWidth;
var height = window.innerHeight;

var margin = {
    top: 50,
    bottom: 50,
    left: 50,
    right: 50,
}

// create an svg to draw in
var svg = d3.select("body")
    .append("svg")
    .attr("width", width)
    .attr("height", height)
    .attr('transform', 'translate(' + margin.top + ',' + margin.left + ')')
    .call(d3.zoom().on("zoom", function () {
        svg.attr("transform", d3.event.transform)
    }))
    .append("g");

width = width - margin.left - margin.right;
height = height - margin.top - margin.bottom;


var simulation = d3.forceSimulation()
    // pull nodes together based on the links between them
    .force("link", d3.forceLink()
        .id(function (d) {
            return d.id;
        }))
    // push nodes apart to space them out
    .force("charge", d3.forceManyBody().strength(-200))
    // add some collision detection so they don't overlap
    .force("collide", d3.forceCollide().radius(30))
    // and draw them around the centre of the space
    .force("center", d3.forceCenter(width / 2, height / 2));

let nodes;
let links;
var toBeFilteredNodes = [];
var toBeFilteredLinks = [];
var targetNodesOfFilteredNodes = [];

initializeNodes = () => d3.json("data.json", function (error, graph) {
    if (error) throw error;

    nodes = [
        { "id": "Alice" },
        { "id": "Bob" },
        { "id": "Carol" },
        { "id": "James" }
    ];

    links = [
        { "source": "Alice", "target": "Bob" },
        { "source": "Bob", "target": "Carol" },
        { "source": "Alice", "target": "James" }
    ];

    // nodes = graph.nodes;
    // links = graph.links;
});

initializeNodes();

function drawNodes() {
    if (toBeFilteredNodes.length == 0) {
        var confirmBox = confirm("Filter list is empty.\nDo you want to draw the entire data?");
        if (!confirmBox)
            return;
        else {
            var link = svg.append("g")
                .attr("class", "link")
                .selectAll("line")
                .data(links)
                .enter().append("line");

            var node = svg.append("g")
                .attr("class", "node")
                .selectAll("circle")
                .data(nodes)
                .enter().append("circle")
                .attr("r", 7)
                .call(d3.drag()
                    .on("start", dragstarted)
                    .on("drag", dragged)
                    .on("end", dragended));

            node.append("title")
                .text(function (d) {
                    finalString = d.id + "\n\nConnected to:\n";
                    d3.selectAll("line").filter(function (line) {
                        if (d.id == line.source) {
                            finalString += line.target + "\n";
                        }
                    })
                    return finalString;
                });

            node.on("mouseover", mouseoverNode);
            node.on("mouseout", mouseoutNode);
            node.on("click", mouseclickNode);

            simulation
                .nodes(nodes)
                .on("tick", ticked);

            simulation.force("link")
                .links(links);

            function ticked() {
                link
                    .attr("x1", function (d) { return d.source.x; })
                    .attr("y1", function (d) { return d.source.y; })
                    .attr("x2", function (d) { return d.target.x; })
                    .attr("y2", function (d) { return d.target.y; });

                node
                    .attr("cx", function (d) { return d.x; })
                    .attr("cy", function (d) { return d.y; });
            }
        }
    } else {
        applyFilterClicked();
    }
}

function removeNodes() {
    d3.selectAll("circle").remove();
    d3.selectAll("line").remove();
    toBeFilteredNodes.length = 0;
    toBeFilteredLinks.length = 0;
    targetNodesOfFilteredNodes.length = 0;
    initializeNodes();
    window.alert("Arrays cleared!");
}

function dragstarted(d) {
    if (!d3.event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x;
    d.fy = d.y;
}

function dragged(d) {
    d.fx = d3.event.x;
    d.fy = d3.event.y;
}

function dragended(d) {
    if (!d3.event.active) simulation.alphaTarget(0);
    d.fx = null;
    d.fy = null;
}

function mouseoverNode(d) {
    if (d3.event.shiftKey) {
        var targetNodes = [d.id];
        var targetLines = [];
        d3.selectAll("line").filter(function (line) {
            if (line.source.id == d.id) {
                targetNodes.push(line.target.id);
                targetLines.push(line);
            }
        });
        d3.selectAll("circle").filter(function (node) {
            if (!targetNodes.includes(node.id))
                return node.id;
        }).style("opacity", "0");

        d3.selectAll("line").filter(function (line) {
            if (!targetLines.includes(line))
                return line;
        }).style("opacity", "0");
    }
}

function mouseoutNode(d) {
    d3.selectAll("circle").style("opacity", "100");
    d3.selectAll("line").style("opacity", "100");
}

function mouseclickNode(d) {
    document.getElementById("nodeInputArea").value = d.id;
}

function addNodeClicked() {
    addNodeIfNotExists();
    findTheLinks(); // Links of the added nodes of the previous function.
    addTargetNodes();

    // console.log(toBeFilteredNodes);
    // console.log(toBeFilteredLinks);
}

function addNodeIfNotExists() {
    var nodeName = document.getElementById("nodeInputArea").value;
    var nodeAsJson = { "id": nodeName };
    for (let i = 0; i < nodes.length; i++) {
        if (JSON.stringify(nodes[i]) === JSON.stringify(nodeAsJson)) {
            if (!existsInToBeFilteredNodes(nodes[i]))
                toBeFilteredNodes.push(nodes[i]);
        }
    }
}

function existsInToBeFilteredNodes(node) {
    for (let i = 0; i < toBeFilteredNodes.length; i++) {
        if (JSON.stringify(toBeFilteredNodes[i]) === JSON.stringify(node))
            return true;
    }
    return false;
}

function findTheLinks() {
    for (let i = 0; i < toBeFilteredNodes.length; i++) {
        for (let j = 0; j < links.length; j++) {
            if (toBeFilteredNodes[i].id == links[j].source) {
                var linkAsJson = links[j];
                if (!existsInToBeFilteredLinks(linkAsJson))
                    toBeFilteredLinks.push(linkAsJson);
            }
        }
    }
}

function existsInToBeFilteredLinks(link) {
    for (let i = 0; i < toBeFilteredLinks.length; i++) {
        if (JSON.stringify(toBeFilteredLinks[i]) === JSON.stringify(link))
            return true;
    }
    return false;
}

function addTargetNodes() {
    for (let i = 0; i < toBeFilteredLinks.length; i++) {
        var targetNodeAsJson = { "id": toBeFilteredLinks[i].target };
        console.log(targetNodeAsJson);
        if (!existsInToBeFilteredNodes(targetNodeAsJson)) {
            console.log(targetNodeAsJson + " does not exist");
            toBeFilteredNodes.push(targetNodeAsJson);
        }
    }
}

function applyFilterClicked() {
    var link = svg.append("g")
        .attr("class", "link")
        .selectAll("line")
        .data(toBeFilteredLinks)
        .enter().append("line");

    var node = svg.append("g")
        .attr("class", "node")
        .selectAll("circle")
        .data(toBeFilteredNodes)
        .enter().append("circle")
        .attr("r", 7)
        .call(d3.drag()
            .on("start", dragstarted)
            .on("drag", dragged)
            .on("end", dragended));

    node.append("title")
        .text(function (d) {
            finalString = d.id + "\n\nConnected to:\n";
            d3.selectAll("line").filter(function (line) {
                if (d.id == line.source) {
                    finalString += line.target + "\n";
                }
            })
            return finalString;
        });

    node.on("mouseover", mouseoverNode);
    node.on("mouseout", mouseoutNode);
    node.on("click", mouseclickNode);

    simulation
        .nodes(toBeFilteredNodes)
        .on("tick", ticked);

    simulation.force("link")
        .links(toBeFilteredLinks);

    function ticked() {
        link
            .attr("x1", function (d) { return d.source.x; })
            .attr("y1", function (d) { return d.source.y; })
            .attr("x2", function (d) { return d.target.x; })
            .attr("y2", function (d) { return d.target.y; });

        node
            .attr("cx", function (d) { return d.x; })
            .attr("cy", function (d) { return d.y; });
    }
}
