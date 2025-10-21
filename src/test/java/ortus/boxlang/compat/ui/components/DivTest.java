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
package ortus.boxlang.compat.ui.components;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.compat.ui.BaseIntegrationTest;
import ortus.boxlang.runtime.scopes.Key;

public class DivTest extends BaseIntegrationTest {

	@DisplayName( "It can create a basic div with content" )
	@Test
	public void testBasicDiv() {
		runtime.executeSource(
		    """
		    bx:div id="myDiv" {
		        writeOutput("Div content here");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-div" );
		assertThat( output ).contains( "id=\"myDiv\"" );
		assertThat( output ).contains( "Div content here" );
		assertThat( output ).contains( "<div" );
		assertThat( output ).contains( "</div>" );
	}

	@DisplayName( "It can create div with bind attribute" )
	@Test
	public void testDivWithBind() {
		runtime.executeSource(
		    """
		    bx:div bind="cfc:myComponent.getData" bindOnLoad="true" {
		        // This content should be ignored when bind is specified
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-div" );
		assertThat( output ).contains( "bx-div-bind" );
		assertThat( output ).contains( "bx-bind-on-load" );
		assertThat( output ).contains( "data-bind=\"cfc:myComponent.getData\"" );
		assertThat( output ).contains( "data-bind-on-load=\"true\"" );
		assertThat( output ).contains( "bx-bind-loading" );
	}

	@DisplayName( "It can create div with bind but no auto-load" )
	@Test
	public void testDivWithBindNoAutoLoad() {
		runtime.executeSource(
		    """
		    bx:div bind="url:/api/data" bindOnLoad="false" {
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-div-bind" );
		assertThat( output ).contains( "data-bind-on-load=\"false\"" );
		assertThat( output ).doesNotContain( "bx-bind-on-load" );
		assertThat( output ).doesNotContain( "bx-bind-loading" );
	}

	@DisplayName( "It throws error when bind and body content coexist" )
	@Test
	public void testDivBindWithBodyContentError() {
		try {
			runtime.executeSource(
			    """
			    bx:div bind="cfc:test.getData" {
			        writeOutput("This should cause an error");
			    }
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "cannot have body content when the bind attribute is specified" );
		}
	}

	@DisplayName( "It can use different tag names" )
	@Test
	public void testDivWithDifferentTagNames() {
		runtime.executeSource(
		    """
		    bx:div tagName="span" {
		        writeOutput("Span content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "<span" );
		assertThat( output ).contains( "</span>" );
		assertThat( output ).contains( "bx-div" );
		assertThat( output ).contains( "Span content" );
	}

	@DisplayName( "It throws error for invalid tag name" )
	@Test
	public void testInvalidTagName() {
		try {
			runtime.executeSource(
			    """
			    bx:div tagName="invalid" {
			        writeOutput("Test content");
			    }
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "tagName attribute must be one of" );
		}
	}

	@DisplayName( "It generates JavaScript for bind functionality" )
	@Test
	public void testDivBindJavaScript() {
		runtime.executeSource(
		    """
		    bx:div bind="cfc:myComponent.getData" onBindError="handleError" {
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "<script type=\"text/javascript\">" );
		assertThat( output ).contains( "executeBind" );
		assertThat( output ).contains( "fetch(" );
		assertThat( output ).contains( "handleError" );
		assertThat( output ).contains( "bindSuccess" );
		assertThat( output ).contains( "bindError" );
	}

	@DisplayName( "It auto-generates ID when not provided" )
	@Test
	public void testDivAutoGeneratedID() {
		runtime.executeSource(
		    """
		    bx:div {
		        writeOutput("Auto ID content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "id=\"div_" );
		assertThat( output ).contains( "Auto ID content" );
	}

	@DisplayName( "It can apply custom CSS classes and styles" )
	@Test
	public void testDivCustomStyling() {
		runtime.executeSource(
		    """
		    bx:div
		        class="my-custom-class another-class"
		        style="background-color: blue; padding: 10px;" {
		        writeOutput("Styled content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-div my-custom-class another-class" );
		assertThat( output ).contains( "background-color: blue; padding: 10px;" );
		assertThat( output ).contains( "Styled content" );
	}

	@DisplayName( "It handles empty div gracefully" )
	@Test
	public void testEmptyDiv() {
		runtime.executeSource(
		    """
		    bx:div id="emptyDiv" {
		        // Empty content
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-div" );
		assertThat( output ).contains( "id=\"emptyDiv\"" );
		assertThat( output ).contains( "<div" );
		assertThat( output ).contains( "</div>" );
	}

	@DisplayName( "It processes URL bind expressions correctly" )
	@Test
	public void testDivURLBind() {
		runtime.executeSource(
		    """
		    bx:div bind="/api/endpoint" bindOnLoad="true" {
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "data-bind=\"/api/endpoint\"" );
		assertThat( output ).contains( "bx-div-bind" );
		assertThat( output ).contains( "bx-bind-on-load" );
	}

	@DisplayName( "It exposes executeBind function for manual triggering" )
	@Test
	public void testDivManualBind() {
		runtime.executeSource(
		    """
		    bx:div id="manualDiv" bind="cfc:test.getData" bindOnLoad="false" {
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "divElement.executeBind = executeBind" );
		assertThat( output ).contains( "id=\"manualDiv\"" );
		assertThat( output ).contains( "data-bind-on-load=\"false\"" );
	}
}