fun defineModule(path: String) {
    include(path)
    findProject(":$path")?.name = path.replace(":", "-")
}

rootProject.name = "pito"

defineModule("platform")
defineModule("generator")
defineModule("runtime:boostrap")
//defineModule("runtime:adapter")
