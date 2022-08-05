package person.companion.stack;

import java.util.Stack;

/**
 * 功能描述：
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-parentheses
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * author: companion
 * Write by: 2021/7/15 11:09
 */
class Solution {
    /**
     * 因为要放到leetcode上面，所以这里不使用自定义的StackArray类
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        /*Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char word = s.charAt(i);

            switch (word) {
                case '}':
                    if (!isMatch('{', stack)) {
                        return false;
                    }

                    break;
                case ']':
                    if (!isMatch('[', stack)) {
                        return false;
                    }

                    break;
                case ')':
                    if (!isMatch('(', stack)) {
                        return false;
                    }

                    break;
                default:
                    stack.push(word);
            }
        }

        return stack.empty();*/

        // 方式2：
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char word = s.charAt(i);
            if (word == '{' || word == '(' || word == '[') {
                stack.push(word);
            } else {
                if (stack.empty()) return false;
                if (word == '}' && stack.peek() != '{') return false;
                if (word == ')' && stack.peek() != '(') return false;
                if (word == ']' && stack.peek() != '[') return false;
                stack.pop();
            }
        }

        return stack.empty();
    }

    private static boolean isMatch(Character s, Stack<Character> stack) {
        if (stack.empty()) {
            return false;
        }

        if (stack.peek().equals(s)) {
            stack.pop();
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(isValid("]"));
    }
}