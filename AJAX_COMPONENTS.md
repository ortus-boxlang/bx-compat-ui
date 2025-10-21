# BoxLang AJAX Components and BIFs

This document describes the AJAX components and Built-In Functions (BIFs) implemented for BoxLang compatibility with Adobe ColdFusion's AJAX features.

## Components

### bx:ajaxproxy

Creates JavaScript proxies for server-side components and executes bind expressions using the Fetch API.

#### Attributes

- `cfc` (string, optional) - The CFC for which to create a proxy (dot-delimited path)
- `jsclassname` (string, optional) - Name for the JavaScript proxy class (defaults to CFC name)
- `bind` (string, optional) - Bind expression for CFC method, JavaScript function, or URL
- `onError` (string, optional) - JavaScript function to execute if bind fails
- `onSuccess` (string, optional) - JavaScript function to execute if bind succeeds

#### Examples

```html
<!-- Create a JavaScript proxy for a CFC -->
<bx:ajaxproxy cfc="myapp.components.UserService" jsclassname="UserProxy" />

<!-- Execute a bind expression on page load -->
<bx:ajaxproxy bind="cfc:mycomponent.getData(param1,param2)" 
              onSuccess="handleSuccess" 
              onError="handleError" />
```

#### Generated JavaScript

The component generates JavaScript classes with async methods using the Fetch API:

```javascript
class UserProxy {
    constructor() {
        this.cfcPath = 'myapp.components.UserService';
    }
    
    async callMethod(methodName, args = {}) {
        // Fetch API implementation
    }
    
    call(methodName, args = {}, callback = null) {
        // Convenience wrapper
    }
}
```

### bx:ajaximport

Imports JavaScript and CSS files required for BoxLang AJAX tags and features.

#### Attributes

- `tags` (string, optional) - Comma-delimited list of BoxLang AJAX tags for which to import supporting files
- `cssSrc` (string, optional) - URL of the directory containing CSS files (default: `/bx-compat-ui/css`)
- `scriptSrc` (string, optional) - URL of the directory containing JavaScript files (default: `/bx-compat-ui/js`)
- `params` (string, optional) - Parameters to pass, such as API keys (e.g., `googlemapkey=ABC123`)

#### Examples

```html
<!-- Import resources for specific tags -->
<bx:ajaximport tags="layout,div,grid" />

<!-- Use custom resource locations -->
<bx:ajaximport cssSrc="/custom/css" scriptSrc="/custom/js" />

<!-- Include parameters -->
<bx:ajaximport params="googlemapkey=ABC123,apikey=XYZ789" />
```

#### Generated Resources

The component generates:
- CSS link tags for core and tag-specific stylesheets
- JavaScript script tags for core and tag-specific functionality
- BoxLang AJAX namespace initialization
- Fetch API utility functions
- Container management functions

## Built-In Functions (BIFs)

### ajaxLink(url)

Generates a URL that causes link results to display within the current AJAX container rather than replacing the current page content.

#### Parameters

- `url` (string, required) - The URL to load when the link is clicked

#### Return Value

Returns a JavaScript URL that handles AJAX loading into the nearest suitable container.

#### Examples

```html
<cfset ajaxURL = ajaxLink("content/page1.cfm") />
<a href="#ajaxURL#">Load Content</a>

<!-- In a pod or layout -->
<cfpod name="myPod">
    <a href="#ajaxLink('data.cfm')#">Load Data</a>
</cfpod>
```

#### Generated JavaScript

```javascript
javascript:void(BoxLangAjax && BoxLangAjax.utils ? 
    BoxLangAjax.utils.handleAjaxLink('content/page1.cfm', event) : 
    console.error('BoxLang AJAX not initialized'))
```

### ajaxOnLoad(functionName)

Specifies a JavaScript function to run when a page loads in the browser.

#### Parameters

- `functionName` (string, required) - The name of the JavaScript function to execute on page load

#### Return Value

Returns an empty string (outputs JavaScript to the page buffer).

#### Examples

```html
<script>
function initializePage() {
    console.log("Page loaded!");
}
</script>

<cfset ajaxOnLoad("initializePage") />
```

#### Generated JavaScript

```javascript
<script type="text/javascript">
(function() {
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            if (typeof initializePage === 'function') {
                initializePage();
            } else {
                console.error('Function initializePage is not defined');
            }
        });
    } else {
        if (typeof initializePage === 'function') {
            initializePage();
        } else {
            console.error('Function initializePage is not defined');
        }
    }
})();
</script>
```

## BoxLang AJAX Namespace

The `bx:ajaximport` component creates a global `BoxLangAjax` namespace with utility functions:

### BoxLangAjax.utils.fetchContent(url, options)

Wrapper around the Fetch API for consistent AJAX handling.

### BoxLangAjax.utils.loadIntoContainer(containerId, url, options)

Loads content from a URL into a specific container element.

### BoxLangAjax.utils.handleAjaxLink(url, event)

Handles AJAX link clicks by finding the nearest suitable container and loading content.

## Integration with Existing Components

These AJAX components work seamlessly with existing BoxLang UI components:

- **Layout Components** - AJAX links within layouts load content into layout areas
- **Div Components** - Can serve as AJAX content containers
- **Pod Components** - Natural containers for AJAX-loaded content
- **Grid Components** - Can be populated with AJAX data

## Browser Compatibility

The implementation uses modern JavaScript features:
- Fetch API (supported in all modern browsers)
- ES6 Classes and arrow functions
- Async/await syntax
- CustomEvents API

For older browser support, consider including appropriate polyfills.

## Security Considerations

- All user input is properly escaped using `encodeForJavaScript()` and `encodeForHTML()`
- CSRF protection should be implemented at the application level
- Validate all server-side endpoints that accept AJAX requests
- Use HTTPS for sensitive data transmission

## Error Handling

The implementation includes comprehensive error handling:
- Network errors are caught and logged
- Invalid function names are validated
- Missing containers trigger appropriate fallbacks
- Error callbacks are supported for custom error handling

## Performance Considerations

- Resources are loaded only when needed based on `tags` attribute
- JavaScript classes are instantiated once and reused
- Fetch requests include appropriate headers for caching
- Loading indicators provide user feedback during operations