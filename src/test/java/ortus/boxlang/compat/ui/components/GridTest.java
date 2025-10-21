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

public class GridTest extends BaseIntegrationTest {

	@DisplayName( "It can create a basic grid with name" )
	@Test
	public void testBasicGrid() {
		runtime.executeSource(
		    """
		    bx:grid name="myGrid" {
		        bx:gridcolumn name="id" header="ID" /
		        bx:gridcolumn name="name" header="Name" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid" );
		assertThat( output ).contains( "data-name=\"myGrid\"" );
		assertThat( output ).contains( "bx-grid-table" );
		assertThat( output ).contains( "bx-grid-header" );
		assertThat( output ).contains( "bx-grid-body" );
	}

	@DisplayName( "It throws error when name attribute is missing" )
	@Test
	public void testMissingNameAttribute() {
		try {
			runtime.executeSource(
			    """
			    bx:grid {
			        bx:gridcolumn name="test" /
			    }
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "name attribute is required" );
		}
	}

	@DisplayName( "It can set grid dimensions and styling" )
	@Test
	public void testGridDimensions() {
		runtime.executeSource(
		    """
		    bx:grid 
		        name="sizedGrid" 
		        height="400px" 
		        width="600px"
		        class="my-grid-class"
		        style="border: 1px solid gray;" {
		        bx:gridcolumn name="col1" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "height: 400px" );
		assertThat( output ).contains( "width: 600px" );
		assertThat( output ).contains( "my-grid-class" );
		assertThat( output ).contains( "border: 1px solid gray" );
	}

	@DisplayName( "It can configure grid behavior attributes" )
	@Test
	public void testGridBehaviorAttributes() {
		runtime.executeSource(
		    """
		    bx:grid 
		        name="behaviorGrid"
		        sortable="true"
		        editable="true"
		        selectMode="multi"
		        pageSize="50"
		        stripeRows="false" {
		        bx:gridcolumn name="id" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid-sortable" );
		assertThat( output ).contains( "bx-grid-editable" );
		assertThat( output ).contains( "data-sortable=\"true\"" );
		assertThat( output ).contains( "data-editable=\"true\"" );
		assertThat( output ).contains( "data-select-mode=\"multi\"" );
		assertThat( output ).contains( "data-page-size=\"50\"" );
		assertThat( output ).doesNotContain( "bx-grid-striped" );
	}

	@DisplayName( "It throws error for invalid selectMode" )
	@Test
	public void testInvalidSelectMode() {
		try {
			runtime.executeSource(
			    """
			    bx:grid name="testGrid" selectMode="invalid" {
			        bx:gridcolumn name="test" /
			    }
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "selectMode attribute must be one of" );
		}
	}

	@DisplayName( "It generates grid headers from columns" )
	@Test
	public void testGridHeaders() {
		runtime.executeSource(
		    """
		    bx:grid name="headerGrid" showHeaders="true" {
		        bx:gridcolumn name="id" header="ID Column" width="80px" /
		        bx:gridcolumn name="name" header="Name Column" width="200px" /
		        bx:gridcolumn name="email" header="Email Address" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid-column-header" );
		assertThat( output ).contains( "ID Column" );
		assertThat( output ).contains( "Name Column" );
		assertThat( output ).contains( "Email Address" );
		assertThat( output ).contains( "data-column=\"id\"" );
		assertThat( output ).contains( "data-column=\"name\"" );
		assertThat( output ).contains( "data-column=\"email\"" );
	}

	@DisplayName( "It can hide headers when showHeaders is false" )
	@Test
	public void testGridNoHeaders() {
		runtime.executeSource(
		    """
		    bx:grid name="noHeaderGrid" showHeaders="false" {
		        bx:gridcolumn name="col1" header="Hidden Header" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).doesNotContain( "<thead>" );
		assertThat( output ).doesNotContain( "bx-grid-header" );
		assertThat( output ).doesNotContain( "Hidden Header" );
		assertThat( output ).contains( "bx-grid-body" );
	}

	@DisplayName( "It generates JavaScript for grid functionality" )
	@Test
	public void testGridJavaScript() {
		runtime.executeSource(
		    """
		    bx:grid 
		        name="jsGrid" 
		        sortable="true" 
		        editable="true"
		        onLoad="gridLoaded"
		        onEdit="cellEdited"
		        onSort="columnSorted" {
		        bx:gridcolumn name="test" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "<script type=\"text/javascript\">" );
		assertThat( output ).contains( "addEventListener('click'" );
		assertThat( output ).contains( "sortColumn" );
		assertThat( output ).contains( "goToPage" );
		assertThat( output ).contains( "toggleSelectAll" );
		assertThat( output ).contains( "gridLoaded" );
		assertThat( output ).contains( "cellEdited" );
		assertThat( output ).contains( "columnSorted" );
	}

	@DisplayName( "It auto-generates ID when not provided" )
	@Test
	public void testGridAutoGeneratedID() {
		runtime.executeSource(
		    """
		    bx:grid name="autoIdGrid" {
		        bx:gridcolumn name="test" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "id=\"grid_" );
	}

	@DisplayName( "It handles grid with manual rows" )
	@Test
	public void testGridWithManualRows() {
		runtime.executeSource(
		    """
		    bx:grid name="manualRowGrid" {
		        bx:gridcolumn name="id" header="ID" /
		        bx:gridcolumn name="name" header="Name" /
		        bx:gridrow data="#{ id: 1, name: 'John' }#" /
		        bx:gridrow data="#{ id: 2, name: 'Jane' }#" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid-row" );
		assertThat( output ).contains( "bx-grid-cell" );
		assertThat( output ).contains( "John" );
		assertThat( output ).contains( "Jane" );
	}

	@DisplayName( "It generates selection columns for multi-select mode" )
	@Test
	public void testGridMultiSelect() {
		runtime.executeSource(
		    """
		    bx:grid name="multiSelectGrid" selectMode="multi" {
		        bx:gridcolumn name="name" header="Name" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid-select-header" );
		assertThat( output ).contains( "bx-grid-select-all" );
		assertThat( output ).contains( "type=\"checkbox\"" );
	}

	@DisplayName( "It generates selection columns for single-select mode" )
	@Test
	public void testGridSingleSelect() {
		runtime.executeSource(
		    """
		    bx:grid name="singleSelectGrid" selectMode="single" {
		        bx:gridcolumn name="name" header="Name" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid-select-header" );
		assertThat( output ).doesNotContain( "bx-grid-select-all" );
		assertThat( output ).doesNotContain( "type=\"checkbox\"" );
	}

	@DisplayName( "It does not generate selection columns for none mode" )
	@Test
	public void testGridNoSelect() {
		runtime.executeSource(
		    """
		    bx:grid name="noSelectGrid" selectMode="none" {
		        bx:gridcolumn name="name" header="Name" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).doesNotContain( "bx-grid-select-header" );
		assertThat( output ).doesNotContain( "bx-grid-select-all" );
	}

	@DisplayName( "It generates pagination when pageSize is set" )
	@Test
	public void testGridPagination() {
		runtime.executeSource(
		    """
		    // Create a mock query with more records than pageSize
		    myQuery = queryNew("id,name", "integer,varchar", [
		        [1, "Record 1"], [2, "Record 2"], [3, "Record 3"],
		        [4, "Record 4"], [5, "Record 5"], [6, "Record 6"]
		    ]);
		    
		    bx:grid name="paginatedGrid" query="#myQuery#" pageSize="3" {
		        bx:gridcolumn name="id" header="ID" /
		        bx:gridcolumn name="name" header="Name" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "bx-grid-pagination" );
		assertThat( output ).contains( "bx-grid-page-btn" );
		assertThat( output ).contains( "Next" );
	}

	@DisplayName( "It fires custom events for grid interactions" )
	@Test
	public void testGridCustomEvents() {
		runtime.executeSource(
		    """
		    bx:grid name="eventGrid" sortable="true" editable="true" {
		        bx:gridcolumn name="test" /
		    }
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "gridCellEdit" );
		assertThat( output ).contains( "gridSort" );
		assertThat( output ).contains( "gridPageChange" );
		assertThat( output ).contains( "CustomEvent" );
		assertThat( output ).contains( "dispatchEvent" );
	}
}