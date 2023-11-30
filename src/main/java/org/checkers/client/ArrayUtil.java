package org.checkers;

public class ArrayUtil{

    public static <T> void reverseArray(T[] array) {
        int start = 0;
        int end = array.length - 1;

        while (start < end) {
            // Swap elements at start and end indices
            T temp = array[start];
            array[start] = array[end];
            array[end] = temp;

            // Move indices towards the center
            start++;
            end--;
        }
    }

    public static <T> void reverseArray(T[][] matrix) {
        int start = 0;
        int end = matrix.length - 1;

        while (start < end) {
            // Swap rows at start and end indices
            T[] temp = matrix[start];
            matrix[start] = matrix[end];
            matrix[end] = temp;

            // Move indices towards the center
            start++;
            end--;
        }
    }
}
