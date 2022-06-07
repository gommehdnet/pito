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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.gommehd.pito.generator.classname.rule.ClassNameRule;
import net.gommehd.pito.generator.classname.rule.CraftBukkitRule;
import net.gommehd.pito.generator.classname.rule.MinecraftRule;
import net.gommehd.pito.generator.jar.CraftBukkitJar;
import net.gommehd.pito.generator.jar.JarBuilder;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;

/**
 * @author David (_Esel)
 */
public class RuleRegistry {
    private final ClassNameRule[] rules = new ClassNameRule[] {
        new CraftBukkitRule(),
        new MinecraftRule()
    };
    private final List<CraftBukkitJar> jars = new ArrayList<>();

    public void register(CraftBukkitJar jar) {
        jars.add(jar);
        for (ClassNameRule rule : rules) {
            rule.register(jar);
        }
    }

    public Map<String, ClassNode> classes() throws IOException {
        Map<String, ClassNode> classes = new HashMap<>();
        for (CraftBukkitJar jar : jars) {
            JarBuilder builder = new JarBuilder();
            builder.readFrom(jar, this);
            classes.putAll(builder.classes());
        }
        return classes;
    }

    public String name(String name) {
        for (ClassNameRule rule : rules) {
            name = rule.map(name);
        }
        return name;
    }

    public void remap(ClassReader reader, ClassNode node) {
        reader.accept(new ClassRemapper(node, new Remapper() {
            @Override
            public String map(String internalName) {
                String a = name(internalName);
                return a;
            }
        }), ClassReader.SKIP_FRAMES);
    }

}
