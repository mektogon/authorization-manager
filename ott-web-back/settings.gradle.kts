rootProject.name = "ott-web-back"
val root = rootDir.toString().substring(0,  rootDir.toString().length - rootProject.name.length)

include(":shared-database")
project(":shared-database").projectDir = file("${root}/shared-database")