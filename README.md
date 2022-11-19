A modding library for DatDeveloper

# Features
## Async Tasks
Async Handler
> A system for scheduling tasks to execute concurrently, optionally after a delay
> 
> Async Tasks execute on another thread, and thus can lead to race conditions if thread safety is not followed

## Delayed Events
> A system for scheduling tasks to execute after a condition is met
> 
> Events run on the server thread, on the server tick event, thus are thread safe

> Events are an implementation of IDelayedEvent, for an example of an event look at DelayedTeleportEvent

## Permissions
DatPermissions
> A permission abstraction system that allows testing permission nodes without worrying about the permission backend
>
> Supports forge and sponge permission APIs

## Utilities
DatChatFormatting
> A set of standardised chat colours for consistently formatting chat messages

DatTeleporter
> A simple ITeleporter implementation for moving the player to a specific location in another dimension 
>
> Does not create a portal and does not make a sound on teleporting

# Documentation
Javadocs can be found at https://api.datdeveloper.com/DatModdingAPI

# Using as a library
DatModdingAPI is available as a maven library at https://maven.datdeveloper.com/

To add to your project, add the following repository to your buildscript
```groovy
maven {
    url "https://maven.datdeveloper.com/releases"
}
```

Then add the following dependency, changing version to the latest available version
```groovy
implementation "com.datdeveloper:datmoddingapi:VERSION"
```