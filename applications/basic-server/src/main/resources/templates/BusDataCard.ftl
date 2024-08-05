<#macro DataCard busId busInfo title="DataCard">
    <html lang="en">
    <head>
        <link rel="stylesheet" href="/static/styles/BusDataCardStyle.css">
    </head>
    <body>
        <div class="busCard">
            <span id="busID" class="busId vert-align">${busId}</span>
            <aside id="busInfo" class="info vert-align">
              ${busInfo}
            </aside>
        </div>
    </body>
</#macro>