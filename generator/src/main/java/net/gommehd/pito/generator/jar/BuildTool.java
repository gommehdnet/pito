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

package net.gommehd.pito.generator.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import net.gommehd.pito.platform.source.VersionAttribute;
import net.gommehd.pito.platform.source.Source;

/**
 * @author David (_Esel)
 */
public class BuildTool {
    public static final String URL = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar";

    private final File jar;

    public BuildTool(File directory) throws IOException {
        this.jar = new File(directory, "BuildTools.jar");
        if (!jar.exists()) {
            InputStream in = new URL(URL).openStream();
            Files.copy(in, jar.toPath());
        }
    }

    public File run(Source source) throws IOException, InterruptedException {
        String rev = source.attribute(VersionAttribute.BUILD_TOOLS_VERSION);
        File output = new File(jar.getParentFile(), "craftbukkit-" + rev + ".jar");
        if (output.exists()) {
            return output;
        }
        String jdk = System.getProperty("bt_jdk_" + source.attribute(VersionAttribute.BUILD_TOOLS_JDK), "java");
        Process proc = new ProcessBuilder(jdk,
            "-jar",
            jar.getName(),
            "--compile", "craftbukkit",
            //"--generate-docs",
            //"--generate-source",
            //"--output-dir", jar.getParent(),
            "--rev", rev)
            .directory(jar.getParentFile())
            .inheritIO()
            .start();
        if (proc.waitFor() != 0) {
            throw new IllegalArgumentException("BuildTools failed: " + proc.exitValue());
        }
        if (!output.exists()) {
            throw new IllegalArgumentException("Output not found");
        }
        return output;
    }
}
