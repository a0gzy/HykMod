import gg.essential.gradle.util.noServerRunConfigs

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

val modGroup: String by project
val modBaseName: String by project
version = "4.0.8"
//val modVersion: String = "4.0.3"
group = modGroup
base.archivesName.set("$modBaseName (${platform.mcVersionStr})")

loom {
    noServerRunConfigs()
    mixin {
        defaultRefmapName.set("mixins.hyk.refmap.json")
    }
    launchConfigs {
        getByName("client") {
//            property("mixin.debug.verbose", "true")
//            property("mixin.debug.export", "true")
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.hyk.json")
        }
    }
//    replace
//    minecraft {
//        replace("@MOD_VERSION@", version)
//    }
}


repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    compileOnly("gg.essential:essential-$platform:4479+ge389e52d8") //https://repo.essential.gg/repository/maven-public/gg/essential/essential-1.8.9-forge/maven-metadata.xml


    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    embed("gg.essential:loader-launchwrapper:1.1.3")
    embed("com.github.jagrosh:DiscordIPC:e29d6d8"){
            exclude("log4j")
    }

    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xno-param-assertions", "-Xjvm-default=all-compatibility")
    }
}

tasks.processResources {
    // Expansions are already set up for `version` (or `file.jarVersion`) and `mcVersionStr`.
    // You do not need to set those up manually.
}

tasks.jar {
    from(embed.files.map { zipTree(it) })

    manifest.attributes(
            mapOf(
                "ModSide" to "CLIENT",
                "FMLCorePluginContainsFMLMod" to "Yes, yes it does",
//                "hykVersion" to modVersion,
                "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                "TweakOrder" to "0",
                "MixinConfigs" to "mixins.hyk.json"
            )
    )
}
