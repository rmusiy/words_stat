package com.musiy.statistics;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

/**
 * Action to recursively calculate count of words
 * in the sentence.
 */
public class Counter extends RecursiveAction {

    /** Sentences in block to handle in one thread */
    private static final int THRESHOLD = 100;

    /** Read-only list */
    private List<String> list;

    /** Range 'from' inclusive */
    private int from;

    /** Range 'to' exclusive */
    private int to;

    /** Concurrent map to keep result */
    private ConcurrentHashMap<String, LongAdder> map;

    public Counter(List<String> list, int from, int to, ConcurrentHashMap<String, LongAdder> map) {
        this.list = list;
        this.from = from;
        this.to = to;
        this.map = map;
    }

    @Override
    protected void compute() {
        if (to - from < THRESHOLD) {
            processBlock();
        } else {
            int mid = (to - from) / 2;
            Counter left = new Counter(list, from, from + mid, map);
            Counter right = new Counter(list, from + mid, to, map);
            invokeAll(left, right);
            left.join();
            right.join();
        }
    }

    /**
     * Root of the system :)
     * Calculate number of words for current block: [from:to)
     */
    private void processBlock() {
        for (int i = from; i < to; i++) {
            String s = list.get(i);
            Arrays.stream(s.split("( ')|\\ |\\.|,|-|\\!|\\(|\\)|\\\"|;|\\?"))
                    .filter(word -> word != null
                            && !word.trim().isEmpty())
                    .map(String::toLowerCase)
                    .forEach(word -> map.computeIfAbsent(word, k -> new LongAdder()).increment());
        }
    }
}
