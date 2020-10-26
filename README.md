plantuml4idea
=============

IntelliJ [IDEA plugin for PlantUML](http://plugins.intellij.net/plugin/?idea&id=7017)

This plugin provides integration with popular [PlantUML](http://plantuml.sourceforge.net/) diagramming tool

Author: [Eugene Steinberg](https://github.com/esteinberg)

Contributors:

 * [Ivan Mamontov](https://github.com/IvanMamontov)
 * [Henady Zakalusky](https://github.com/hza)
 * [Max Gorbunov](https://github.com/6zow)
 * [Vojtěch Krása](https://github.com/krasa)
 * [Andrew Korolev](https://github.com/koroandr)

# Features

* PlantUML tool window renders PlantUML source code under caret in currently selected editor
* Supports multiple sources per file
* Supports pagination and zoom
* Can copy diagram to clipboard or export as PNG, EPS or SVG, ASCII Art
* Caching and incremental rendering 

# Tips

* PlantUML code must be inside @startuml and @enduml tags to be rendered.
* To be able to generate many diagram types, you must have [Graphviz](https://www.graphviz.org/download/)
 installed on your machine. For Windows, use [Development version](https://ci.appveyor.com/project/ellson/graphviz-pl238), not Stable 2.38 - that one is very old. You have to select your OS/Release and then go in `Artefact` tab to get .exe file or zip. About screen tests your installation.

# Developer notes
* Project setup: [gif](https://user-images.githubusercontent.com/1160875/55478653-7dbb2300-561c-11e9-8a58-66f5a66b5dc1.gif) [mp4](https://mega.nz/#!66oTUIgA!ckkAdLZNHtXjIwyoSlN6BwA-vEWh_034vTRqtWZr9AM) 
* + add `plugins\platform-images\lib\platform-images.jar` to the SDK. Protip: use https://plugins.jetbrains.com/plugin/6844-useful-actions -  `Tools | Add Dependant Bundled Plugins to IntelliJ Platform Plugin SDK` instead.
* debug logs can be enabled by adding '#org.plantuml' and 'org.plantuml' to [Help | Debug Log Settings] 
* use [Jetbrains JDK](https://confluence.jetbrains.com/display/JBR/JetBrains+Runtime) if normal JDK produces UI bugs

## There are following branches:

### master
* Current production branch

### 1.x
* deprecated branch, to be deleted

### grammar
* Contains new experimental syntax support
* Grammar classes can be generated using tools/grammar-gen.sh
* This script can run automatically when you run the plugin using "Plugin" Run/Debug configuration. Just add the script
above as an external tool and make it run before the "Make" step.
