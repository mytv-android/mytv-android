pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
}

rootProject.name = "天光云影"

include(":core:data")
include(":core:util")
include(":core:designsystem")
include(":tv")
include(":mobile")
include(":ijkplayer-java")
include(":allinone")

val mediaSettingsFile = file("../media/core_settings.gradle")
if (mediaSettingsFile.exists()) {
    (gradle as ExtensionAware).extra["androidxMediaModulePrefix"] = "media3:"
    apply(from = mediaSettingsFile)
}
