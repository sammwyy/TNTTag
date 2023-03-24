package dev._2lstudios.tnttag.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dev._2lstudios.tnttag.utils.FileUtils;

public class LanguageExtractor {
    private static boolean shouldExtractEntry(JarEntry entry) {
        return entry.getName().startsWith("lang/") && entry.getName().endsWith(".yml");
    }

    public static void extractAll(File jar, File target) {
        if (!target.exists()) {
            target.mkdirs();
        }

        try (JarFile jarFile = new JarFile(jar)) {
            for (Enumeration<JarEntry> enums = jarFile.entries(); enums.hasMoreElements();) {
                final JarEntry entry = enums.nextElement();
                if (shouldExtractEntry(entry)) {
                    FileUtils.extractFile(new File(target, new File(entry.getName()).getName()), entry.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}