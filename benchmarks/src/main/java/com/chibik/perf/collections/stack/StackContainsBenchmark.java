package com.chibik.perf.collections.stack;

import com.chibik.perf.BenchmarkRunner;
import com.chibik.perf.util.AvgTimeBenchmark;
import com.chibik.perf.util.PrintAssembly;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 10, time = 1)
@AvgTimeBenchmark
@PrintAssembly(printMethod = "IntStack.contains")
public class StackContainsBenchmark {

    private static final int KEYS = 6;
    private static final int QUERY_KEYS = KEYS * 2;

    private FastLookupIntStackX stack;
    private int[] queryKeys;

    @Setup(Level.Iteration)
    public void setUp() {
        stack = new FastLookupIntStackX();

        List<Integer> keys = new ArrayList<>();

        for (int i = 0; i < KEYS; i++) {
            keys.add(i);
        }
        Collections.shuffle(keys);
        for (int k : keys) {
            stack.push(k);
        }

        queryKeys = new int[KEYS * 2];
        for (int i = 0; i < KEYS; i++) {
            queryKeys[i] = keys.get(i);
        }
        for (int i = KEYS; i < 2 * KEYS; i++) {
            queryKeys[i] = i;
        }
    }

    @Benchmark
    public boolean contains() {
        int key = queryKeys[ThreadLocalRandom.current().nextInt(QUERY_KEYS)];
        return stack.contains(key);
    }

    public static void main(String[] args) {
        BenchmarkRunner.run(StackContainsBenchmark.class);
    }
}
