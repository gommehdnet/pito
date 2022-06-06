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
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 * @author David (_Esel)
 */
@ToString(of = "name")
public class Source {
    @Getter
    private final String name;
    private final Map<VersionAttribute, String> attributes = new HashMap<>();

    public Source(File file) {
        name = nameFromFile(file.getName());
        forEachMapping(file, (type, url) -> {
            attributes.put(VersionAttribute.valueOf(type), url);
        });
    }

    private File tempFile(VersionAttribute type) {
        return new File(".work", name + "-" + type.name());
    }

    @SneakyThrows
    private File ensureDownloaded(VersionAttribute type) {
        File file = tempFile(type);
        if (!file.exists()) {
            InputStream in = new URL(attributes.get(type)).openStream();
            Files.copy(in, tempFile(type).toPath());
        }
        return file;
    }

    public String attribute(VersionAttribute type) {
        return attributes.get(type);
    }

    public Map<String, String> mappings(VersionAttribute type) {
        Map<String, String> mapping = new HashMap<>();
        forEachMapping(ensureDownloaded(type), mapping::put);
        return mapping;
    }

    public Map<String, String> reverseMappings(VersionAttribute type) {
        Map<String, String> mapping = new HashMap<>();
        forEachMapping(ensureDownloaded(type), (s, s2) -> mapping.put(s2, s));
        return mapping;
    }

    @SneakyThrows
    private static void forEachMapping(File file, BiConsumer<String, String> consumer) {
        Files.lines(file.toPath()).forEach(line -> {
            int space = line.indexOf(' ');
            consumer.accept(line.substring(0, space), line.substring(space + 1));
        });
    }

    private static String nameFromFile(String file) {
        int extension = file.indexOf('.');
        return extension == -1
            ? file
            : file.substring(0, extension);
    }
}
