package leetcode;

import java.util.ArrayList;
import java.util.List;

// Source : https://leetcode.com/problems/letter-combinations-of-a-phone-number/
// Author : Tara Elsey
// Date   : 2019-05-26

/**
 * TODO: Fix this when possible.
 * This solution did not account for the 4-letter keys. It assumed all keys had 3 letters on them.
 * So, then a silly hack was added to make it work.
 * <p>
 * BASIC STRATEGY: Backtracking
 * <p>
 * OTHER TAGS: Iterative, Permutations
 * <p>
 * Time complexity: O(3^n * 4^m)
 * Space complexity: O(3^n * 4^m)
 * <p>
 * QUESTION:
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that
 * the number could represent.
 * <p>
 * Example:
 * Input: "23"
 * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
 */
public class LetterCombinationsOfAPhoneNumber {
    static int CHARS_PER_DIGIT = 3;

    public List<String> letterCombinations(String digits) {
        List<String> list = new ArrayList();

        for (Character c : digits.toCharArray()) {
            list = letterCombinations(list, c - '0');
        }

        return list;
    }

    public List<String> letterCombinations(List<String> prev, int next) {
        List<String> list = new ArrayList();
        int perDigit = CHARS_PER_DIGIT;
        int start = (next - 2) * perDigit;

        //oops :(
        if (next == 7 || next == 9) {
            perDigit++;
        }
        if (next > 7) {
            start++;
        }
        //end oops

        if (prev.isEmpty()) {
            prev.add("");
        }
        for (String s : prev) {
            for (int i = 0; i < perDigit; i++) {
                list.add(s + numToAlpha(start + i));
            }
        }
        return list;
    }

    public char numToAlpha(int i) {
        return (char) (i + 'a');
    }
}
