/*
 * Copyright 2022 IndieLemon GmbH & Co. KG
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

repositories {
    mavenLocal()
    mavenCentral()
}

subprojects {
    plugins.withId("java") {
        //apply<MavenPublishPlugin>()
        apply(plugin = "groovy")
        repositories {
            mavenCentral()
        }
        tasks.withType<JavaCompile> {
            options.isFork = true
        }
        dependencies {
            add("testImplementation", "org.codehaus.groovy:groovy:3.0.8")
            add("testImplementation", "org.spockframework:spock-core:2.0-groovy-3.0")
            add("testImplementation", "junit:junit:4.13.2")
        }
    }
}