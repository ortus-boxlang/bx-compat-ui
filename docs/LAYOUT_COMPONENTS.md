# BoxLang Layout Components

This module provides BoxLang implementations of the ColdFusion `cflayout` and `cflayoutarea` tags, offering modern UI layout capabilities compatible with CFML syntax.

## Components

### Layout Component (`bx:layout`)

The Layout component creates structured layouts for organizing content areas. It supports multiple layout types:

- **tab** - Tabbed interface with clickable headers
- **accordion** - Collapsible panels with headers
- **border** - Five-region layout (north, south, east, west, center)
- **hbox** - Horizontal box layout (flex row)
- **vbox** - Vertical box layout (flex column)

#### Attributes

| Attribute | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `type` | string | Yes | - | Layout type: tab, accordion, border, hbox, vbox |
| `align` | string | No | "" | Content alignment: left, center, right, justify |
| `fillHeight` | boolean | No | false | Fill available height |
| `fitToWindow` | boolean | No | false | Fill entire window viewport |
| `height` | string | No | "" | CSS height value |
| `width` | string | No | "" | CSS width value |
| `style` | string | No | "" | Additional CSS styles |
| `id` | string | No | auto-generated | HTML element ID |
| `class` | string | No | "" | Additional CSS classes |

#### Examples

**Tab Layout:**
```cfml
<bx:layout type="tab" fillHeight="true">
    <bx:layoutarea title="Home">
        <h2>Welcome</h2>
        <p>Home page content</p>
    </bx:layoutarea>
    <bx:layoutarea title="About">
        <h2>About Us</h2>
        <p>About page content</p>
    </bx:layoutarea>
</bx:layout>
```

**Border Layout:**
```cfml
<bx:layout type="border" height="500px">
    <bx:layoutarea position="north">
        <header>Site Header</header>
    </bx:layoutarea>
    <bx:layoutarea position="west" size="200px">
        <nav>Navigation Menu</nav>
    </bx:layoutarea>
    <bx:layoutarea position="center">
        <main>Main Content Area</main>
    </bx:layoutarea>
    <bx:layoutarea position="east" size="150px">
        <aside>Sidebar</aside>
    </bx:layoutarea>
    <bx:layoutarea position="south">
        <footer>Site Footer</footer>
    </bx:layoutarea>
</bx:layout>
```

**Accordion Layout:**
```cfml
<bx:layout type="accordion">
    <bx:layoutarea title="Section 1" collapsible="true">
        <p>First section content</p>
    </bx:layoutarea>
    <bx:layoutarea title="Section 2" initcollapsed="true">
        <p>Second section content (initially collapsed)</p>
    </bx:layoutarea>
</bx:layout>
```

### LayoutArea Component (`bx:layoutarea`)

The LayoutArea component defines content areas within a Layout. Must be used inside a Layout component.

#### Attributes

| Attribute | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `title` | string | No | "" | Area title (used for tabs/accordion headers) |
| `align` | string | No | "" | Content alignment within the area |
| `collapsible` | boolean | No | true | Whether area can be collapsed (accordion) |
| `initcollapsed` | boolean | No | false | Start in collapsed state |
| `source` | string | No | "" | URL to load content from |
| `position` | string | No | "center" | Position for border layout: north, south, east, west, center |
| `size` | string | No | "" | CSS size for border layout regions |
| `splitter` | boolean | No | true | Show splitter for border layout |
| `minsize` | string | No | "" | Minimum size constraint |
| `maxsize` | string | No | "" | Maximum size constraint |
| `id` | string | No | auto-generated | HTML element ID |

#### Border Layout Positions

- **north** - Top region (full width)
- **south** - Bottom region (full width)
- **east** - Right region (between north/south)
- **west** - Left region (between north/south)
- **center** - Middle region (fills remaining space)

## CSS Classes

The components generate semantic CSS classes for styling:

### Layout Classes
- `.bx-layout` - Base layout container
- `.bx-layout-{type}` - Layout type specific (tab, accordion, border, hbox, vbox)
- `.bx-layout-fill-height` - Fill height layout
- `.bx-layout-fit-window` - Fit to window layout
- `.bx-layout-align-{align}` - Alignment classes

### Tab Layout Classes
- `.bx-tab-headers` - Tab header container
- `.bx-tab-header` - Individual tab header
- `.bx-tab-header.active` - Active tab header
- `.bx-tab-content` - Tab content container
- `.bx-tab-panel` - Individual tab panel
- `.bx-tab-panel.active` - Active tab panel

### Accordion Layout Classes
- `.bx-accordion-panel` - Accordion panel container
- `.bx-accordion-panel.collapsed` - Collapsed panel
- `.bx-accordion-header` - Accordion header
- `.bx-accordion-content` - Accordion content area

### Border Layout Classes
- `.bx-border-{position}` - Border region (north, south, east, west, center)
- `.bx-border-area` - Border area base class
- `.bx-border-splitter` - Splitter enabled area

### Box Layout Classes
- `.bx-box-item` - Box layout item (hbox/vbox)

## JavaScript Functionality

The components automatically generate JavaScript for interactive layouts:

### Tab Layout
- Click tab headers to switch panels
- Automatic active state management
- Keyboard navigation support

### Accordion Layout  
- Click headers to toggle panel visibility
- Smooth collapse/expand animations
- Multiple panels can be open simultaneously

## Styling

Include the provided CSS file for default styling:

```html
<link rel="stylesheet" href="/path/to/layout-components.css">
```

Or customize with your own CSS targeting the generated classes.

## Browser Support

- Modern browsers with CSS Grid and Flexbox support
- IE11+ (with CSS Grid polyfill for border layout)
- Mobile responsive design included

## Examples

### Complete Application Layout

```cfml
<!DOCTYPE html>
<html>
<head>
    <title>BoxLang Layout Example</title>
    <link rel="stylesheet" href="layout-components.css">
</head>
<body>
    <bx:layout type="border" fitToWindow="true">
        <bx:layoutarea position="north" size="60px">
            <header style="padding: 10px; background: #333; color: white;">
                <h1>My Application</h1>
            </header>
        </bx:layoutarea>
        
        <bx:layoutarea position="west" size="250px">
            <bx:layout type="accordion">
                <bx:layoutarea title="Navigation">
                    <ul>
                        <li><a href="#home">Home</a></li>
                        <li><a href="#products">Products</a></li>
                        <li><a href="#contact">Contact</a></li>
                    </ul>
                </bx:layoutarea>
                <bx:layoutarea title="Tools">
                    <button>Export Data</button>
                    <button>Import Data</button>
                </bx:layoutarea>
            </bx:layout>
        </bx:layoutarea>
        
        <bx:layoutarea position="center">
            <bx:layout type="tab" fillHeight="true">
                <bx:layoutarea title="Dashboard">
                    <h2>Dashboard</h2>
                    <p>Main dashboard content</p>
                </bx:layoutarea>
                <bx:layoutarea title="Reports">
                    <h2>Reports</h2>
                    <p>Reports content</p>
                </bx:layoutarea>
                <bx:layoutarea title="Settings">
                    <h2>Settings</h2>
                    <p>Settings content</p>
                </bx:layoutarea>
            </bx:layout>
        </bx:layoutarea>
        
        <bx:layoutarea position="south" size="40px">
            <footer style="padding: 10px; background: #f5f5f5; text-align: center;">
                © 2024 My Application
            </footer>
        </bx:layoutarea>
    </bx:layout>
</body>
</html>
```

This creates a complete application layout with header, footer, collapsible sidebar navigation, and tabbed main content area.