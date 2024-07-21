import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
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

dependencyLocking {
    lockFile.set(file("$projectDir/gradle/lockfiles/${rootProject.name}-${version}.lockfile"))
    lockAllConfigurations()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.4")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.liquibase:liquibase-core:4.23.0")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.springframework.boot:spring-boot-testcontainers:3.3.2")

    compileOnly("org.projectlombok:lombok:1.18.28")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.2")
    testImplementation("org.testcontainers:testcontainers:1.20.0")
    testImplementation("org.testcontainers:junit-jupiter:1.20.0")
    testImplementation("org.testcontainers:postgresql:1.20.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar {
    //Отключение генерации jar-package
    enabled = false
}

tasks.register("createpatch") {

    /**
     * Получение имени пользователя, которое хранится в .gitconfig
     *
     * В случае отсутствия или пустого значения выводится заглушка: "<AUTHOR NAME>"
     */
    fun getGitUserName(): String {
        val defaultAuthorText = "<AUTHOR NAME>"
        val outputStream = ByteArrayOutputStream()

        try {
            exec {
                executable = "git"
                args("config", "--get", "user.name")
                standardOutput = outputStream
            }

            return outputStream.toString().trimIndent().ifEmpty {
                println("Warn! The username is empty! Return the stub value: $defaultAuthorText")
                defaultAuthorText
            }
        } catch (e: Exception) {
            println("Error! Couldn't find git username! Return the stub value: $defaultAuthorText")
        }

        return defaultAuthorText
    }

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

    val versionToCatalogLiquibase = if (project.hasProperty("patchversion")) {
        project.property("patchversion")
    } else {
        project.version
    }

    val basePathToFile = project.layout.projectDirectory.dir("src/main/resources/db/changelog")
    val changelogName = "changelog.yaml"
    val patchName = "${System.currentTimeMillis()}_${project.property("patchname")}.sql"
    val gitUserName = getGitUserName()

    var defaultDataScript = """
        --liquibase formatted sql
        --changeset $gitUserName:$patchName
    """.trimIndent()

    if (project.hasProperty("type")) {
        defaultDataScript = defaultDataScript.plus("\n--type ${project.property("type")}")
    }

    if (project.hasProperty("task")) {
        defaultDataScript = defaultDataScript.plus("\n--comment ${project.property("task")}")
    }

    outputs.files(
            "$basePathToFile/$changelogName", //Master-changelog
            "$basePathToFile/$versionToCatalogLiquibase/$changelogName", //Local-changelog
            "$basePathToFile/$versionToCatalogLiquibase/$patchName" //Patch-script
    )

    doFirst {
        outputs.files.forEach { file ->
            if (file.name.equals(patchName)) {
                file.writeText(defaultDataScript) //Writing patch-script
            } else {
                if (file.absolutePath.contains(versionToCatalogLiquibase.toString())) { //Writing local-changelog
                    dataWriter(file, patchName)
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