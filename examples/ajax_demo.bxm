<!DOCTYPE html>
<html>
<head>
    <title>BoxLang AJAX Components Demo</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .demo-section { border: 1px solid #ccc; margin: 20px 0; padding: 15px; }
        .demo-section h2 { margin-top: 0; color: #333; }
        .result { background: #f0f0f0; padding: 10px; margin: 10px 0; border-left: 4px solid #007cba; }
        .container { border: 2px dashed #007cba; padding: 20px; min-height: 100px; }
        .bx-loading { color: #666; font-style: italic; }
        .bx-error { color: red; }
    </style>
</head>
<body>
    <h1>BoxLang AJAX Components and BIFs Demo</h1>

    <div class="demo-section">
        <h2>1. AjaxImport Component</h2>
        <p>This imports the necessary CSS and JavaScript files for AJAX functionality:</p>
        <div class="result">
            <bx:ajaximport tags="layout,div,grid" />
        </div>
        <p><strong>Expected:</strong> CSS and JavaScript import statements should be generated above.</p>
    </div>

    <div class="demo-section">
        <h2>2. AjaxProxy Component - CFC Proxy</h2>
        <p>Creates a JavaScript proxy for server-side components:</p>
        <div class="result">
            <bx:ajaxproxy cfc="examples.UserService" jsclassname="UserProxy" />
        </div>
        <p><strong>Expected:</strong> JavaScript class definition for UserProxy should be generated above.</p>
    </div>

    <div class="demo-section">
        <h2>3. AjaxProxy Component - Bind Expression</h2>
        <p>Executes a bind expression on page load:</p>
        <div class="result">
            <bx:ajaxproxy bind="cfc:examples.DataService.loadInitialData()" onSuccess="handleDataLoad" onError="handleError" />
        </div>
        <p><strong>Expected:</strong> JavaScript code to execute the bind expression should be generated above.</p>
    </div>

    <div class="demo-section">
        <h2>4. AjaxLink BIF</h2>
        <p>Creates links that load content into AJAX containers:</p>
        <div class="result">
            <cfset ajaxURL = ajaxLink("content/sample.cfm") />
            <p>Generated URL: <code><cfoutput>#ajaxURL#</cfoutput></code></p>
            <div class="container" id="linkContainer">
                <p>Click the link below to load content here:</p>
                <a href="<cfoutput>#ajaxURL#</cfoutput>">Load Sample Content</a>
            </div>
        </div>
        <p><strong>Expected:</strong> JavaScript URL starting with "javascript:void(" should be generated.</p>
    </div>

    <div class="demo-section">
        <h2>5. AjaxOnLoad BIF</h2>
        <p>Registers a JavaScript function to run when the page loads:</p>
        <div class="result">
            <script type="text/javascript">
                function initializePage() {
                    console.log("Page initialized via AjaxOnLoad!");
                    document.getElementById('onLoadResult').innerHTML = 'AjaxOnLoad function executed successfully!';
                }
            </script>
            <cfset ajaxOnLoad("initializePage") />
            <div id="onLoadResult">Waiting for AjaxOnLoad...</div>
        </div>
        <p><strong>Expected:</strong> The div above should show "AjaxOnLoad function executed successfully!" when the page loads.</p>
    </div>

    <div class="demo-section">
        <h2>6. Combined Example - Layout with AJAX Content</h2>
        <p>Demonstrates how the components work together:</p>
        <div class="result">
            <bx:layout type="tab" id="ajaxTabDemo">
                <bx:layoutarea title="Static Content">
                    <p>This is static content in the first tab.</p>
                </bx:layoutarea>
                <bx:layoutarea title="AJAX Content">
                    <div id="ajaxContent">
                        <p>Load content dynamically:</p>
                        <cfset dynamicURL = ajaxLink("content/dynamic.cfm") />
                        <a href="<cfoutput>#dynamicURL#</cfoutput>">Load Dynamic Content</a>
                    </div>
                </bx:layoutarea>
            </bx:layout>
        </div>
    </div>

    <script type="text/javascript">
        function handleDataLoad(data) {
            console.log("Data loaded successfully:", data);
        }
        
        function handleError(errorCode, errorMessage) {
            console.error("AJAX Error:", errorCode, errorMessage);
        }

        // Demonstrate the BoxLang AJAX utilities
        if (typeof BoxLangAjax !== 'undefined') {
            console.log("BoxLang AJAX initialized:", BoxLangAjax.version);
        } else {
            console.warn("BoxLang AJAX not initialized - check AjaxImport component");
        }
    </script>
</body>
</html>