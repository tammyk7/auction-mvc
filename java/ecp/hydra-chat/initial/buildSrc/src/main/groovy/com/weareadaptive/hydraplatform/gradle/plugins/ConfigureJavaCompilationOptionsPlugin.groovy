package com.weareadaptive.hydraplatform.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class ConfigureJavaCompilationOptionsPlugin implements Plugin<Project> {
    @Override
    void apply(final Project target) {
        target.with {
            sourceCompatibility = 17
            targetCompatibility = 17

            tasks.withType(JavaCompile) {
                options.encoding = 'UTF-8'
                options.compilerArgs <<
                        "-Xlint:deprecation" <<
                        "-Xlint:dep-ann" <<
                        "-Xlint:unchecked" <<
                        "-Xlint:divzero" <<
                        "-Xlint:empty" <<
                        "-Xlint:fallthrough" <<
                        "-Xlint:finally" <<
                        "-Xlint:options" <<
                        "-Xlint:overrides" <<
                        "-Xlint:path" <<
                        "-Xlint:processing" <<
                        "-Xlint:rawtypes" <<
                        "-Xlint:static"
                "-Werror"
            }
        }
    }
}
