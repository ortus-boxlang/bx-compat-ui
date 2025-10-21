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

public class LayoutTest extends BaseIntegrationTest {

	@DisplayName( "It can create a basic tab layout" )
	@Test
	public void testBasicTabLayout() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="tab", id="myTabLayout" ) {
		            bx:layoutarea( title="Tab 1" ) {
		                writeOutput("Content for Tab 1");
		            }
		            bx:layoutarea( title="Tab 2" ) {
		                writeOutput("Content for Tab 2");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout" );
		assertThat( output ).contains( "bx-layout-tab" );
		assertThat( output ).contains( "id=\"myTabLayout\"" );
		assertThat( output ).contains( "Tab 1" );
		assertThat( output ).contains( "Tab 2" );
		assertThat( output ).contains( "Content for Tab 1" );
		assertThat( output ).contains( "Content for Tab 2" );
		assertThat( output ).contains( "bx-tab-header" );
		assertThat( output ).contains( "bx-tab-panel" );
	}

	@DisplayName( "It can create an accordion layout" )
	@Test
	public void testAccordionLayout() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="accordion", fillHeight="true" ) {
		            bx:layoutarea( title="Section 1" ) {
		                writeOutput("Content for Section 1");
		            }
		            bx:layoutarea( title="Section 2", initcollapsed="true" ) {
		                writeOutput("Content for Section 2");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout" );
		assertThat( output ).contains( "bx-layout-accordion" );
		assertThat( output ).contains( "bx-layout-fill-height" );
		assertThat( output ).contains( "Section 1" );
		assertThat( output ).contains( "Section 2" );
		assertThat( output ).contains( "Content for Section 1" );
		assertThat( output ).contains( "Content for Section 2" );
		assertThat( output ).contains( "bx-accordion-panel" );
		assertThat( output ).contains( "collapsed" );
	}

	@DisplayName( "It can create a border layout" )
	@Test
	public void testBorderLayout() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="border", height="500px" ) {
		            bx:layoutarea( position="top", title="Header" ) {
		                writeOutput("Header content");
		            }
		            bx:layoutarea( position="left", size="200px" ) {
		                writeOutput("Left sidebar");
		            }
		            bx:layoutarea( position="center" ) {
		                writeOutput("Main content");
		            }
		            bx:layoutarea( position="right", size="150px" ) {
		                writeOutput("Right sidebar");
		            }
		            bx:layoutarea( position="bottom" ) {
		                writeOutput("Footer content");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout" );
		assertThat( output ).contains( "bx-layout-border" );
		assertThat( output ).contains( "height: 500px" );
		assertThat( output ).contains( "bx-border-top" );
		assertThat( output ).contains( "bx-border-left" );
		assertThat( output ).contains( "bx-border-center" );
		assertThat( output ).contains( "bx-border-right" );
		assertThat( output ).contains( "bx-border-bottom" );
		assertThat( output ).contains( "Header content" );
		assertThat( output ).contains( "Left sidebar" );
		assertThat( output ).contains( "Main content" );
		assertThat( output ).contains( "Right sidebar" );
		assertThat( output ).contains( "Footer content" );
	}

	@DisplayName( "It can create an hbox layout" )
	@Test
	public void testHBoxLayout() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="hbox", align="center" ) {
		            bx:layoutarea() {
		                writeOutput("Box 1");
		            }
		            bx:layoutarea() {
		                writeOutput("Box 2");
		            }
		            bx:layoutarea() {
		                writeOutput("Box 3");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout" );
		assertThat( output ).contains( "bx-layout-hbox" );
		assertThat( output ).contains( "bx-layout-align-center" );
		assertThat( output ).contains( "bx-box-item" );
		assertThat( output ).contains( "Box 1" );
		assertThat( output ).contains( "Box 2" );
		assertThat( output ).contains( "Box 3" );
	}

	@DisplayName( "It can create a vbox layout" )
	@Test
	public void testVBoxLayout() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="vbox", width="300px" ) {
		            bx:layoutarea() {
		                writeOutput("Vertical Box 1");
		            }
		            bx:layoutarea() {
		                writeOutput("Vertical Box 2");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout" );
		assertThat( output ).contains( "bx-layout-vbox" );
		assertThat( output ).contains( "width: 300px" );
		assertThat( output ).contains( "bx-box-item" );
		assertThat( output ).contains( "Vertical Box 1" );
		assertThat( output ).contains( "Vertical Box 2" );
	}

	@DisplayName( "It throws error when type attribute is missing" )
	@Test
	public void testMissingTypeAttribute() {
		try {
			runtime.executeSource(
			    """
			        bx:layout() {
			            bx:layoutarea( title="Test" ) {
			                writeOutput("Test content");
			            }
			        }
			        """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "type attribute is required" );
		}
	}

	@DisplayName( "It throws error for invalid type attribute" )
	@Test
	public void testInvalidTypeAttribute() {
		try {
			runtime.executeSource(
			    """
			        bx:layout( type="invalid" ) {
			            bx:layoutarea( title="Test" ) {
			                writeOutput("Test content");
			            }
			        }
			        """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "type attribute must be one of" );
		}
	}

	@DisplayName( "It generates JavaScript for interactive layouts" )
	@Test
	public void testJavaScriptGeneration() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="tab", id="jsTabTest" ) {
		            bx:layoutarea( title="Interactive Tab" ) {
		                writeOutput("Interactive content");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "<script type=\"text/javascript\">" );
		assertThat( output ).contains( "getElementById('jsTabTest')" );
		assertThat( output ).contains( "bx-tab-header" );
		assertThat( output ).contains( "addEventListener" );
	}

	@DisplayName( "It handles custom CSS classes and styles" )
	@Test
	public void testCustomCSSAndStyles() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( 
		            type="tab", 
		            class="my-custom-class",
		            style="background-color: red;",
		            fillHeight="true",
		            fitToWindow="true"
		        ) {
		            bx:layoutarea( title="Styled Tab" ) {
		                writeOutput("Styled content");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout bx-layout-tab bx-layout-fill-height bx-layout-fit-window my-custom-class" );
		assertThat( output ).contains( "background-color: red;" );
		assertThat( output ).contains( "Styled content" );
	}

	@DisplayName( "It handles empty layout gracefully" )
	@Test
	public void testEmptyLayout() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="tab", id="emptyLayout" ) {
		            // No layout areas
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "bx-layout" );
		assertThat( output ).contains( "bx-layout-tab" );
		assertThat( output ).contains( "id=\"emptyLayout\"" );
		// Should still render properly even with no areas
	}

	@DisplayName( "It auto-generates ID when not provided" )
	@Test
	public void testAutoGeneratedID() {
		runtime.executeSource(
		    """
		        result = writeOutput("");
		        bx:layout( type="tab" ) {
		            bx:layoutarea( title="Auto ID Test" ) {
		                writeOutput("Test content");
		            }
		        }
		        """,
		    context
		);

		String output = context.getBuffer().toString();
		assertThat( output ).contains( "id=\"layout_" );
		assertThat( output ).contains( "bx-layout" );
	}
}