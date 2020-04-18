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

var labels = [];

initializeNodes = () => d3.json("data.json", function (error, graph) {
    if (error) throw error;

    // Test data
    // nodes = [
    //     { "id": "Alice" },
    //     { "id": "Bob" },
    //     { "id": "Carol" },
    //     { "id": "James" }
    // ];

    // links = [
    //     { "source": "Alice", "target": "Bob" },
    //     { "source": "Bob", "target": "Carol" },
    //     { "source": "Alice", "target": "James" }
    // ];

    // Real data
    nodes = graph.nodes;
    links = graph.links;

    function initInputAreaNames() {
        var nodeNames = [];
        for (let i = 0; i < nodes.length; i++)
            nodeNames.push(nodes[i].id);
        autocomplete(document.getElementById("nodeInput"), nodeNames);
    }
    initInputAreaNames();
});

initializeNodes();

// function drawNodes() {
//     if (toBeFilteredNodes.length == 0) {
//         var confirmBox = confirm("Filter list is empty.\nDo you want to draw the entire data?");
//         if (!confirmBox)
//             return;
//         else {
//             var link = svg.append("g")
//                 .attr("class", "link")
//                 .selectAll("line")
//                 .data(links)
//                 .enter().append("line");

//             var node = svg.append("g")
//                 .attr("class", "node")
//                 .selectAll("circle")
//                 .data(nodes)
//                 .enter().append("circle")
//                 .attr("r", 7)
//                 .call(d3.drag()
//                     .on("start", dragstarted)
//                     .on("drag", dragged)
//                     .on("end", dragended));

//             node.append("title")
//                 .text(function (d) {
//                     finalString = d.id + "\n\nConnected to:\n";
//                     d3.selectAll("line").filter(function (line) {
//                         if (d.id == line.source) {
//                             finalString += line.target + "\n";
//                         }
//                     })
//                     return finalString;
//                 });

//             node.on("mouseover", mouseoverNode);
//             node.on("mouseout", mouseoutNode);
//             node.on("click", mouseclickNode);

//             simulation
//                 .nodes(nodes)
//                 .on("tick", ticked);

//             simulation.force("link")
//                 .links(links);

//             function ticked() {
//                 link
//                     .attr("x1", function (d) { return d.source.x; })
//                     .attr("y1", function (d) { return d.source.y; })
//                     .attr("x2", function (d) { return d.target.x; })
//                     .attr("y2", function (d) { return d.target.y; });

//                 node
//                     .attr("cx", function (d) { return d.x; })
//                     .attr("cy", function (d) { return d.y; });
//             }
//         }
//     } else {
//         applyFilterClicked();
//     }
// }

function removeNodes() {
    d3.selectAll("circle").remove();
    d3.selectAll("line").remove();
    resetArrays();
    initializeNodes();
    window.alert("Arrays are cleared!");

    console.log("Labels");
    console.log(labels);

    while (labels.length != 0) {
        labels.pop().remove();
    }
}

function resetArrays() {
    toBeFilteredNodes.length = 0;
    toBeFilteredLinks.length = 0;
    targetNodesOfFilteredNodes.length = 0;
    // labels.length = 0;
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
    document.getElementById("nodeInput").value = d.id;
}


// Add node algorithm
var listOfNodeNetworks = [];
function addNodeClicked() {
    var nodeName = document.getElementById("nodeInput").value;
    console.log(nodeName);
    if (!isCenterNode(nodeName)) {
        var relatedNodesToCenter = [];
        relatedNodesToCenter.push(nodeName);
        for (let i = 0; i < links.length; i++) {
            if (links[i].source == nodeName)
                relatedNodesToCenter.push(links[i].target);
        }
        listOfNodeNetworks.push(relatedNodesToCenter);
        addNodeLabelToList(nodeName);
    } else {
        window.alert(nodeName + " already exists as a center node.");
    }
    console.log(listOfNodeNetworks);
}

function isCenterNode(nodeName) {
    for (let i = 0; i < listOfNodeNetworks.length; i++) {
        if (listOfNodeNetworks[i][0] == nodeName)
            return true;
    }
    return false;
}

function drawNodesClicked() {
    if (listOfNodeNetworks != 0) {
        prepareCenterNodes();
        prepareLinks();
        prepareAddedNodesAsJson();
        drawNodesV2();
        recolorNodes();
    } else {
        window.alert("No center nodes detected.");
    }
}

