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

package net.gommehd.pito.generator.classname.rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.gommehd.pito.generator.jar.CraftBukkitJar;

/**
 * @author David (_Esel)
 */
public class MinecraftRule implements ClassNameRule {
    private final Map<String, String> captured = new HashMap<>();
    private final Set<String> collision = new HashSet<>();

    @Override
    public void register(CraftBukkitJar serverJar) {
        if (serverJar.fullPackageNames()) {
            for (String rawName : serverJar.names()) {
                capture(rawName);
            }
        }
    }

    @Override
    public String map(String current) {
        String replacement = captured.get(simpleFromFully(current));
        if (replacement != null) {
            current = replacement;
        }
        return current;
    }

    private void capture(String name) {
        String className = simpleFromFully(name);
        if (collision.contains(className)) {
            return;
        }
        System.out.println("capture: " + className);
        String collision = captured.put(className, name);
        if (collision != null && !collision.equals(name)) {
            this.collision.add(className);
            System.out.println("Name collision for " + className + " a=" + collision + " b=" + name);
            captured.remove(className);
        }
    }

    private static String simpleFromFully(String full) {
        int lastPackage = full.lastIndexOf('/');
        if (lastPackage != -1) {
            return full.substring(lastPackage + 1);
        }
        return full;
    }
}
