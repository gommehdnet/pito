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
import java.util.ArrayList;
import java.util.List;
import net.gommehd.pito.generator.classname.BukkitMappings;
import net.gommehd.pito.generator.classname.PackageDetector;
import net.gommehd.pito.platform.source.Source;
import net.gommehd.pito.platform.source.Sources;

/**
 * @author David (_Esel)
 */
public class Generator {
    public static void main(String[] args) throws IOException {
        File workDirectory = new File(".work");
        workDirectory.delete();
        workDirectory.mkdir();
        PackageDetector packageDetector = new PackageDetector();
        List<BukkitMappings> mappingsList = new ArrayList<>();
        for (Source source : new Sources(new File("source-location")).sources()) {
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
