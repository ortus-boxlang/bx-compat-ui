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
import ortus.boxlang.runtime.dynamic.casters.IntegerCaster;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;

public class QueryConvertForGridTest extends BaseIntegrationTest {

	@DisplayName( "It can convert a query for grid display" )
	@Test
	public void testBasicQueryConvert() {
		runtime.executeSource(
		    """
		    // Create test query
		    testQuery = queryNew("id,name,email", "integer,varchar,varchar");
		    queryAddRow(testQuery, {id: 1, name: "John Doe", email: "john@example.com"});
		    queryAddRow(testQuery, {id: 2, name: "Jane Smith", email: "jane@example.com"});
		    queryAddRow(testQuery, {id: 3, name: "Bob Johnson", email: "bob@example.com"});

		    result = QueryConvertForGrid(testQuery, 1, 2);
		    """,
		    context
		);

		IStruct result = variables.getAsStruct( Key.of( "result" ) );
		assertThat( result ).isNotNull();
		assertThat( IntegerCaster.cast( result.get( Key.of( "TOTALROWCOUNT" ) ) ) ).isEqualTo( 3 );
		assertThat( IntegerCaster.cast( result.get( Key.of( "PAGE" ) ) ) ).isEqualTo( 1 );
		assertThat( IntegerCaster.cast( result.get( Key.of( "PAGESIZE" ) ) ) ).isEqualTo( 2 );
		assertThat( IntegerCaster.cast( result.get( Key.of( "TOTALPAGES" ) ) ) ).isEqualTo( 2 );
	}

	@DisplayName( "It handles pagination correctly" )
	@Test
	public void testQueryConvertPagination() {
		runtime.executeSource(
		    """
		    // Create test query with 10 rows
		    testQuery = queryNew("id,name", "integer,varchar");
		    for (i = 1; i <= 10; i++) {
		        queryAddRow(testQuery, {id: i, name: "Person #i#"});
		    }

		    // Get page 2 with 3 rows per page
		    result = QueryConvertForGrid(testQuery, 2, 3);
		    """,
		    context
		);

		IStruct result = variables.getAsStruct( Key.of( "result" ) );
		assertThat( IntegerCaster.cast( result.get( Key.of( "PAGE" ) ) ) ).isEqualTo( 2 );
		assertThat( IntegerCaster.cast( result.get( Key.of( "STARTROW" ) ) ) ).isEqualTo( 4 );
		assertThat( IntegerCaster.cast( result.get( Key.of( "ENDROW" ) ) ) ).isEqualTo( 6 );
	}

	@DisplayName( "It validates required parameters" )
	@Test
	public void testQueryConvertValidation() {
		runtime.executeSource(
		    """
		    try {
		        result = QueryConvertForGrid();
		        hasError = false;
		    } catch (any e) {
		        hasError = true;
		        errorMessage = e.message;
		    }
		    """,
		    context
		);

		Boolean hasError = variables.getAsBoolean( Key.of( "hasError" ) );
		assertThat( hasError ).isTrue();
	}
}