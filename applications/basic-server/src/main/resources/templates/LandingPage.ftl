<#import "BusDataCard.ftl" as card>
<#macro LandingPage busDataList collectorUrl webUrl title="LandingPage">
<head>
    <title>Bus Arriving In</title>
    <link rel="stylesheet" type="text/css" href="/static/styles/landingPageStyle.css">
    <script src="/static/javascript/filterBusData.js" type="text/javascript"></script>
</head>
<body onload="init('${collectorUrl}', '${webUrl}')">
    <div class="title-header">
        <div class="title-banner">
            <h1>Bus Arriving In...</h1>
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
                        <button class="submit-filter-button" onclick="filterByLine()">Submit</button>
                    </div>
                    <hr>
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
                    <div class="dropdown-content-title-banner">
                        <p class="filter-title">Stops</p>
                        <button class="submit-filter-button" onclick="filterByStop()">Submit</button>
                    </div>
                    <hr>
                    <ul class="filter-list" id="stopsList">
                    </ul>
                </div>
            </div>
        </div>
        <div id="refreshButtonContainer">
            <button id="refreshButton" onclick="refresh()">
                Refresh
            </button>
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
                    <#list busDataList?sort_by("stopName")?sort_by("lineRef") as busData>
                        <tr data-stop-ref="${busData.stopRef?c}" data-line-ref="${busData.lineRef}" data-line-name="${busData.lineName}">
                            <td>${busData.lineRef} ${busData.lineName}</td>
                            <td>${busData.directionRef}</td>
                            <td>${busData.stopName}</td>
                            <td>${busData.occupancy}</td>
                            <td>
                                <#if busData.arrivalTime == "null">
                                    Data Unavailable
                                <#else> ${busData.arrivalTime} min
                                </#if>
                            </td>
                        </tr>
                    </#list>
                </tbody>
        </table>
    </div>  
</body>
</#macro>