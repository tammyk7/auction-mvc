package com.weareadaptive.hydraplatform.gradle.plugins

import com.weareadaptive.hydraplatform.gradle.Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Pmd

class ConfigurePmdExtension {
    String[] ruleSets = []
}

class ConfigurePmd implements Plugin<Project> {

    @Override
    void apply(final Project target) {

        def extension = target.extensions.create('pmdConfig', ConfigurePmdExtension)

        target.afterEvaluate {
            target.with {
                apply plugin: 'pmd'

                dependencies {
                    pmd "com.weareadaptive.tools:pmd-code-analyzer:${Dependencies.Versions.PMD_CODE_ANALYZER}"
                }

                def customPmdJar = configurations.pmd.find {
                    it.toURI().toString().contains('pmd-code-analyzer')
                }

                def ruleSetFilesToUse = [
                        target.rootDir.toPath().resolve("config/pmd-ruleset.xml").toFile()
                ]
                extension.ruleSets.each {
                    ruleSetFilesToUse.add(resources.text.fromArchiveEntry(customPmdJar, it).asFile())
                }

                pmd {
                    ignoreFailures = System.getenv().containsKey("CI");
                    consoleOutput = true
                    ruleSets = [] // See: https://stackoverflow.com/a/53696963/14274365
                    ruleSetFiles = files(*ruleSetFilesToUse)
                    sourceSets = [sourceSets.main]
                    toolVersion = Dependencies.Versions.PMD
                    incrementalAnalysis = true
                }

                tasks.withType(Pmd) {
                    reports {
                        html.required = !System.getenv().containsKey("CI")
                        xml.required = System.getenv().containsKey("CI")
                    }
                }
            }
        }
    }
}
