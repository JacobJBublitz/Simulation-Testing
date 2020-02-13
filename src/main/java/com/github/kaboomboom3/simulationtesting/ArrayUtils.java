package com.github.kaboomboom3.simulationtesting;

public class ArrayUtils {
    public static int[] arange(int stop) {
        return arange(0, stop, 1);
    }

    public static int[] arange(int start, int stop) {
        return arange(start, stop, 1);
    }

    public static int[] arange(int start, int stop, int step) {
        int delta = stop - start;
        int[] array = new int[delta / step];

        for (int i = 0; i < array.length; i++) {
            array[i] = start + i * step;
        }

        return array;
    }
}
