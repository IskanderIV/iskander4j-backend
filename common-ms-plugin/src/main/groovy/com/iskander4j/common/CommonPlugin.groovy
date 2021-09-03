package com.iskander4j.common

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin

class CommonPlugin implements Plugin<Project> {

    public static final String COMMON_EXTENSION_NAME = 'common'

    private static final JavaVersion JAVA_VERSION = JavaVersion.VERSION_11

    @Override
    void apply(Project project) {
        def extension = project.extensions.create(COMMON_EXTENSION_NAME, CommonPluginExtension)

        println "Project name: " + project.name
        println "Configurations: " + project.configurations
        project.plugins.apply(JavaPlugin)
        project.plugins.apply(GroovyPlugin)
        project.plugins.apply(SpringBootPlugin)
        project.plugins.apply(MavenPublishPlugin)
        project.plugins.apply(DependencyManagementPlugin)
        project.afterEvaluate {
            println "After evaluate!!! Begin"

            project.dependencies {
                //SPRING
                implementation 'org.springframework.boot:spring-boot-starter-actuator'
                implementation 'io.micrometer:micrometer-registry-prometheus'
                // UTILS
                implementation('com.google.guava:guava') {
                    exclude group: 'com.google.guava', module: 'guava'
                }
                implementation 'org.apache.commons:commons-lang3'
                implementation 'com.fasterxml.jackson.core:jackson-databind'
                // ELK
                implementation "net.logstash.logback:logstash-logback-encoder:6.3"
                // LOMBOK
                compileOnly 'org.projectlombok:lombok'
                annotationProcessor 'org.projectlombok:lombok'
                //JAXB
                runtimeOnly "com.sun.xml.bind:jaxb-core:2.3.0.1"
                runtimeOnly "com.sun.xml.bind:jaxb-impl:2.3.1"
                runtimeOnly "javax.xml.bind:jaxb-api:2.3.1"

                // TEST
                testImplementation('org.springframework.boot:spring-boot-starter-test')
                testImplementation 'org.junit.vintage:junit-vintage-engine'
                testImplementation 'org.junit.jupiter:junit-jupiter'
                testImplementation 'org.junit.jupiter:junit-jupiter-params'
                testImplementation 'org.awaitility:awaitility'
                testImplementation 'org.mockito:mockito-core'
                testCompileOnly 'org.projectlombok:lombok'
                testAnnotationProcessor 'org.projectlombok:lombok'
            }
            project.allprojects {
                test {
                    useJUnitPlatform()
                }
            }
            project.extensions.findByType(DependencyManagementExtension).imports {
                mavenBom "org.springframework.cloud:spring-cloud-dependencies:${extension.springCloudDependenciesVersion}"
//                mavenBom "org.testcontainers:testcontainers-bom:${extension.testContainersVersion}"
            }
            println project.plugins
            println "After evaluate!!! End"
        }
    }
}