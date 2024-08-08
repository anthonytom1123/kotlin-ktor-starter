<#macro DataCard busId busInfo directionRef title="DataCard">
    <html lang="en">
    <head>
        <link rel="stylesheet" href="/static/styles/BusDataCardStyle.css">
    </head>
    <body>
        <div class="busCard">
            <span id="busID" class="busId vert-align">${busId} ${directionRef}</span>
            <aside id="busInfo" class="info vert-align">
              ${busInfo}
            </aside>
        </div>
    </body>
</#macro>