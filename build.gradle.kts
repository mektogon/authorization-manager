import java.util.UUID.randomUUID

plugins {
    java
    war
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
}

group = "ru.dorofeev"
version = "01.000.00"

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

tasks.register("createPatch") {

    /**
     * Добавление записи в changelog.yaml в зависимости от наличия или отсутствия файла.
     *
     * Если файл отсутствует - добавляется базовый текст, иначе добавляется новая запись к существующему тексту.
     *
     * @param file файл, в который осуществляется запись
     * @param path относительный путь до changelog
     */
    fun dataWriter(file: File, path: String) {
        val defaultDataChangelog = """
        databaseChangeLog:
            - include:
                file: $path
                relativeToChangelogFile: true
    """.trimIndent()

        val defaultDataIncludePatch = """
    - include:
        file: $path
        relativeToChangelogFile: true     
    """.trimEnd()

        if (!file.exists()) {
            file.writeText(defaultDataChangelog)
        } else {
            file.writeText(file.readText().plus(defaultDataIncludePatch))
        }
    }

    if (!project.hasProperty("patchname")) {
        throw GradleException("The name of the patch is missing!")
    }

    val versionToCatalogLiquibase: Any? = if (project.hasProperty("patchversion")) {
        project.property("patchversion")
    } else {
        project.version
    }

    val basePathToFile = project.layout.projectDirectory.dir("src/main/resources/db/")
    val pathToCurrentPatchCatalog = "$basePathToFile/$versionToCatalogLiquibase"
    val changelogName = "changelog.yaml"
    val resultPatchName = "${project.property("patchname")}_${randomUUID()}.sql"

    var defaultDataScript = """
        --liquibase formatted sql
        --changeset <AUTHOR NAME>:$resultPatchName:<TASK NAME>
    """.trimIndent()

    if (project.hasProperty("type")) {
        defaultDataScript = defaultDataScript.plus("\n--type ${project.property("type")}")
    }

    outputs.files(
            "$basePathToFile/$changelogName", //Master-changelog
            "$pathToCurrentPatchCatalog/$changelogName", //Local-changelog
            "$pathToCurrentPatchCatalog/$resultPatchName" //Patch-script
    )

    doFirst {
        outputs.files.forEach { file ->
            if (file.name.equals(resultPatchName)) {
                file.writeText(defaultDataScript) //Writing patch-script
            } else {
                if (file.absolutePath.contains(versionToCatalogLiquibase.toString())) { //Writing local-changelog
                    dataWriter(file, "$versionToCatalogLiquibase/$resultPatchName")
                } else { //Writing to the master-changelog
                    if (!file.exists()) { //First writing
                        dataWriter(file, "$versionToCatalogLiquibase/$changelogName")
                    }
                    if (file.exists() && !file.readText().contains(versionToCatalogLiquibase.toString())) {
                        dataWriter(file, "$versionToCatalogLiquibase/$changelogName")
                    }
                }
            }
        }
    }
}