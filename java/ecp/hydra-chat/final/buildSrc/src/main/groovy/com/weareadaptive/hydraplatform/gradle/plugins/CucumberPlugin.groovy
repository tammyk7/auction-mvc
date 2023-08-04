package com.weareadaptive.hydraplatform.gradle.plugins

import com.weareadaptive.hydraplatform.gradle.Dependencies
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.*
import org.gradle.process.ExecResult
import org.gradle.process.JavaExecSpec

class CucumberTask extends DefaultTask {

    @Input
    Map<String, ?> cucumberSystemProperties = [:]

    @Input
    List<String> cucumberArgs = ['src/test/resources']

    @Input
    List<String> cucumberJvmArgs = ['-ea', '--add-opens', 'java.base/sun.nio.ch=ALL-UNNAMED', '--add-opens', 'java.base/java.util=ALL-UNNAMED']

    @Input
    String cucumberMaxheapSize = "8g"

    @Classpath
    FileCollection cucumberClasspath = ((JavaExec) project.tasks.getByName('cucumberClasspathWrapper')).classpath

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    FileCollection inputMainFiles = project.extensions.getByType(SourceSetContainer).getByName('main').allSource

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    FileCollection inputTestFiles = project.extensions.getByType(SourceSetContainer).getByName('test').allSource

    @OutputDirectories
    FileCollection outputMainFiles = project.extensions.getByType(SourceSetContainer).getByName('main').output

    @OutputDirectories
    FileCollection outputTestFiles = project.extensions.getByType(SourceSetContainer).getByName('test').output

    @TaskAction
    void doIt() {
        JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class)
        ExecResult execResult = project.javaexec({ JavaExecSpec javaExecSpec ->
            javaExecSpec.with {
                mainClass = "io.cucumber.core.cli.Main"
                classpath = project.configurations.cucumberRuntime + javaExtension.sourceSets.main.output + javaExtension.sourceSets.test.output
                args = cucumberArgs
                cucumberSystemProperties.put('cucumber.publish.quiet', 'true')
                systemProperties = cucumberSystemProperties
                jvmArgs = cucumberJvmArgs
                maxHeapSize = cucumberMaxheapSize
                environment('HYDRA_VERSION', Dependencies.Versions.HYDRA)
            }
        })
        execResult.rethrowFailure()
    }
}

class CucumberPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        project.with {
            apply plugin: ConfigureJavaLibraryPlugin

            configurations {
                cucumberRuntime {
                    extendsFrom testImplementation
                }
            }
        }

        project.tasks.withType(CucumberTask) {
            dependsOn('build')
        }

        project.tasks
                .register('cucumberClasspathWrapper', JavaExec.class)
                .configure({ javaExec ->
                    javaExec.classpath = project.configurations.getByName('cucumberRuntime')
                })
    }
}
