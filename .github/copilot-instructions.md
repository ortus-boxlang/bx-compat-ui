# BoxLang Spreadsheet Module - AI Coding Guide

## Project Overview

This is a BoxLang module for Excel spreadsheet manipulation built on Apache POI. The module provides three distinct APIs:

1. **Fluent API** (`SpreadsheetFile` class) - Recommended modern approach with method chaining
2. **BIF Functions** - Traditional function-based approach (`SpreadsheetNew()`, `SpreadsheetRead()`, etc.)
3. **Component Tag** (`<bx:spreadsheet>`) - Declarative CFML-compatible approach
4. All markdown headers need spaces between them due to standards

## Architecture Patterns

### Module Structure

- `src/main/bx/ModuleConfig.bx` - BoxLang module descriptor defining registration, settings, and lifecycle
- `src/main/java/ortus/boxlang/spreadsheet/` - Core Java implementation
  - `SpreadsheetFile.java` - Main fluent API class (1200+ lines)
  - `SpreadsheetUtil.java` - Apache POI utilities and static helpers
  - `bifs/` - Individual BIF function implementations (`@BoxBIF` annotated)
  - `components/Spreadsheet.java` - Component tag implementation (`@BoxComponent` annotated)
  - `interceptors/LicenseInterceptor.java` - License validation interceptor
  - `util/KeyDictionary.java` - Centralized Key constants

### Key Code Patterns

**CRITICAL ARCHITECTURE PRINCIPLE: Thin Controllers**
BIFs and Components are CONTROLLERS - they should be thin wrappers that delegate to business logic in `SpreadsheetFile` and `SpreadsheetUtil`. Never implement complex logic directly in BIFs/Components.

**BIF Implementation Pattern (Thin Controller):**

```java
@BoxBIF(description = "Creates a new spreadsheet object")
public class SpreadsheetNew extends BIF {
    public SpreadsheetNew() {
        declaredArguments = new Argument[] {
            new Argument(false, Argument.STRING, Key.of("sheetname")),
            new Argument(false, Argument.BOOLEAN, Key.of("xmlformat"))
        };
    }

    public Object _invoke(IBoxContext context, ArgumentsScope arguments) {
        // Extract arguments
        String sheetName = arguments.getAsString(Key.of("sheetname"));
        Boolean xmlFormat = arguments.getAsBoolean(Key.of("xmlformat"));

        // Delegate to SpreadsheetFile - NO business logic here
        return new SpreadsheetFile(xmlFormat, sheetName);
    }
}
```

**Component Implementation Pattern (Thin Controller):**

```java
@BoxComponent(name = "Spreadsheet", allowsBody = false, requiresBody = false)
public class Spreadsheet extends Component {
    // Declare all supported attributes in constructor
    // Switch on action attribute in _invoke method
    // Delegate ALL operations to SpreadsheetFile/SpreadsheetUtil
}
```

**Business Logic Location:**

- `SpreadsheetFile.java` - High-level fluent API operations, state management
- `SpreadsheetUtil.java` - Low-level Apache POI operations, static utilities
- BIFs/Components - ONLY argument extraction and delegation

**Fluent API Pattern:**
All methods in `SpreadsheetFile` return `this` for chaining, with explicit state management via private fields (`workbook`, `activeSheetName`, `path`, etc.).

## Development Workflows

### Build & Test

```bash
./gradlew build          # Full build with tests
./gradlew test           # Run tests only
./gradlew shadowJar      # Create fat JAR with dependencies
```

### Testing Patterns

- Extend `BaseIntegrationTest` for all module tests
- Use `@BeforeAll` to load module into BoxRuntime instance
- Test data in `src/test/resources/tmp/` (auto-cleaned)
- Execute BoxLang code via `runtime.executeSource(code, context)`
- Assertions use Google Truth: `assertThat(result).isNotNull()`

**Test Structure Example:**

```java
public class MyTest extends BaseIntegrationTest {
    @Test
    public void testFeature() {
        runtime.executeSource("""
            result = SpreadsheetNew();
            """, context);
        assertThat(variables.get(result)).isNotNull();
    }
}
```

### Module Loading

Module uses `KeyDictionary.moduleName = "bx-spreadsheet"` for registration. The `BaseIntegrationTest.loadModule()` handles module lifecycle during testing.

## BoxLang Type Helpers and Casters

### IStruct/ArgumentsScope getAs* Methods

BoxLang's `IStruct` interface and `ArgumentsScope` (which extends `IStruct`) provide typed accessor methods for safe value extraction:

**Use `getAs*()` methods when you KNOW the type at compile time (strongly typed):**

