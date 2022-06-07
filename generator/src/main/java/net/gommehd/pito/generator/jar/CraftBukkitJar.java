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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class CraftBukkitJar {
    private static final String[] LOOKUP_JARS = new String[] {
        "META-INF/versions/craftbukkit-",
        "META-INF/libraries/bukkit-"
    };
    private static final String[] PACKAGES_INCLUDE = new String[] {
        "net/minecraft",
        "org/bukkit"
    };
    private static final String[] PACKAGES_EXCLUDE = new String[] {
        "org/bukkit/craftbukkit/bootstrap",
        "org/bukkit/craftbukkit/libs",
    };

    private final Set<String> names = new HashSet<>();
    private final File buildToolsOutput;
    private boolean fullPackageNames;
    private Map<ZipEntry, byte[]> entries;

    public boolean fullPackageNames() {
        ensureEntriesRead();
        return fullPackageNames;
    }

    public Set<String> names() {
        ensureEntriesRead();
        return names;
    }

    public Map<ZipEntry, byte[]> entries() {
        ensureEntriesRead();
        return entries;
    }

    @SneakyThrows
    private void ensureEntriesRead() {
        if (entries == null) {
            entries = new HashMap<>();
            try (FileInputStream stream = new FileInputStream(buildToolsOutput)) {
                lookupEntries(stream);
            }
        }
    }

    private boolean isExcluded(String candidate) {
        for (String exclude : PACKAGES_EXCLUDE) {
            if (candidate.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInclude(String candidate) {
        if (isExcluded(candidate)) {
            return false;
        }
        for (String include : PACKAGES_INCLUDE) {
            if (candidate.startsWith(include)) {
                return true;
            }
        }
        return false;
    }

    private void lookupEntries(InputStream inputStream) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            for (String jar : LOOKUP_JARS) {
                if (name.startsWith(jar)) {
                    System.out.println(jar);
                    fullPackageNames = true;
                    lookupEntries(zipInputStream);
                }
            }
            if (name.endsWith(".class") && isInclude(name)) {
                entries.put(entry, zipInputStream.readAllBytes());
                names.add(name.substring(0, name.length() - ".class".length()));
            }
        }
    }
}
