package com.weareadaptive.hydraplatform.gradle.plugins


import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigureJavaLibraryPlugin implements Plugin<Project> {
    @Override
    void apply(final Project target) {
        target.with {
            apply plugin: 'java'
            apply plugin: 'java-library'
            apply plugin: ConfigureJavaCompilationOptionsPlugin
            if (!target.properties['checkstyleDisabled']) {
                apply plugin: ConfigureCheckstylePlugin
            }

            jar {
                // Spring Boot puts jars inside of its uber-jar, so they need to have unique names
                archiveFileName = target.parent != null ? "${target.parent.name}-${target.name}.jar" : "${target.name}.jar"
            }

            test {
                useJUnitPlatform()
            }
        }
    }
}
