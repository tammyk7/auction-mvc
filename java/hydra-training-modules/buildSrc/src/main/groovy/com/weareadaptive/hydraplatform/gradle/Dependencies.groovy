package com.weareadaptive.hydraplatform.gradle

import org.gradle.api.Project

class Dependencies {
    class Versions {
        static final def HYDRA = '4.12.0'
        static final def JUNIT = '5.9.3'
        static final def PMD = '6.52.0'
        static final def PMD_CODE_ANALYZER = '0.2'
        static final def TESTCONTAINERS = '1.18.3'
        static final def GRPC = '1.56.0'
        static final def TOMCAT_ANNOTATIONS_API = '6.0.53'
        static final def VERTX= '4.4.3'
        static final def AERON= '1.41.2'
        static final def SLF4J= '2.0.7'
        static final def LOGBACK= '1.4.7'
        static final def AGRONA = '1.19.1'
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

    static final def HYDRA_PLATFORM_DATABASE_WRITER() {
        hydraModule("hydra-platform-database-writer")
    }

    static final def HYDRA_PLATFORM_ENGINE() {
        hydraModule("hydra-platform-engine")
    }

    static final def HYDRA_PLATFORM_ENGINE_CLIENT() {
        hydraModule("hydra-platform-engine-client")
    }

    static final def HYDRA_PLATFORM_CAMEL() {
        hydraModule("hydra-platform-camel")
    }

    static final def HYDRA_PLATFORM_FIX() {
        hydraModule("hydra-platform-fix")
    }

    static final def HYDRA_PLATFORM_FIX_TESTING() {
        hydraModule("hydra-platform-fix-testing")
    }

    static final def HYDRA_PLATFORM_HISTORY_TESTING() {
        hydraModule("hydra-platform-history-testing")
    }

    static final def HYDRA_PLATFORM_SAML() {
        hydraModule("hydra-platform-saml")
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

    static final def HYDRA_CODECS() {
        return hydraModule("hydra-codecs")
    }

    static final def HYDRA_MEMORY() {
        return hydraModule("hydra-memory")
    }

    static final def HYDRA_MEMORY_INDEXES() {
        return hydraModule("hydra-memory-indexes")
    }

    private static def hydraModule(String name) {
        return "com.weareadaptive:${name}:${Versions.HYDRA}"
    }


    static final def JUNIT_API() {
        return "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}"
    }

    static final def JUNIT_ENGINE() {
        return "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}"
    }

    private static def testcontainersModule(String name) {
        return "org.testcontainers:${name}:${Versions.TESTCONTAINERS}"
    }

    static final def GRPC_PROTOBUF() {
        return "io.grpc:grpc-protobuf:${Versions.GRPC}"
    }

    static final def GRPC_NETTY_SHADED() {
        return "io.grpc:grpc-netty-shaded:${Versions.GRPC}"
    }

    static final def GRPC_STUB() {
        return "io.grpc:grpc-stub:${Versions.GRPC}"
    }

    static final def TOMCAT_ANNOTATIONS_API() {
        return "org.apache.tomcat:annotations-api:${Versions.TOMCAT_ANNOTATIONS_API}"
    }

    static final def VERX_CORE() {
        return "io.vertx:vertx-core:${Versions.VERTX}"
    }

    static final def VERX_WEB() {
        return "io.vertx:vertx-web:${Versions.VERTX}"
    }

    static final def VERX_WEB_CLIENT() {
        return "io.vertx:vertx-web-client:${Versions.VERTX}"
    }

    static final def VERX_JUNIT() {
        return "io.vertx:vertx-junit5:${Versions.VERTX}"
    }

    static final def AERON_ALL() {
        return "io.aeron:aeron-all:${Versions.AERON}"
    }

    static final def SLF4J() {
        return "org.slf4j:slf4j-api:${Versions.SLF4J}"
    }

    static final def LOGBACK() {
        return "ch.qos.logback:logback-classic:${Versions.LOGBACK}"
    }

    static final def AGRONA() {
        return "org.agrona:agrona:${Versions.AGRONA}"
    }
}
