package com.weareadaptive.hydraplatform.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle

class ConfigureCheckstylePlugin implements Plugin<Project> {
    @Override
    void apply(final Project target) {
        target.with {
            apply plugin: 'checkstyle'

            checkstyle {
                configFile = "$rootDir/config/checkstyle.xml" as File
                configProperties = ['suppressionFile': "$rootDir/config/suppressions.xml"]
            }

            tasks.withType(Checkstyle) {
                reports {
                    html.required = !System.getenv().containsKey("CI")
                    xml.required = System.getenv().containsKey("CI")
                }
            }
        }
    }
}
