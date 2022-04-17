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

package net.gommehd.pito.platform.source;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author David (_Esel)
 */
public class Sources {
    private final Map<String, Source> sources = new HashMap<>();

    public Sources(File file) {
        for (File sourceFile : file.listFiles()) {
            Source source = new Source(sourceFile);
            sources.put(source.name(), source);
        }
    }

    public Collection<Source> sources() {
        return sources.values();
    }
}
