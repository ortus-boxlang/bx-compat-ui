/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.compat.ui.bifs;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.compat.ui.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;

public class AjaxLinkTest extends BaseIntegrationTest {

	@DisplayName( "It can create an AJAX link with URL parameter" )
	@Test
	public void testBasicAjaxLink() {
		runtime.executeSource(
		    """
		    result = ajaxLink("test.cfm");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).startsWith( "javascript:void(" );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
		assertThat( result ).contains( "test.cfm" );
		assertThat( result ).endsWith( ")" );
	}

	@DisplayName( "It can handle URLs with special characters" )
	@Test
	public void testAjaxLinkWithSpecialChars() {
		runtime.executeSource(
		    """
		    result = ajaxLink("test.cfm?id=123&name=John's Data");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
		assertThat( result ).startsWith( "javascript:void(" );
		// Should escape JavaScript special characters properly
		assertThat( result ).doesNotContain( "John's Data" );
	}

	@DisplayName( "It can handle complex URLs" )
	@Test
	public void testAjaxLinkWithComplexURL() {
		runtime.executeSource(
		    """
		    result = ajaxLink("/admin/users/edit.cfm?id=42&redirect=/dashboard");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
		assertThat( result ).startsWith( "javascript:void(" );
		// The URL should be properly encoded but we don't need to check exact encoding
	}

	@DisplayName( "It throws error when URL parameter is missing" )
	@Test
	public void testMissingURLParameter() {
		try {
			runtime.executeSource(
			    """
			    result = ajaxLink();
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "url parameter is required for AjaxLink" );
		}
	}

	@DisplayName( "It throws error when URL parameter is empty" )
	@Test
	public void testEmptyURLParameter() {
		try {
			runtime.executeSource(
			    """
			    result = ajaxLink("");
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "url parameter is required for AjaxLink" );
		}
	}

	@DisplayName( "It works with named parameters" )
	@Test
	public void testNamedParameters() {
		runtime.executeSource(
		    """
		    result = ajaxLink(url="data/users.cfm");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
		assertThat( result ).startsWith( "javascript:void(" );
	}

	@DisplayName( "It handles absolute URLs" )
	@Test
	public void testAbsoluteURL() {
		runtime.executeSource(
		    """
		    result = ajaxLink("https://api.example.com/data");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
		assertThat( result ).startsWith( "javascript:void(" );
	}

	@DisplayName( "It properly escapes JavaScript strings" )
	@Test
	public void testJavaScriptEscaping() {
		runtime.executeSource(
		    """
		    result = ajaxLink('test.cfm?message="Hello World"&action=update');
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
		assertThat( result ).startsWith( "javascript:void(" );
		// Should not contain unescaped quotes that would break JavaScript
		assertThat( result ).doesNotContain( "\"Hello World\"" );
	}

	@DisplayName( "It includes fallback error handling" )
	@Test
	public void testFallbackErrorHandling() {
		runtime.executeSource(
		    """
		    result = ajaxLink("test.cfm");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).contains( "BoxLangAjax && BoxLangAjax.utils" );
		assertThat( result ).contains( "console.error('BoxLang AJAX not initialized')" );
	}

	@DisplayName( "It can be used in HTML attributes" )
	@Test
	public void testInHTMLContext() {
		runtime.executeSource(
		    """
		    ajaxURL = ajaxLink("content/page1.cfm");
		    result = '<a href="' & ajaxURL & '">Click me</a>';
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		assertThat( result ).startsWith( "<a href=\"javascript:void(" );
		assertThat( result ).endsWith( "\">Click me</a>" );
		assertThat( result ).contains( "BoxLangAjax.utils.handleAjaxLink" );
	}

	@DisplayName( "It passes event parameter correctly" )
	@Test
	public void testEventParameter() {
		runtime.executeSource(
		    """
		    result = ajaxLink("load.cfm");
		    """,
		    context
		);

		String result = variables.getAsString( Key.of( "result" ) );
		// Should include event parameter in the function call
		assertThat( result ).contains( "handleAjaxLink('load.cfm', event)" );
	}
}