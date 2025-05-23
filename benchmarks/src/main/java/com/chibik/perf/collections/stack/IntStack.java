package com.chibik.perf.collections.stack;

import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * Simple int stack. Doesn't shrink by design.
 */
public class IntStack {

    private static final int DEFAULT_CAPACITY = 16;

    private int[] array;
    private int size;

    public IntStack() {
        this.array = new int[DEFAULT_CAPACITY];
        Arrays.fill(array, -1);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(int value) {
        ensureCapacity();
        this.array[size++] = value;
    }

    public int top() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return this.array[size - 1];
    }

    /**
     * Pops top if top matches the provided value.
     */
    public boolean popIfTop(int expectedTop) {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        int top = this.array[size - 1];
        if (top == expectedTop) {
            size--;
            return true;
        }
        return false;
    }

    public int pop() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return this.array[--size];
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            if (array[i] == value) {
                return true;
            }
        }
//        return array[0] == value || array[1] == value || array[2] == value || array[3] == value || array[4] == value;
        return false;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == array.length) {
            int[] newArray = new int[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            this.array = newArray;
        }
    }

    @TestOnly
    public List<Integer> toList() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(array[i]);
        }
        return result;
    }
}