var addedNodes = [];
function prepareCenterNodes() {
    for (let i = 0; i < listOfNodeNetworks.length; i++) {
        var currentNodeSet = listOfNodeNetworks[i];
        for (let j = 0; j < currentNodeSet.length; j++) {
            if (!addedNodes.includes(currentNodeSet[j]))
                addedNodes.push(currentNodeSet[j]);
        }
    }
}

var linksOfAddedNodes = [];
function prepareLinks() {
    for (let i = 0; i < addedNodes.length; i++) {
        var source = addedNodes[i];
        for (let j = 0; j < links.length; j++) {
            if (source == links[j].source && !linksOfAddedNodes.includes(links[j]) && addedNodes.includes(links[j].target)) {   // Potential bug
                linksOfAddedNodes.push(links[j]);
            }
        }
    }
    // console.log(linksOfAddedNodes);
}

var addedNodesAsJson = [];
function prepareAddedNodesAsJson() {
    for (let i = 0; i < addedNodes.length; i++) {
        for (let j = 0; j < nodes.length; j++) {
            if (addedNodes[i] == nodes[j].id && !addedNodesAsJson.includes(nodes[j])) {
                addedNodesAsJson.push(nodes[j]);
            }
        }
    }
}

function drawNodesV2() {
    var link = svg.append("g")
        .attr("class", "link")
        .selectAll("line")
        .data(linksOfAddedNodes)
        .enter().append("line");

    var node = svg.append("g")
        .attr("class", "node")
        .selectAll("circle")
        .data(addedNodesAsJson)
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

function recolorNodes() {
    for (let i = listOfNodeNetworks.length; i > 0; i--) {
        var currentList = listOfNodeNetworks[i - 1];
        var currentColor = labels[i - 1].style.color;
        for (let j = 0; j < currentList.length; j++) {
            d3.selectAll("circle").filter(function (node) {
                if (currentList[j] == node.id)
                    return node;
            }).style("fill", currentColor);
        }
    }
}

// // TODO check for bugs
// // function addNodeClicked() {
// //     if (addNodeIfNotExists()) {
// //         findTheLinks(); // Links of the added nodes of the previous function.
// //         addTargetNodes();
// //     } else
// //         window.alert("Node already exists!");
// // }

// function addNodeIfNotExists() {
//     var nodeName = document.getElementById("nodeInput").value;
//     var nodeAsJson = { "id": nodeName };
//     for (let i = 0; i < nodes.length; i++) {
//         if (JSON.stringify(nodes[i]) === JSON.stringify(nodeAsJson)) {
//             if (!existsInToBeFilteredNodes(nodes[i])) {
//                 toBeFilteredNodes.push(nodes[i]);
//                 addNodeLabelToList(nodeName);
//                 return true;
//             }
//         }
//     }
//     return false;
// }

// function existsInToBeFilteredNodes(node) {
//     for (let i = 0; i < toBeFilteredNodes.length; i++) {
//         if (JSON.stringify(toBeFilteredNodes[i]) === JSON.stringify(node))
//             return true;
//     }
//     return false;
// }

function addNodeLabelToList(nodeName) {
    var randomColor = randomColorGenerator();
    var node = document.createElement("p");
    var textnode = document.createTextNode(nodeName);
    node.appendChild(textnode);
    node.setAttribute("id", "labels");
    node.style.color = randomColor;
    document.getElementById("mySidenav").appendChild(node);
    labels.push(node);
}

// function findTheLinks() {
//     for (let i = 0; i < toBeFilteredNodes.length; i++) {
//         for (let j = 0; j < links.length; j++) {
//             if (toBeFilteredNodes[i].id == links[j].source) {
//                 var linkAsJson = links[j];
//                 if (!existsInToBeFilteredLinks(linkAsJson))
//                     toBeFilteredLinks.push(linkAsJson);
//             }
//         }
//     }
// }

// function existsInToBeFilteredLinks(link) {
//     for (let i = 0; i < toBeFilteredLinks.length; i++) {
//         if (JSON.stringify(toBeFilteredLinks[i]) === JSON.stringify(link))
//             return true;
//     }
//     return false;
// }

// // Add node button pressed
// function addTargetNodes() {
//     for (let i = 0; i < toBeFilteredLinks.length; i++) {
//         var targetNodeAsJson = { "id": toBeFilteredLinks[i].target };
//         console.log(targetNodeAsJson);
//         if (!existsInToBeFilteredNodes(targetNodeAsJson)) {
//             console.log(targetNodeAsJson + " does not exist");
//             toBeFilteredNodes.push(targetNodeAsJson);
//         }
//     }
// }

// // If toBeFiltered is not empty do this
// function applyFilterClicked() {
//     console.log("apply filter clicked");
//     var link = svg.append("g")
//         .attr("class", "link")
//         .selectAll("line")
//         .data(toBeFilteredLinks)
//         .enter().append("line");

//     var node = svg.append("g")
//         .attr("class", "node")
//         .selectAll("circle")
//         .data(toBeFilteredNodes)
//         .enter().append("circle")
//         .attr("r", 7)
//         .call(d3.drag()
//             .on("start", dragstarted)
//             .on("drag", dragged)
//             .on("end", dragended));

//     node.append("title")
//         .text(function (d) {
//             finalString = d.id + "\n\nConnected to:\n";
//             d3.selectAll("line").filter(function (line) {
//                 if (d.id == line.source) {
//                     finalString += line.target + "\n";
//                 }
//             })
//             return finalString;
//         });

//     node.on("mouseover", mouseoverNode);
//     node.on("mouseout", mouseoutNode);
//     node.on("click", mouseclickNode);

//     simulation
//         .nodes(toBeFilteredNodes)
//         .on("tick", ticked);

//     simulation.force("link")
//         .links(toBeFilteredLinks);

//     function ticked() {
//         link
//             .attr("x1", function (d) { return d.source.x; })
//             .attr("y1", function (d) { return d.source.y; })
//             .attr("x2", function (d) { return d.target.x; })
//             .attr("y2", function (d) { return d.target.y; });

//         node
//             .attr("cx", function (d) { return d.x; })
//             .attr("cy", function (d) { return d.y; });
//     }
//     applyCorrectColor();
// }

// function applyCorrectColor() {
//     var mainNodes = [];
//     for (let i = 0; i < labels.length; i++) {
//         var lastLabel = labels[labels.length - 1 - i];
//         d3.selectAll("circle").filter(function (node) {
//             if (node.id == lastLabel.innerHTML) {
//                 mainNodes.push(node);
//                 return node;
//             }
//         }).style("fill", lastLabel.style.color);
//     }

//     for (let i = 0; i < mainNodes.length; i++) {
//         d3.selectAll("line").filter(function (line) {
//             if (mainNodes[i].id == line.source.id) {
//                 d3.selectAll("circle").filter(function (node) {
//                     if (node.id == line.target.id) {
//                         return node;
//                     }
//                 }).style("fill", function (node) {
//                     for (let j = 0; j < labels.length; j++) {
//                         if (labels[j].innerHTML === mainNodes[i].id)
//                             return labels[j].style.color;
//                     }
//                 });
//             }
//         });
//     }
// }

function randomColorGenerator() {
    var colorOptions = "0123456789ABCDEF";
    var color = "#";
    for (let i = 0; i < 6; i++) {
        randomNum = Math.floor(Math.random() * colorOptions.length);
        color += colorOptions.charAt(randomNum);
    }
    return color;
}


// ###################################
// Script for autocomplete below
function autocomplete(inp, arr) {
    /*the autocomplete function takes two arguments,
    the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) { return false; }
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        for (i = 0; i < arr.length; i++) {
            if (arr[i].includes(val)) {
                b = document.createElement("DIV");
                /*make the matching letters bold:*/
                var substrIndex = arr[i].indexOf(val);
                b.innerHTML = arr[i].substr(0, substrIndex);
                b.innerHTML += "<strong>" + arr[i].substr(substrIndex, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(substrIndex + val.length);
                /*insert a input field that will hold the current array item's value:*/
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function (e) {
                    /*insert the value for the autocomplete text field:*/
                    inp.value = this.getElementsByTagName("input")[0].value;
                    /*close the list of autocompleted values,
                    (or any other open lists of autocompleted values:*/
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
            /*If the arrow DOWN key is pressed,
            increase the currentFocus variable:*/
            currentFocus++;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 38) { //up
            /*If the arrow UP key is pressed,
            decrease the currentFocus variable:*/
            currentFocus--;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (x) x[currentFocus].click();
            }
        }
    });
    function addActive(x) {
        /*a function to classify an item as "active":*/
        if (!x) return false;
        /*start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        /*add class "autocomplete-active":*/
        x[currentFocus].classList.add("autocomplete-active");
    }
    function removeActive(x) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }
    function closeAllLists(elmnt) {
        /*close all autocomplete lists in the document,
        except the one passed as an argument:*/
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    /*execute a function when someone clicks in the document:*/
    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}
