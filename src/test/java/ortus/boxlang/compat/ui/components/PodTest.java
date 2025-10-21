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

public class PodTest extends BaseIntegrationTest {

	@DisplayName( "It can create a basic pod with title" )
	@Test
	public void testBasicPodWithTitle() {
		runtime.executeSource(
		    """
		    bx:pod title="My Pod Title" {
		        writeOutput("Pod content here");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-pod" );
		assertThat( output ).contains( "bx-pod-header" );
		assertThat( output ).contains( "bx-pod-body" );
		assertThat( output ).contains( "My Pod Title" );
		assertThat( output ).contains( "Pod content here" );
	}

	@DisplayName( "It can create a pod without title" )
	@Test
	public void testPodWithoutTitle() {
		runtime.executeSource(
		    """
		    bx:pod {
		        writeOutput("Content without title");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-pod" );
		assertThat( output ).contains( "bx-pod-body" );
		assertThat( output ).doesNotContain( "bx-pod-header" );
		assertThat( output ).contains( "Content without title" );
	}

	@DisplayName( "It can set pod dimensions" )
	@Test
	public void testPodDimensions() {
		runtime.executeSource(
		    """
		    bx:pod title="Sized Pod" height="200px" width="300px" {
		        writeOutput("Sized content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "height: 200px" );
		assertThat( output ).contains( "width: 300px" );
		assertThat( output ).contains( "Sized Pod" );
	}

	@DisplayName( "It can set overflow behavior" )
	@Test
	public void testPodOverflow() {
		runtime.executeSource(
		    """
		    bx:pod title="Overflow Test" overflow="hidden" {
		        writeOutput("Content with hidden overflow");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "overflow: hidden" );
		assertThat( output ).contains( "Overflow Test" );
	}

	@DisplayName( "It throws error for invalid overflow value" )
	@Test
	public void testInvalidOverflowValue() {
		try {
			runtime.executeSource(
			    """
			    bx:pod overflow="invalid" {
			        writeOutput("Test content");
			    }
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "overflow attribute must be one of" );
		}
	}

	@DisplayName( "It can apply custom styles" )
	@Test
	public void testPodCustomStyles() {
		runtime.executeSource(
		    """
		    bx:pod
		        title="Styled Pod"
		        bodyStyle="background-color: lightblue; padding: 10px;"
		        headerStyle="font-weight: bold; color: red;"
		        class="my-custom-class"
		        style="border: 1px solid black;" {
		        writeOutput("Styled content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "my-custom-class" );
		assertThat( output ).contains( "border: 1px solid black" );
		assertThat( output ).contains( "background-color: lightblue" );
		assertThat( output ).contains( "font-weight: bold; color: red" );
		assertThat( output ).contains( "Styled Pod" );
	}

	@DisplayName( "It can handle source attribute for AJAX content" )
	@Test
	public void testPodSourceAttribute() {
		runtime.executeSource(
		    """
		    bx:pod title="AJAX Pod" source="/api/pod-content" {
		        // This content should be replaced by AJAX
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-pod" );
		assertThat( output ).contains( "AJAX Pod" );
		assertThat( output ).contains( "<!-- Content loaded from: /api/pod-content -->" );
		assertThat( output ).contains( "bx-source-loading" );
		assertThat( output ).contains( "data-source=\"/api/pod-content\"" );
		assertThat( output ).contains( "Loading content..." );
	}

	@DisplayName( "It generates JavaScript for AJAX source loading" )
	@Test
	public void testPodSourceJavaScript() {
		runtime.executeSource(
		    """
		    bx:pod title="JS Pod" source="/api/content" onBindError="handleError" {
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "<script type=\"text/javascript\">" );
		assertThat( output ).contains( "fetch(sourceUrl)" );
		assertThat( output ).contains( "handleError" );
		assertThat( output ).contains( "bx-source-loading" );
	}

	@DisplayName( "It auto-generates ID when not provided" )
	@Test
	public void testPodAutoGeneratedID() {
		runtime.executeSource(
		    """
		    bx:pod title="Auto ID Pod" {
		        writeOutput("Test content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "id=\"pod_" );
		assertThat( output ).contains( "Auto ID Pod" );
	}

	@DisplayName( "It uses custom ID when provided" )
	@Test
	public void testPodCustomID() {
		runtime.executeSource(
		    """
		    bx:pod id="myCustomPod" title="Custom ID Pod" {
		        writeOutput("Custom ID content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "id=\"myCustomPod\"" );
		assertThat( output ).contains( "Custom ID Pod" );
	}

	@DisplayName( "It handles name attribute with data attribute" )
	@Test
	public void testPodNameAttribute() {
		runtime.executeSource(
		    """
		    bx:pod name="myPodName" title="Named Pod" {
		        writeOutput("Named content");
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "data-name=\"myPodName\"" );
		assertThat( output ).contains( "Named Pod" );
	}
}