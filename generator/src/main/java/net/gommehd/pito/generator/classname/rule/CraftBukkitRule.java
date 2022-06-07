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

import net.gommehd.pito.generator.jar.CraftBukkitJar;

/**
 * @author David (_Esel)
 */
public class CraftBukkitRule implements ClassNameRule {
    private static final String CRAFT_BUKKIT = "org/bukkit/craftbukkit/";

    @Override
    public void register(CraftBukkitJar serverJar) {

    }

    @Override
    public String map(String current) {
        if (current.startsWith(CRAFT_BUKKIT + "v")) {
            current = current.substring(CRAFT_BUKKIT.length());
            current = current.substring(current.indexOf('/') + 1);
            current = CRAFT_BUKKIT + "shared/" + current;
        }
        return current;
    }
}
