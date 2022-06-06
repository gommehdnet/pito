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

package net.gommehd.pito.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.gommehd.pito.generator.classname.BukkitMappings;
import net.gommehd.pito.generator.classname.PackageDetector;
import net.gommehd.pito.generator.jar.BuildTool;
import net.gommehd.pito.platform.source.Source;
import net.gommehd.pito.platform.source.Sources;

/**
 * @author David (_Esel)
 */
public class Generator {
    public static void main(String[] args) throws IOException, InterruptedException {
        File workDirectory = new File(".work");
        Files.walk(workDirectory.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);

        workDirectory.delete();
        workDirectory.mkdir();
        PackageDetector packageDetector = new PackageDetector();
        List<BukkitMappings> mappingsList = new ArrayList<>();
        Sources sources = new Sources(new File("source-location"));
        BuildTool buildTool = new BuildTool(workDirectory);
        for (Source source : sources.sources()) {
            buildTool.run(source);
            BukkitMappings mappings = new BukkitMappings(source);
            packageDetector.inspect(mappings);
            mappingsList.add(mappings);
        }
        packageDetector.fix();
        for (BukkitMappings mappings : mappingsList) {
            System.out.println(mappings.source());
            System.out.println(mappings.mapping());
        }

    }
}
