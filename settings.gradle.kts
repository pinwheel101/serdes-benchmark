rootProject.name = "serdes-benchmark"

include("serdes-core")
include("serdes-avro")
include("serdes-protobuf")
include("serdes-benchmark")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
        maven("https://packages.confluent.io/maven/")
    }
}
