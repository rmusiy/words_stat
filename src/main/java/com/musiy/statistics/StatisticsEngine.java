package com.musiy.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Used as start point of application functionality.
 */
public class StatisticsEngine {

    /** Text stream to read */
    private InputStreamReader inputStreamReader;

    /** Used to keep word occurrences count */
    private ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>();

    /**
     * Init statistic engine by stream
     * @param inputStreamReader stream to read
     */
    public StatisticsEngine(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
    }

    /**
     * Calculate statistics and call consumer function over it
     * @param consumer function to consume statistic result
     * @throws IOException ошибка при чтении текстового потока
     */
    public void start(BiConsumer <String, Long> consumer) throws IOException {
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            List<String> list = reader.lines()
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());
            Counter counter = new Counter(list, 0, list.size(), map);
            ForkJoinPool pool = new ForkJoinPool(8);
            pool.invoke(counter);
            map.entrySet().stream()
                    .sorted(Comparator.comparingLong(f -> f.getValue().longValue()))
                    .forEach(entry -> consumer.accept(entry.getKey(), entry.getValue().longValue()));
        }
    }
}
