// opens a block after `pluginManagement`
pluginManagement {
    // opens a block after `repositories`
    repositories {
        // opens a block after `google`
        google {
            // opens a block after `content`
            content {
                // calls `includeGroupByRegex` with arguments `("com\\.android.*")`
                includeGroupByRegex("com\\.android.*")
                // calls `includeGroupByRegex` with arguments `("com\\.google.*")`
                includeGroupByRegex("com\\.google.*")
                // calls `includeGroupByRegex` with arguments `("androidx.*")`
                includeGroupByRegex("androidx.*")
            // closes the block
            }
        // closes the block
        }
        // calls `mavenCentral` with arguments `()`
        mavenCentral()
        // calls `gradlePluginPortal` with arguments `()`
        gradlePluginPortal()
    // closes the block
    }
// closes the block
}
// opens a block after `plugins`
plugins {
    // calls `id` with arguments `("org.gradle.toolchains.foojay-resolver-conve…)`
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
// closes the block
}
// opens a block after `dependencyResolutionManagement`
dependencyResolutionManagement {
    // calls `set` on `repositoriesMode` with arguments `(RepositoriesMode.FAIL_ON_PROJECT_REPOS)`
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // opens a block after `repositories`
    repositories {
        // calls `google` with arguments `()`
        google()
        // calls `mavenCentral` with arguments `()`
        mavenCentral()
    // closes the block
    }
// closes the block
}

// assigns `rootProject.name` the value `"PROG7314"`
rootProject.name = "PROG7314"
// calls `include` with arguments `(":app")`
include(":app")
