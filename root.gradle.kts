import gg.essential.gradle.util.versionFromBuildIdAndBranch

plugins {
    kotlin("jvm") version "1.6.0" apply false
    id("gg.essential.multi-version.root")
}

//version = "4.0.1"

preprocess {
    "1.8.9"(10809, "srg")
}