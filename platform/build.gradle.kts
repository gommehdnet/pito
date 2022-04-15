plugins {
    groovy
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    api("org.ow2.asm", "asm", "9.1")
}

