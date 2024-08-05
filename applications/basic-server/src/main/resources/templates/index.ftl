<#import "template.ftl" as layout />
<#import "BusDataCard.ftl" as card />
<#import "LandingPage.ftl" as landingPage/>

<!--<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                An example application using Kotlin and Ktor.
            </p>
        </div>
    </section>
</@layout.noauthentication>
-->
<html>
    <@landingPage.LandingPage/>
</html>