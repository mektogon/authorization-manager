import org.gradle.kotlin.dsl.war

plugins {
    java
    war
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
}

group = "ru.dorofeev"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.1.2")
    implementation("org.springframework:spring-web:6.0.11")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.1.2")
    implementation("org.liquibase:liquibase-core:4.23.0")

    compileOnly("org.projectlombok:lombok:1.18.28")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.18.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false //Disable generation plain-file for jar-package
}

tasks.getByName<War>("war") {
    enabled = false //Disable generation plain-file for war-package
}
