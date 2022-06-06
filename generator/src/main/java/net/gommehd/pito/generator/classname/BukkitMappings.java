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

package net.gommehd.pito.generator.classname;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.gommehd.pito.platform.source.VersionAttribute;
import net.gommehd.pito.platform.source.Source;

/**
 * @author David (_Esel)
 */
public class BukkitMappings {
    @Getter
    private final Source source;
    @Getter
    private final boolean sourceHasPackages;
    /* bukkit name -> obfuscated */
    private final Map<String, String> rawMapping;
    /* obfuscated -> bukkit name */
    private final Map<String, String> rawMappingReverse = new HashMap<>();
    /* our name -> obfuscated */
    @Getter
    private final Map<String, String> mapping;

    public BukkitMappings(Source source) {
        this.source = source;
        rawMapping = source.reverseMappings(VersionAttribute.MAPPING_BUKKIT);
        mapping = new HashMap<>(rawMapping);
        mapping.forEach((s, s2) -> rawMappingReverse.put(s2, s));
        sourceHasPackages = rawMapping.keySet().stream().anyMatch(s -> s.contains("/")
            && !rawMappingReverse.containsKey(s.replace('/', '.')));
    }

    public void changeMapping(String old, String newName) {
        String removed = mapping.remove(old);
        mapping.put(newName, removed);
    }

    public Collection<String> rawNames() {
        return rawMapping.keySet();
    }
}