- `getAsString(Key)` - Returns the value as a String, null if not present
- `getAsBoolean(Key)` - Returns the value as a Boolean, null if not present
- `getAsInteger(Key)` - Returns the value as an Integer, null if not present
- `getAsLong(Key)` - Returns the value as a Long, null if not present
- `getAsDouble(Key)` - Returns the value as a Double, null if not present
- `getAsStruct(Key)` - Returns the value as an IStruct, null if not present
- `getAsArray(Key)` - Returns the value as an Array, null if not present
- `getAsNumber(Key)` - Returns the value as a Number (smallest type possible)
- `getAsKey(Key)` - Returns the value as a Key

Example usage (when type is known from argument/attribute declarations):

```java
// In ArgumentsScope (method arguments), type is declared, so safe to use getAs*
String sheetName = arguments.getAsString(Key.of("sheetname"));
Boolean xmlFormat = arguments.getAsBoolean(Key.of("xmlformat"));
Integer fontSize = commentStruct.getAsInteger(Key.of("size"));

// For optional values that may be null, check first
if (commentStruct.containsKey(Key.of("bold"))) {
    Boolean bold = commentStruct.getAsBoolean(Key.of("bold"));
    if (bold != null && bold) {
        font.setBold(true);
    }
}
```

### Dynamic Type Casting with Casters

When the type is NOT known at compile time or from user input that needs validation, use the caster classes from `ortus.boxlang.runtime.dynamic.casters`:

**Common Casters:**

- `StringCaster.cast(value)` - Casts any value to String (for unknown types)
- `BooleanCaster.cast(value)` - Casts any value to Boolean (with validation)
- `IntegerCaster.cast(value)` - Casts any value to Integer
- `LongCaster.cast(value)` - Casts any value to Long
- `DoubleCaster.cast(value)` - Casts any value to Double
- `NumberCaster.cast(value)` - Casts to smallest appropriate number type
- `StructCaster.cast(value)` - Casts any value to Struct
- `ArrayCaster.cast(value)` - Casts any value to Array
- `DateTimeCaster.cast(value)` - Casts to DateTime objects

Example usage (when type is unknown or user-provided):

```java
// Using getAs* is safe - returns null if type can't be converted
Object userValue = someSource.get(someKey);
if (userValue != null) {
    // For dynamic values from user input where type is uncertain, use casters
    String strValue = StringCaster.cast(userValue);  // Handles any type
    Integer intValue = IntegerCaster.cast(userValue); // Validates and converts
}

// Or use the CastAttempt approach for soft failures
CastAttempt<String> attempt = StringCaster.tryCast(userValue);
if (attempt.wasSuccessful()) {
    String result = attempt.getResult();
}
```

**Pattern: Use Strong Typing in Module Code**

- Always declare argument types in BIF constructors using `Argument.STRING`, `Argument.BOOLEAN`, etc.
- Use `arguments.getAs*()` in BIF `_invoke()` methods - type is guaranteed by framework
- Only use casters when dealing with user input from untyped contexts
- In `SpreadsheetUtil` helper methods, prefer documented strong types where possible

## Project-Specific Conventions

### Licensing

- Requires BoxLang+ (bx-plus) license via `LicenseInterceptor`
- License check happens on `onRuntimeStart` interception point
- Uses `DynamicInteropService` to call license service methods

### File Format Handling

- Default `xmlFormat = true` creates `.xlsx` files
- `xmlFormat = false` creates legacy `.xls` files
- Apache POI abstractions handle format differences transparently

### Key Management

- All BoxLang `Key` objects centralized in `KeyDictionary`
- Use `Key.of("string")` for new keys, prefer constants for reused keys
- BoxLang context access via `context.getScopeNearby(VariablesScope.name)`

### Error Handling

- Use `BoxRuntimeException` for all module-specific errors
- Include descriptive messages for API misuse
- Validate required parameters early in BIF/component methods

## Integration Points

### BoxLang Runtime Integration

- Module registers automatically via `ModuleService`
- BIFs auto-discovered in `bifs/` package
- Components auto-discovered with `@BoxComponent` annotation
- Interceptors registered via `ModuleConfig.configure()`

### Data Interchange

- Export to BoxLang Arrays/Structs via `toArray()`, `toQuery()`
- Import from BoxLang data structures via constructor overloads
- JSON export/import through BoxLang's native serialization

## Common Gotchas

- Module depends on BoxLang+ license - ensure license service available
- Test module loading requires physical path resolution in `BaseIntegrationTest`
- File paths need expansion via `FileSystemUtil.expandPath(context, path)`
