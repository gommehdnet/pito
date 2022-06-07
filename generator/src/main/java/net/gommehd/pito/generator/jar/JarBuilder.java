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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.Getter;
import net.gommehd.pito.generator.classname.RuleRegistry;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

/**
 * @author David (_Esel)
 */
public class JarBuilder {
    @Getter
    private final Map<String, ClassNode> classes = new HashMap<>();

    public void addClass(ClassNode classNode) {
        classes.put(classNode.name, classNode);
    }

    public void readFrom(CraftBukkitJar jar, RuleRegistry registry) throws IOException {
        jar.entries().forEach((zipEntry, bytes) -> {
            String name = zipEntry.getName();
            name = registry.name(name.substring(0, name.length() - ".class".length()));
            ClassReader reader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            registry.remap(reader, classNode);
            classes.put(name, classNode);
            System.out.println(name + " " + classNode.name);
        });
    }

    public void writeTo(File outputFile) throws IOException {
        outputFile.getParentFile().mkdirs();
        try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(outputFile))) {
            System.out.println("Writing back " + classes.size() + " classes");
            for (Map.Entry<String, ClassNode> entry : classes.entrySet()) {
                ClassWriter writer = new ClassWriter(0);
                try {
                    ClassNode node = entry.getValue();
                    node.accept(writer);
                } catch (Throwable ex) {
                    System.out.println("Skip failed class " + entry.getKey() + ": " + ex.getMessage());
                    continue;
                }
                zipStream.putNextEntry(new ZipEntry(entry.getKey()));
                zipStream.write(writer.toByteArray());
                zipStream.closeEntry();
            }
        }
    }
}
