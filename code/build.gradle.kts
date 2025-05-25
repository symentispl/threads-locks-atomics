import me.champeau.jmh.JMHPlugin
import me.champeau.jmh.JMHTask

plugins {
    id("java")
    id("io.github.reyerizo.gradle.jcstress") version "0.8.15"
    id("me.champeau.jmh") version "0.7.3"
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

jcstress {
    jcstressDependency = "org.openjdk.jcstress:jcstress-core:0.16"
    regexp = "pl\\.symentis\\.(concurrent\\.pool|lockfree)\\.*"
}

tasks.build {
    dependsOn(tasks.jmhClasses)
}

spotless {
    java {
        // Exclude generated files
        targetExclude("build/**")

        // Apply Palantir's Java formatter
        palantirJavaFormat()

        // Remove unused imports
        removeUnusedImports()

        // Make sure every file has the following copyright header
        licenseHeaderFile("spotless.license.java") // You'll need to create this file

        // Apply specific formatting rules
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
}

jmh {
    fork.set(1)
    warmupIterations.set(1)
    iterations.set(1)
    threadGroups.set(listOf(4, 4))
}