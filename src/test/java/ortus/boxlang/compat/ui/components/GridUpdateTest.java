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

public class GridUpdateTest extends BaseIntegrationTest {

	@DisplayName( "It can create grid update with database configuration" )
	@Test
	public void testGridUpdateDatabase() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="myGrid"
		        dataSource="myDataSource"
		        tableName="employees"
		        keyOnly="false";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "<form" );
		assertThat( output ).contains( "gridName" );
		assertThat( output ).contains( "myGrid" );
		assertThat( output ).contains( "gridData" );
		assertThat( output ).contains( "<script type=\"text/javascript\">" );
		assertThat( output ).contains( "updateDatabase" );
	}

	@DisplayName( "It can create grid update with URL configuration" )
	@Test
	public void testGridUpdateURL() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="urlGrid"
		        url="/api/update-grid"
		        method="PUT"
		        onSuccess="handleSuccess"
		        onError="handleError";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "method=\"PUT\"" );
		assertThat( output ).contains( "action=\"/api/update-grid\"" );
		assertThat( output ).contains( "urlGrid" );
		assertThat( output ).contains( "updateViaURL" );
		assertThat( output ).contains( "handleSuccess" );
		assertThat( output ).contains( "handleError" );
	}

	@DisplayName( "It throws error when grid attribute is missing" )
	@Test
	public void testGridUpdateMissingGrid() {
		try {
			runtime.executeSource(
			    """
			    bx:gridupdate
			        dataSource="test"
			        tableName="test";
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "grid attribute is required" );
		}
	}

	@DisplayName( "It throws error when neither database nor URL config provided" )
	@Test
	public void testGridUpdateMissingConfiguration() {
		try {
			runtime.executeSource(
			    """
			    bx:gridupdate grid="testGrid" /
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "requires either dataSource+tableName for database updates or url for HTTP updates" );
		}
	}

	@DisplayName( "It throws error for invalid HTTP method" )
	@Test
	public void testGridUpdateInvalidMethod() {
		try {
			runtime.executeSource(
			    """
			    bx:gridupdate
			        grid="testGrid"
			        url="/api/update"
			        method="INVALID";
			    """,
			    context
			);
		} catch ( Exception e ) {
			assertThat( e.getMessage() ).contains( "method attribute must be one of" );
		}
	}

	@DisplayName( "It generates JavaScript for change tracking" )
	@Test
	public void testGridUpdateChangeTracking() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="trackingGrid"
		        dataSource="testDB"
		        tableName="testTable";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "modifiedCells" );
		assertThat( output ).contains( "deletedRows" );
		assertThat( output ).contains( "newRows" );
		assertThat( output ).contains( "gridCellEdit" );
		assertThat( output ).contains( "gridRowDelete" );
		assertThat( output ).contains( "gridRowAdd" );
		assertThat( output ).contains( "addEventListener" );
	}

	@DisplayName( "It creates public update function" )
	@Test
	public void testGridUpdatePublicFunction() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="publicFunctionGrid"
		        url="/api/save";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "window['updateGrid_publicFunctionGrid']" );
		assertThat( output ).contains( "function()" );
	}

	@DisplayName( "It handles database authentication" )
	@Test
	public void testGridUpdateDatabaseAuth() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="authGrid"
		        dataSource="secureDB"
		        tableName="secureTable"
		        username="dbuser"
		        password="dbpass"
		        keyOnly="true";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "payload.username" );
		assertThat( output ).contains( "payload.password" );
		assertThat( output ).contains( "keyOnly: true" );
	}

	@DisplayName( "It generates database update endpoint call" )
	@Test
	public void testGridUpdateDatabaseEndpoint() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="endpointGrid"
		        dataSource="myDB"
		        tableName="myTable";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "/bx-compat-ui/gridupdate" );
		assertThat( output ).contains( "fetch(" );
		assertThat( output ).contains( "application/json" );
		assertThat( output ).contains( "JSON.stringify" );
	}

	@DisplayName( "It handles URL updates with form data" )
	@Test
	public void testGridUpdateURLFormData() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="formGrid"
		        url="/update-endpoint"
		        method="POST";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "FormData(updateForm)" );
		assertThat( output ).contains( "/update-endpoint" );
		assertThat( output ).contains( "POST" );
	}

	@DisplayName( "It fires success and error events" )
	@Test
	public void testGridUpdateEvents() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="eventGrid"
		        url="/api/events"
		        onSuccess="mySuccess"
		        onError="myError";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "gridUpdateSuccess" );
		assertThat( output ).contains( "gridUpdateError" );
		assertThat( output ).contains( "CustomEvent" );
		assertThat( output ).contains( "mySuccess" );
		assertThat( output ).contains( "myError" );
		assertThat( output ).contains( "dispatchEvent" );
	}

	@DisplayName( "It handles content type detection in responses" )
	@Test
	public void testGridUpdateContentType() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="contentTypeGrid"
		        url="/api/flexible-response";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "content-type" );
		assertThat( output ).contains( "application/json" );
		assertThat( output ).contains( "response.json()" );
		assertThat( output ).contains( "response.text()" );
	}

	@DisplayName( "It includes table metadata in database updates" )
	@Test
	public void testGridUpdateTableMetadata() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="metadataGrid"
		        dataSource="catalogDB"
		        tableName="products"
		        tableOwner="inventory"
		        tableQualifier="production";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "tableOwner: 'inventory'" );
		assertThat( output ).contains( "tableQualifier: 'production'" );
		assertThat( output ).contains( "tableName: 'products'" );
	}

	@DisplayName( "It clears tracking data after successful update" )
	@Test
	public void testGridUpdateTrackingClear() {
		runtime.executeSource(
		    """
		    bx:gridupdate
		        grid="clearGrid"
		        url="/api/clear-test";
		    result = getBoxContext().getBuffer().toString()
		    """,
		    context
		);

		String output = variables.getAsString( Key.of( "result" ) );
		assertThat( output ).contains( "modifiedCells.clear()" );
		assertThat( output ).contains( "deletedRows.clear()" );
		assertThat( output ).contains( "newRows = []" );
	}
}