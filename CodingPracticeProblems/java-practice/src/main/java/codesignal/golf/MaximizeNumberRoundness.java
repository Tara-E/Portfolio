package codesignal.golf;

// Source : https://app.codesignal.com/challenge/h5rpQQTrM3cCCevmq
// Author : Tara Elsey
// Date   : 2019-05-29

import java.util.Arrays;

/**
 * BASIC STRATEGY: Two Pointers
 * <p>
 * OTHER TAGS: Iterative, Golf
 * <p>
 * TODO:
 * Time complexity:
 * Space complexity:
 * <p>
 * QUESTION:
 * Define an integer's roundness as the number of trailing zeros in it. Sometimes it is possible to increase a number's roundness by swapping two of its digits.
 * <p>
 * Given an integer n, find the minimum number of swaps required to maximize n's roundness.
 * <p>
 * Example
 * <p>
 * For n = 902200100, the output should be
 * maximizeNumberRoundness(n) = 1.
 * <p>
 * It's enough to swap the leftmost 0 with 1.
 * <p>
 * For n = 11000, the output should be
 * maximizeNumberRoundness(n) = 0.
 * <p>
 * n already has the maximum roundness possible.
 */

public class MaximizeNumberRoundness {
    int maximizeNumberRoundness(int n) {
        char[] a = ("" + n).toCharArray();

        int r = 0, l = a.length-1, s = 0;

        while (l > r) {
            int x = a[r]-'0', y = a[l]-'0';

            if(y == 0)
                l--;
            else if(x == 0){
                s++;
                r++;
                l--;
            }
            if(x != 0)
                r++;

        }
        return s;
    }
}