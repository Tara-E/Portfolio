package codesignal.golf;

// Source : https://app.codesignal.com/challenge/4uuajLD5XiSsYMzgD
// Author : Tara Elsey
// Date   : 2019-05-29

import java.util.Arrays;

/**
 * BASIC STRATEGY: Brute Force
 * <p>
 * OTHER TAGS: Iterative, Java 8, streams, Golf
 * <p>
 * TODO:
 * Time complexity:
 * Space complexity:
 * <p>
 * QUESTION:
 * Given two sorted arrays of integers, check if there is at least one element which occurs in both arrays.
 * <p>
 * Example
 * <p>
 * For arr1 = [1, 2, 3, 5] and arr2 = [1, 4, 5], the output should be
 * checkSameElementExistence(arr1, arr2) = true;
 * For arr1 = [1, 3, 5] and arr2 = [-2, 0, 2, 4, 6], the output should be
 * checkSameElementExistence(arr1, arr2) = false.
 */

public class CheckSameElementExistence {
    boolean checkSameElementExistence(int[] a, int[] b) {
        return Arrays.stream(a)
                .filter(x -> Arrays.stream(b).anyMatch(y -> y == x))
                .toArray().length > 0;
    }
}