package com.musiy.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simple utility class
 */
public class Utils {

    /**
     * Get resource from JAR as string
     * @param clazz class
     * @param name  resource name
     * @param <T>   type of class
     * @return resource as string
     */
    public static <T> String getResourceAsString(Class<T> clazz, String name) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clazz.getResourceAsStream(name)))) {
            reader.lines().forEach(line -> sb.append(line).append('\n'));
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading resource file", e);
        }
    }
}
