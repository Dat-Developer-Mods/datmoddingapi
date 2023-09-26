A modding library for DatDeveloper

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/)
[![GitHub Release](https://img.shields.io/github/release/Dat-Developer-Mods/datmoddingapi.svg?style=flat)]()

# Features
## Concurrent Tasks
Concurrent Tasks
> A system for scheduling tasks to execute concurrently, optionally after a delay
> 
> Concurrent Tasks execute on another thread, and thus can lead to race conditions if thread safety is not followed

## Delayed Events
> A system for scheduling tasks to execute after a condition is met
> 
> Events run on the server thread, on the server tick event, thus are thread safe

> Events are an implementation of IDelayedEvent, for an example of an event look at DelayedTeleportEvent

## Localisation
DatLocalisation
> A framework for loading localisation strings

## Commands
Pager
> A command utility for taking a list of objects and converting them into a series of pages that are navigable using in-text buttons.

## Permissions
DatPermissions
> A permission abstraction system that allows testing permission nodes without worrying about the permission backend
>
> Supports forge and sponge permission APIs

## Utilities
AgeUtil
> A simple utility for converting a delta time into a friendly relative time message

DatChatFormatting
> A set of standardised chat colours for consistently formatting chat messages

DatCodec
> A few useful codecs for serialising common values
> 
> Currently Implemented: UUID, Enums, ChunkPos

DatMessageFormatter
> A utility for formatting strings into chat Components using variable replacements and shorthands for formatting directives

NotificationHelper
> A Utility with functions to simplify sending various types of notifications to a player
> 
> Supports displaying notifications as a title (in the middle of the player's screen), above the hotbar, and in chat.

DatTeleporter
> A simple ITeleporter implementation for moving the player to a specific location in another dimension 
>
> Does not create a portal and does not make a sound on teleporting


# Documentation
General help can be found at https://github.com/Dat-Developer-Mods/datmoddingapi/wiki

Javadocs can be found at https://api.datdeveloper.com/

# Using as a library
DatModdingAPI is available as a maven library at https://maven.datdeveloper.com/

To add to your project, add the following repository in `build.gradle`
```groovy
maven {
    url "https://maven.datdeveloper.com/releases"
}
```

Create a version variable in `gradle.properties`, changing `MINCRAFTVERSION` to your version of minecraft, and `VERSION`
to the latest available version of `Dat Modding API`
```groovy
datmoddingapi_version=MINECRAFTVERSION-VERSION
```

Then add the following dependency in `build.gradle`
```groovy
implementation fg.deobf("com.datdeveloper:datmoddingapi:${datmoddingapi_version}")
```
