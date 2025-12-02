# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**SciSTS** is a dual-mod package for Slay the Spire (杀戮尖塔) that combines:

1. **MapMarks Module**: A map node highlighting and marking system for visual dungeon annotation
2. **SciSTS Module**: A content mod adding new relics, cards, events, and gameplay mechanics

The project uses Maven for build management and targets Java 8 compatibility for Slay the Spire modding.

## Build and Development Commands

### Core Commands
- **Build**: `mvn clean package` - Compiles and packages both mod modules
- **Install**: Automatically copies JAR to Slay the Spire mods directory after build
- **Test**: Manual testing required - launch Slay the Spire with ModTheSpire and load the mod

### Configuration Requirements
- **Steam Path**: Must be configured in `pom.xml` line 26 (`<Steam.path>`)
- **Target Directory**: JAR automatically copied to `steamapps/common/SlayTheSpire/mods/`

## Architecture

### Dual-Mod Structure
The codebase follows a **dual-mod architecture** where two independent modules coexist:

```
src/main/java/
├── MapMarks/          # Map visualization and UI enhancement
└── sciSTS/           # Core gameplay content and modifications
```

### MapMarks Module Architecture
- **UI Framework**: Built on Easel library for advanced interface components
- **Event System**: Uses BaseMod's subscriber pattern for game integration
- **Rendering**: Custom sprite batch rendering for map overlays
- **Input Handling**: Radial menus, mouse gestures, and keyboard modifiers
- **State Management**: SpireConfig for persistent settings

### SciSTS Module Architecture
- **Content Registration**: BaseMod interfaces for cards, relics, events
- **Game Patching**: ModTheSpire annotations for runtime code modification
- **Localization**: JSON-based multi-language support (ZHS/ENG)
- **Resource Management**: Structured asset loading for cards, relics, UI

### Key Dependencies
- **ModTheSpire 3.23.2**: Core modding framework for bytecode patching
- **BaseMod 5.33.1**: Community API for content addition
- **StSLib 2.11.0**: Extended library with additional utilities
- **Easel 2020-11-30**: UI framework used by MapMarks
- **Slay the Spire 12-22-2020**: Target game version

## Development Workflow

### Adding New Content (SciSTS Module)
1. Create card/relic classes in respective packages
2. Add localization entries to `SciSTSResources/localization/[LANG]/`
3. Register content in `SciSTS.java` using BaseMod methods:
   - `BaseMod.addCard()` for cards
   - `BaseMod.addRelic()` for relics
4. Include assets in appropriate resource directories

### Map Features (MapMarks Module)
1. Map functionality requires both Pen Nib and Ink Bottle relics
2. Right-click gestures for highlighting/unhighlighting map nodes
3. Alt-key for free drawing mode
4. Ctrl-click for clearing unreachable nodes
5. Radial color menu for selecting highlight colors

### Localization Support
- **Languages**: ZHS (Simplified Chinese) and ENG (English)
- **File Locations**:
  - `MapMarks/localization/[LANG]/UIStrings.json`
  - `SciSTSResources/localization/[LANG]/[type].json`
- **Detection**: Automatic based on `Settings.language`

### Testing Approach
- Manual in-game testing through Slay the Spire
- Map marking features: Test with Pen Nib + Ink Bottle relics
- Content verification: Ensure new cards/relics appear with proper localization
- Visual testing: Verify UI elements render correctly at different resolutions

## Key Integration Points

### Mod Entry Points
- **MapMarks**: `MapMarks.java` - Initializes UI system and event subscriptions
- **SciSTS**: `sciSTS/modcore/SciSTS.java` - Main mod class with content registration

### Cross-Module Communication
- `SciSTS.shouldDraw()` method controls MapMarks visibility
- Both modules share localization and configuration systems
- Resource loading uses consistent patterns across modules

### Game Integration
- Event-driven architecture using BaseMod subscriber interfaces
- Runtime patching via ModTheSpire annotations (@SpireInitializer)
- LibGDX rendering integration for custom UI elements