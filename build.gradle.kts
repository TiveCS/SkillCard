plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "com.github.tivecs"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://repo.xenondevs.xyz/releases")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("xyz.xenondevs.invui:invui:1.49")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.49")

    implementation("com.github.cryptomorin:XSeries:13.6.0")

    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-4")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-4")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-4")

    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation("mysql:mysql-connector-java:8.0.33")

}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(25) // Use Java 25 for compilation
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) // Target Java 17 bytecode
    }
}

// Configure Java compiler to also target Java 17
tasks.withType<JavaCompile> {
    sourceCompatibility = targetJavaVersion.toString()
    targetCompatibility = targetJavaVersion.toString()
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(25)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

// Development shadowJar - No relocation for hot swap debugging
val shadowJarDev by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    group = "shadow"
    description = "Create a combined JAR without relocation (for development/debugging)"

    from(sourceSets.main.get().output)
    configurations = listOf(project.configurations.runtimeClasspath.get())

    archiveClassifier.set("dev")

    // No relocation - classes stay at original packages for hot swap
}

// Production shadowJar - With relocation to avoid conflicts
val shadowJarProd by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    group = "shadow"
    description = "Create a combined JAR with relocation (for production)"

    from(sourceSets.main.get().output)
    configurations = listOf(project.configurations.runtimeClasspath.get())

    archiveClassifier.set("prod")

    // Relocate to avoid conflicts with other plugins
    relocate("com.cryptomorin.xseries", "com.github.tivecs.skillcard.internal.libs.xseries")
    relocate("xyz.xenondevs.invui", "com.github.tivecs.skillcard.internal.libs.invui")
    relocate("org.jetbrains.exposed", "com.github.tivecs.skillcard.internal.libs.exposed")
}

// Configure runServer to use dev jar by default (for hot swap debugging)
// To test with prod jar, manually change shadowJarDev to shadowJarProd below
tasks.runServer {
    minecraftVersion("1.20")

    // Switch between shadowJarDev (hot swap) and shadowJarProd (production test)
    pluginJars.from(shadowJarDev)

    downloadPlugins {
        url("https://download.luckperms.net/1611/bukkit/loader/LuckPerms-Bukkit-5.5.22.jar")
        hangar("ViaVersion", "5.7.0")
    }
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}