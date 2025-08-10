import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

val mindustryVersion = properties["mindustryVersion"]
val arcVersion = properties["arcVersion"]
//val jabelVersion = properties["jabelVersion"]

val modOutputDir = properties["modOutputDir"] as? String

val isWindows = System.getProperty("os.name").lowercase().contains("windows")
val sdkRoot = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
val d8 = if (isWindows) "d8.bat" else "d8"

val buildDir = layout.buildDirectory.get()

val debugGamePath = project.properties["debugGamePath"] as? String ?: ""
val debugGameJavaVersion = project.properties["debugGameJavaVersion"]!!

plugins {
    java
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.20"
    `maven-publish`
}

group = "com.github.LiuDai2011"
version = "alpha"

sourceSets.main.configure {
    java {
        srcDir("src")
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_18
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.xpdustry.com/mindustry")
    maven("https://www.jitpack.io")
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")

    compileOnly("com.github.Anuken.Arc:arc-core:$arcVersion")
    compileOnly("com.github.Anuken.Mindustry:core:$mindustryVersion")

//    annotationProcessor("com.github.Anuken:jabel:$jabelVersion")

    implementation(kotlin("stdlib-jdk8"))
}

allOpen {
    annotation("sequence.core.AllOpen")
}

allprojects {
    tasks.withType(JavaCompile::class.java) {
        options.compilerArgs.addAll(arrayOf("--release", "8"))
    }
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.github.Anuken.Arc") {
            useVersion("$mindustryVersion")
        }
    }
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName = "${project.name}-desktop.jar"

        from(rootDir) {
            include("mod.hjson")
        }

        from("assets/") {
            include("**")
        }

        from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) })
    }

    register("jarAndroid") {
        dependsOn("jar")

        doLast {
            try {
                if (sdkRoot == null) throw GradleException("No valid Android SDK found. Ensure that ANDROID_HOME is set to your Android SDK directory.");

                val platformRoot = File("$sdkRoot/platforms/").listFiles()
                    ?.sortedBy { it.name.replace("-R", "-30") }
                    ?.reversed()
                    ?.find { f -> File(f, "android.jar").exists() }
                    ?: throw GradleException("No android.jar found. Ensure that you have an Android platform installed.")

                val dependencies = (
                        configurations.compileClasspath.get().files +
                                configurations.runtimeClasspath.get().files +
                                setOf(File(platformRoot, "android.jar"))
                        ).joinToString(" ") { "--classpath $it" }

                val buildToolRoot = File("$sdkRoot/build-tools/").listFiles()
                    ?.sortedBy { it.name }
                    ?.reversed()
                    ?.find { f -> File(f, d8).exists() }
                    ?: throw GradleException("No d8 found. Ensure that you have an Android platform installed.")

                val buildTool = File(buildToolRoot, d8)

//                println("Android ready. dependencies: $dependencies")

                "$buildTool $dependencies --min-api 14 --output ${project.name}-android.jar ${project.name}-desktop.jar"
                    .execute(File("$buildDir", "libs"))
            } catch (e: Throwable) {
                if (e is Error) {
                    println(e.message)
                    return@doLast
                }

                println("[WARNING] d8 tool or platform tools was not found, if you was installed android SDK, please check your environment variable")
                println(e.message)

                delete(
                    files("${buildDir}/libs/${project.name}-android.jar")
                )

                val out = JarOutputStream(FileOutputStream("${buildDir}/libs/${project.name}-android.jar"))
                out.putNextEntry(JarEntry("non-androidMod.txt"))
                val reader = StringReader(
                    "this mod is don't have classes.dex for android, please consider recompile with a SDK or run this mod on desktop only"
                )

                var r = reader.read()
                while (r != -1) {
                    out.write(r)
                    out.flush()
                    r = reader.read()
                }
                out.close()
            }
        }
    }

    register("deploy", Jar::class) {
        dependsOn("jarAndroid")
        archiveFileName = "${project.name}.jar"

        from(
            zipTree("${buildDir}/libs/${project.name}-desktop.jar"),
            zipTree("${buildDir}/libs/${project.name}-android.jar")
        )

        doLast {
            if (!modOutputDir.isNullOrEmpty()) {
                copy {
                    into("$modOutputDir/")
                    from("$buildDir/libs/${project.name}.jar")
                }
            }
        }
    }

    register("deployDesktop", Jar::class) {
        dependsOn("jar")
        archiveFileName = "${project.name}.jar"

        from(zipTree("${buildDir}/libs/${project.name}-desktop.jar"))

        doLast {
            if (!modOutputDir.isNullOrEmpty()) {
                copy {
                    into("$modOutputDir/")
                    from("${buildDir}/libs/${project.name}.jar")
                }
            }
        }
    }

    register("debugMod", JavaExec::class) {
        dependsOn("classes")
        dependsOn("deployDesktop")

        mainClass = "-jar"
        args = listOf(
            debugGamePath,
            "-debug"
        )
        version = debugGameJavaVersion
    }
}

fun String.execute(path: File? = null, vararg args: Any?): Process {
    val cmd = split(Regex("\\s+"))
        .toMutableList()
        .apply { addAll(args.map { it?.toString() ?: "null" }) }
        .toTypedArray()
    val process = ProcessBuilder(*cmd)
        .directory(path ?: rootDir)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()

    if (process.waitFor() != 0) throw Error(InputStreamReader(process.errorStream).readText())

    return process
}

class Error(str: String) : RuntimeException(str)
