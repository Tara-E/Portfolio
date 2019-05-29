package leetcode;

import util.ListNode;

// Source : https://leetcode.com/problems/merge-two-sorted-lists/
// Author : Tara Elsey
// Date   : 2019-05-26

/**
 * BASIC STRATEGY:
 * Keep pointers to the current position in each list (starting at heads).
 * Keep pointer to the current position in new merged list.
 * Compare the two current values.
 * Set merged.next to min. Increment min. Leave other as-is.
 * Iterate to the end of both l1 and l2.
 * <p>
 * OTHER TAGS: Iterative, Easy
 * <p>
 * Time complexity: O(l1.length + l2.length)
 * Space complexity: 0(1)
 * <p>
 * QUESTION:
 * Merge two sorted linked lists and return it as a new list.
 * The new list should be made by splicing together the nodes of the first two lists.
 * <p>
 * Example:
 * <p>
 * Input: 1->2->4, 1->3->4
 * Output: 1->1->2->3->4->4
 */

public class MergeTwoSortedLists {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode n1 = l1;
        ListNode n2 = l2;
        ListNode mergedHead = null;
        ListNode merged = null;

        while (!(n1 == null && n2 == null)) {
            ListNode next;
            if (n1 != null && (n2 == null || n1.val < n2.val)) {
                next = n1;
            } else {
                next = n2;
            }

            if (merged != null) {
                merged.next = next;
            } else {
                mergedHead = next;
            }

            // increment
            merged = next;
            if (next == n1) {
                n1 = n1.next;
            } else {
                n2 = n2.next;
            }
        }
        return mergedHead;
    }
}