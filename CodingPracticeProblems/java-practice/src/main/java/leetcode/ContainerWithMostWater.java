package leetcode;

// Source : https://leetcode.com/problems/container-with-most-water/
// Author : Tara Elsey
// Date   : 2019-05-29

/**
 * BASIC STRATEGY: Two Pointers
 * <p>
 * OTHER TAGS: Iterative
 * <p>
 * Time complexity: O(n)
 * Space complexity: 0(1)
 * <p>
 * QUESTION:
 * Given n non-negative integers a1, a2, ..., an , where each represents a point at coordinate (i, ai). n vertical
 * lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0). Find two lines, which together
 * with x-axis forms a container, such that the container contains the most water.
 * Example:
 * Input: [1,8,6,2,5,4,8,3,7]
 * Output: 49
 */

public class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int i = 0, j = height.length - 1;
        int max = 0;
        while (i < j) {
            int h = Math.min(height[i], height[j]);
            int area = h * (j - i);
            if (area > max) {
                max = area;
            }

            if (height[i] >= height[j]) {
                j--;
            } else {
                i++;
            }
        }

        return max;
    }
}