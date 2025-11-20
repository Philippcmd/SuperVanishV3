rootProject.name = "SuperVanish"

var MINECRAFT_VERSION = "1.21"

val MAJOR_VERSION = 3
val MINOR_VERSION = 2
val PATCH_VERSION = 2

var RELEASE_CHANNEL = ""
var BUILD = ""

gradle.rootProject {
    extra["mc-version"] = MINECRAFT_VERSION
    extra["major"] = MAJOR_VERSION
    extra["minor"] = MINOR_VERSION
    extra["patch"] = PATCH_VERSION
    extra["channel"] = RELEASE_CHANNEL
    extra["build"] = BUILD
}
