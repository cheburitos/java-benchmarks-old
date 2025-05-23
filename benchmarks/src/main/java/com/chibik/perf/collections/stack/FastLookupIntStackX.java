package com.chibik.perf.collections.stack;

import org.jetbrains.annotations.TestOnly;

import java.util.List;

public class FastLookupIntStackX {

    // change to 0 to use hashmap for lookup
    private static final int HASH_MAP_LOOKUP_MIN_SIZE = Integer.parseInt(System.getProperty("stack.hash-map-lookup.threshold", "10"));

    private int size;
    private final IntStack stack = new IntStack();
    private final Int2IntHashMap lookupIndex = new Int2IntHashMap();

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(int value) {
        if (size < HASH_MAP_LOOKUP_MIN_SIZE) {
            return stack.contains(value);
        } else {
            return lookupIndex.get(value) > 0;
        }
    }

    public void push(int value) {
        stack.push(value);
        lookupIndex.addTo(value, 1);
        size++;
    }

    public int top() {
        return stack.top();
    }

    /**
     * Pops everything until finds a provided value. The found value is also popped.
     *
     * @param value a value to search for
     */
    public void popUpTo(int value) {
        while (!stack.isEmpty()) {
            int next = stack.top();
            pop();
            if (next == value) {
                return;
            }
        }
    }

    /**
     * Pops if top matches the provided value.
     */
    public boolean popIfTop(int expectedTop) {
        if (stack.popIfTop(expectedTop)) {
            removeFromIndex(expectedTop);
            return true;
        }
        return false;
    }

    public int pop() {
        int value = stack.pop();
        size--;
        removeFromIndex(value);
        return value;
    }

    private void removeFromIndex(int value) {
        int cnt = lookupIndex.get(value);
        if (cnt == 1) {
            lookupIndex.remove(value);
        } else {
            lookupIndex.put(value, cnt - 1);
        }
    }

    @TestOnly
    public List<Integer> toList() {
        return stack.toList();
    }
}
