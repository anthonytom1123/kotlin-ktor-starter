<#import "BusDataCard.ftl" as card>
<#macro LandingPage busDataList title="LandingPage">
<head>
    <title>When's the Bus?</title>
    <link rel="stylesheet" type="text/css" href="/static/styles/landingPageStyle.css">
    <script src="/static/javascript/filterBusData.js" type="text/javascript"></script>
</head>
<body onload="init()">
    <div class="title-header">
        <div class="title-banner">
            <h1>Bus Data Table</h1>
        </div>
        <div class="source">
            powered by <a href="http://www.511.org/">511.org</a>
        </div>
    </div>
    <hr>
    <div class="filter-banner">
        <div class="filter" id="linesFilter">
            <div class="filter-dropdown-button-container">
                <button class="filter-dropdown-button" id="linesDropdownButton" onclick="toggleFilterDisplay('linesDropdownContentContainer')">Select Bus Line</button>
            </div>
            <div class="dropdown-content-container" id="linesDropdownContentContainer">
                <div class="dropdown-content" id="linesDropdownContent">
                    <div class="dropdown-content-title-banner">
                        <p class="filter-title">Bus Lines</p>
                        <button>Test</button>
                        <hr>
                    </div>
                    <ul class="filter-list" id="linesList">
                    </ul>
                </div>
            </div>
        </div>
        <div class="filter" id="stopsFilter">
            <div class="filter-dropdown-button-container">
                <button class="filter-dropdown-button" id="stopDropdownButton" onclick="toggleFilterDisplay('stopsDropdownContentContainer')">Select Stops</button>
            </div>
            <div class="dropdown-content-container" id="stopsDropdownContentContainer">
                <div class="dropdown-content" id="stopsDropdownContent">
                    <p class="filter-title">Stops</p>
                    <hr>
                    <ul class="filter-list" id="stopsList">
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="bus-data-table">
        <table id="busDataTable">
                <thead>
                    <tr>
                        <th>Line</th>
                        <th>Direction</th>
                        <th>Stop</th>
                        <th>Occupancy</th>
                        <th>Arrival Time</th>
                    </tr>
                </thead>
                <tbody>
                    <#list busDataList as busData>
                        <tr data-stop-ref="${busData.stopRef?c}" data-line-ref = "${busData.lineRef}" data-line-name="${busData.lineName}">
                            <td>${busData.lineRef} ${busData.lineName}</td>
                            <td>${busData.directionRef}</td>
                            <td>${busData.stopName}</td>
                            <td>${busData.occupancy}</td>
                            <td>${busData.arrivalTime} min</td>
                        </tr>
                    </#list>
                </tbody>
        </table>
    </div>  
</body>
</#macro>