package leetcode;

// Source : https://leetcode.com/problems/generate-parentheses/
// Author : Tara Elsey
// Date   : 2019-05-29

import java.util.ArrayList;
import java.util.List;

/**
 * BASIC STRATEGY: Backtracking
 * <p>
 * OTHER TAGS: Recursive
 * <p>
 * Time complexity: O(4^n/n^(1/2))
 * Space complexity: 0(n)
 * <p>
 * QUESTION:
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 * <p>
 * For example, given n = 3, a solution set is:
 * <p>
 * [
 * "((()))",
 * "(()())",
 * "(())()",
 * "()(())",
 * "()()()"
 * ]
 */

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<>();
        generateParenthesisRecursive(list, "", 0, 0, n);
        return list;
    }

    private static void generateParenthesisRecursive(List<String> l, String curr, int open, int close, int n) {
        if (curr.length() == n * 2) {
            l.add(curr);
            return;
        }

        if (open < n) {
            generateParenthesisRecursive(l, curr + "(", open + 1, close, n);
        }

        if (close < open) {
            generateParenthesisRecursive(l, curr + ")", open, close + 1, n);
        }
    }
}