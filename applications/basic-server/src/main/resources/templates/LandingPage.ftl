<#import "BusDataCard.ftl" as card />
<#macro LandingPage title="LandingPage">
<head>
    <title>When's the Bus?</title>
    <link rel="stylesheet" type="text/css" href="/static/styles/indexStyle.css">
    <script src="index.js" type="application/javascript"></script>
</head>
<body>
    <h1>Bus Data Table</h1>
    <div>
        Current task is <em id="currentTaskDisplay">None</em>
    </div>
    <hr>
    <table border="1">
            <thead>
                <tr>
                    <th>Bus Data</th>
                </tr>
            </thead>
            <tbody>
                <#list busDataList as busData>
                    <tr>
                        <td>
                            <@card.DataCard busId="${busData.lineRef}" busInfo="Next Arrival: ${busData.arrivalTime} min<br> Capacity: ${busData.occupancy}" />
                        </td>
                    </tr>
                </#list>
            </tbody>
    </table>
</body>
</#macro>