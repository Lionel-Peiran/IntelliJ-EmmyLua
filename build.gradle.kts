import de.undercouch.gradle.tasks.download.*
import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream

plugins {
    id("org.jetbrains.intellij").version("0.6.5")
    id("org.jetbrains.kotlin.jvm").version("1.5.10")
    id("de.undercouch.download").version("3.4.3")
}

data class BuildData(
    val ideaSDKShortVersion: String,
    // https://www.jetbrains.com/intellij-repository/releases
    val ideaSDKVersion: String,
    val sinceBuild: String,
    val untilBuild: String,
    val archiveName: String = "EmmmmyLua",
    val jvmTarget: String = "1.8",
    val targetCompatibilityLevel: JavaVersion = JavaVersion.VERSION_11,
    val explicitJavaDependency: Boolean = true,
    val bunch: String = ideaSDKShortVersion,
    val instrumentCodeCompilerVersion: String = ideaSDKVersion
)

val buildDataList = listOf(
    BuildData(
        ideaSDKShortVersion = "212",
        ideaSDKVersion = "193.5233.102",
        sinceBuild = "192.0",
        untilBuild = "212.*",
        bunch = "212"
    )
)

val buildVersion = System.getProperty("IDEA_VER") ?: "212"

val buildVersionData = buildDataList.find { it.ideaSDKShortVersion == buildVersion }!!

val emmyDebuggerVersion = "1.0.16"

val resDir = "src/main/resources"

val isWin = Os.isFamily(Os.FAMILY_WINDOWS)

val isCI = System.getenv("APPVEYOR") != null


version = "NetEase-Special-Version-1.0"

task("downloadEmmyDebugger", type = Download::class) {
    src(arrayOf(
        "https://github.com/EmmyLua/EmmyLuaDebugger/releases/download/${emmyDebuggerVersion}/emmy_core.so",
        "https://github.com/EmmyLua/EmmyLuaDebugger/releases/download/${emmyDebuggerVersion}/emmy_core.dylib",
        "https://github.com/EmmyLua/EmmyLuaDebugger/releases/download/${emmyDebuggerVersion}/emmy_core@x64.zip",
        "https://github.com/EmmyLua/EmmyLuaDebugger/releases/download/${emmyDebuggerVersion}/emmy_core@x86.zip"
    ))

    dest("temp")
}

task("unzipEmmyDebugger", type = Copy::class) {
    dependsOn("downloadEmmyDebugger")
    from(zipTree("temp/emmy_core@x64.zip")) {
        into("x64")
    }
    from(zipTree("temp/emmy_core@x86.zip")) {
        into("x86")
    }
    destinationDir = file("temp")
}

project(":") {
    repositories {
        maven(url = "https://www.jetbrains.com/intellij-repository/releases")
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(fileTree(baseDir = "libs") { include("*.jar") })
        implementation("com.google.code.gson:gson:2.8.6")
        implementation("org.scala-sbt.ipcsocket:ipcsocket:1.3.0")
        implementation("org.luaj:luaj-jse:3.0.1")
        implementation("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")
    }

    sourceSets {
        main {
            java.srcDirs("gen", "src/main/compat")
            resources.exclude("debugger/**")
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = buildVersionData.targetCompatibilityLevel
        targetCompatibility = buildVersionData.targetCompatibilityLevel
    }

    tasks {
        buildPlugin {
            archiveBaseName.set(buildVersionData.archiveName)
            from(fileTree(resDir) { include("debugger/**") }) {
                into("/${project.name}/classes/")
            }
            from(fileTree(resDir) { include("!!DONT_UNZIP_ME!!.txt") }) {
                into("/${project.name}")
            }
        }

        compileKotlin {
            kotlinOptions {
                jvmTarget = buildVersionData.jvmTarget
            }
        }

        patchPluginXml {
            setSinceBuild(buildVersionData.sinceBuild)
            setUntilBuild(buildVersionData.untilBuild)
        }

        instrumentCode {
            setCompilerVersion(buildVersionData.instrumentCodeCompilerVersion)
        }
    }

    intellij {
        type = "IC"
        updateSinceUntilBuild = false
        downloadSources = false
        version = buildVersionData.ideaSDKVersion
        localPath = System.getenv("IDEA_HOME_${buildVersionData.ideaSDKShortVersion}")
        setPlugins("java")
        sandboxDirectory = "${project.buildDir}/${buildVersionData.ideaSDKShortVersion}/idea-sandbox"
    }
}