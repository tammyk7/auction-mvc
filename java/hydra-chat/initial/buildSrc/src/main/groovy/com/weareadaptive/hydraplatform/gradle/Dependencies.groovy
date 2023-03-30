package com.weareadaptive.hydraplatform.gradle

import org.gradle.api.Project

class Dependencies {
    class Versions {
        static final def FIND_SEC_BUGS = '1.11.0'
        static final def HYDRA = '3.138.0' // ./gradlew updateHydraClientPackages
        static final def JACKSON = '2.13.1'
        static final def JSON_ASSERT = '1.5.0'
        static final def JUNIT = '5.8.2'
        static final def PICO = '2.15'
        static final def SPOTBUGS = '4.7.3'
        static final def TESTCONTAINERS = '1.16.2'
        static final def TRUTH = '1.1.3'
    }

    static final def HYDRA_CLUSTER_CORE() {
        hydraModule("hydra-cluster-core")
    }

    static final def HYDRA_CODE_GEN_PLUGIN() {
        hydraModule("hydra-code-gen-plugin")
    }

    static final def HYDRA_LOGGING_API() {
        hydraModule("hydra-logging-api")
    }

    static final def HYDRA_LOGGING_IMPL() {
        hydraModule("hydra-logging-impl")
    }

    static final def HYDRA_LOGGING_SLF4J() {
        hydraModule("hydra-logging-slf4j-impl")
    }

    static final def HYDRA_PERF_HARNESS_CORE() {
        hydraModule("hydra-perf-harness-core")
    }

    static final def HYDRA_PLATFORM_CORE() {
        hydraModule("hydra-platform-core")
    }

    static final def HYDRA_PLATFORM_ENGINE() {
        hydraModule("hydra-platform-engine")
    }

    static final def HYDRA_PLATFORM_ENGINE_CLIENT() {
        hydraModule("hydra-platform-engine-client")
    }

    static final def HYDRA_PLATFORM_FIX_TESTING() {
        hydraModule("hydra-platform-fix-testing")
    }

    static final def HYDRA_PLATFORM_HISTORY_TESTING() {
        hydraModule("hydra-platform-history-testing")
    }

    static final def HYDRA_PLATFORM_STUB_IDP() {
        hydraModule("hydra-platform-stub-idp")
    }

    static final def HYDRA_PLATFORM_WEB() {
        hydraModule("hydra-platform-web")
    }

    static final def HYDRA_PLATFORM_CUCUMBER() {
        hydraModule("hydra-platform-cucumber")
    }

    static final def HYDRA_WS() {
        hydraModule("hydra-ws-cluster-gateway")
    }

    private static def hydraModule(String name) {
        return "com.weareadaptive:${name}:${Versions.HYDRA}"
    }

    static final def PICO() {
        return "org.picocontainer:picocontainer:${Versions.PICO}"
    }

    static final def JUNIT_API() {
        return "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}"
    }

    static final def JUNIT_ENGINE() {
        return "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}"
    }

    static final def TESTCONTAINERS() {
        return testcontainersModule("testcontainers")
    }

    static final def TESTCONTAINERS_POSTGRESQL() {
        return testcontainersModule("postgresql")
    }

    private static def testcontainersModule(String name) {
        return "org.testcontainers:${name}:${Versions.TESTCONTAINERS}"
    }

    static final def TRUTH() {
        return "com.google.truth:truth:${Versions.TRUTH}"
    }

    static final def SPOTBUGS() {
        return "com.github.spotbugs:spotbugs:${Versions.SPOTBUGS}"
    }

    static final def FIND_SEC_BUGS() {
        return "com.h3xstream.findsecbugs:findsecbugs-plugin:${Versions.FIND_SEC_BUGS}"
    }

    static final def JSON_ASSERT() {
        return "org.skyscreamer:jsonassert:${Versions.JSON_ASSERT}"
    }

    static final def toolVersion(Project rootProject, String tool, String defaultVersion) {
        def matcher = (rootProject.file('.tool-versions').getText() =~ ~/^${tool}\s+(.*)$/)
        def versions = []
        if (matcher.matches()) {
            versions = matcher.group(1).trim().split(~/\s+/)
        }
        return (!versions.isEmpty()) ? versions[0].trim() : defaultVersion
    }
}
