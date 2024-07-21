plugins {
    id("java")
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

subprojects {
    apply {
        plugin("org.springframework.boot")
    }
}

allprojects {
    apply {
        plugin("java")
        plugin("io.spring.dependency-management")
    }

    group = "ru.dorofeev"
    version = "01.000.00"

    java {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }

    repositories {
        mavenCentral()
    }

    dependencyLocking {
        lockFile.set(file("$projectDir/gradle/lockfiles/${rootProject.name}-${version}.lockfile"))
        lockAllConfigurations()
    }
}

tasks.jar {
    //Отключение генерации plain-file для jar-package
    enabled = false
}

tasks.bootRun {
    if (project.hasProperty("args")) {
        args(project.properties["args"]?.toString()?.split(","))
    }
}

dependencies {
    implementation(project(":shared-database"))

    implementation("org.springframework.boot:spring-boot-starter-web:3.3.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.2")
    implementation("org.springframework.boot:spring-boot-testcontainers:3.3.2")
    implementation("io.micrometer:micrometer-registry-prometheus:1.11.5")

    implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.2")
    testImplementation("org.testcontainers:junit-jupiter:1.20.0")
}

tasks.test {
    useJUnitPlatform()
}
