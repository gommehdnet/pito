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
import net.gommehd.pito.generator.classname.RuleRegistry;
import net.gommehd.pito.generator.jar.BuildTool;
import net.gommehd.pito.generator.jar.CraftBukkitJar;
import net.gommehd.pito.platform.source.Source;
import net.gommehd.pito.platform.source.Sources;

/**
 * @author David (_Esel)
 */
public class Generator {
    public static void main(String[] args) throws IOException, InterruptedException {
        File workDirectory = new File(".work");
        workDirectory.mkdir();

        Sources sources = new Sources(new File("source-location"));
        BuildTool buildTool = new BuildTool(workDirectory);
        RuleRegistry ruleRegistry = new RuleRegistry();
        for (Source source : sources.sources()) {
            File serverJar = buildTool.run(source);
            System.out.println("reading: " + serverJar);
            CraftBukkitJar jar = new CraftBukkitJar(serverJar);
            ruleRegistry.register(jar);
        }
        System.out.println(ruleRegistry.classes());
    }
}
