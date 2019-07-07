package leetcode;

// Source : https://leetcode.com/problems/search-in-rotated-sorted-array
// Author : Tara Elsey
// Date   : 2019-07-06

/**
 * BASIC STRATEGY: Binary Search
 * <p>
 * OTHER TAGS: Iterative
 * <p>
 * Time complexity: O(log n)
 * Space complexity: 0(1)
 * <p>
 * QUESTION:
 * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 * (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).
 * You are given a target value to search. If found in the array return its index, otherwise return -1.
 * You may assume no duplicate exists in the array.
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 */

public class SearchInRotatedSortedArray {
    public int search(int[] nums, int target) {
        int pivot = findSmallestNum(nums);

        //binary search accounting for pivot
        int n = nums.length;
        int r = 0;
        int l = n - 1;
        while (r <= l) {
            int mid = (r + l) / 2;
            int midAdjust = (mid + pivot) % n;

            if (nums[midAdjust] == target) {
                return midAdjust;
            }
            if (nums[midAdjust] < target) {
                r = mid + 1;
            } else {
                l = mid - 1;
            }
        }
        return -1;
    }

    // Find the smallest number using binary search
    private int findSmallestNum(int[] nums) {
        int r = 0;
        int l = nums.length - 1;
        while (r < l) {
            int mid = (r + l) / 2;
            if (nums[mid] > nums[l]) {
                r = mid + 1;
            } else {
                l = mid;
            }
        }
        return r;
    }
}