package com.supermarket.promows.utils;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtil {
    
        public static String slugifyFileName(String originalName) {
        // Separa nome e extensão
        String extension = "";
        int dotIndex = originalName.lastIndexOf(".");
        String namePart = (dotIndex != -1) ? originalName.substring(0, dotIndex) : originalName;
        extension = (dotIndex != -1) ? originalName.substring(dotIndex) : "";

        // 1. Remove acentos
        String normalized = Normalizer.normalize(namePart, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        slug = slug.toLowerCase(Locale.ROOT);

        // 3. Substitui espaços e _ por hífens
        slug = slug.replaceAll("[\\s_]+", "-");

        slug = slug.replaceAll("[^a-z0-9-]", "");

        // 5. Adiciona timestamp
        // String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        // slug = slug + "-" + timestamp;

        return slug + extension;
    }
}
