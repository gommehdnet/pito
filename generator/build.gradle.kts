import org.gradle.language.nativeplatform.internal.Dimensions

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

plugins {
    id("pito.java-conventions")
    id("application")
}

application {
    mainClass.set("net.gommehd.pito.generator.Generator")
}

dependencies {
    implementation(project(":platform"))
}
