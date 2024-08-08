<#import "BusDataCard.ftl" as card>
<#macro LandingPage busDataList title="LandingPage">
<head>
    <title>When's the Bus?</title>
    <link rel="stylesheet" type="text/css" href="/static/styles/indexStyle.css">
    <script src="/javascript/filterBusData.js" type="application/javascript"></script>
</head>
<body>
    <#list busDataList as busData>
        <script>
            insertData(busData)
        </script>
    </#list>
    <h1>Bus Data Table</h1>
    <div>
        Current task is <em id="currentTaskDisplay">None</em>
    </div>
    <hr>

<!--Filter dropdown-->
    <div>
        <select id="stopsDropdown" onchange="filterByStop()">
            <option value="">Stops</option>
            <#list busDataList as busData>
                <option value="${busData.stopRef}">${busData.stopName}</option>
            </#list>
        </select>
    </div>
<!--Filter dropdown-->

    <table border="1">
            <thead>
                <tr>Bus Data</tr>
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
                    <tr>
                        <td>${busData.lineRef}</td>
                        <td>${busData.directionRef}</td>
                        <td>${busData.stopName}</td>
                        <td>${busData.occupancy}</td>
                        <td>${busData.arrivalTime} min</td>
                    </tr>
                </#list>
            </tbody>
    </table>
</body>
</#macro>