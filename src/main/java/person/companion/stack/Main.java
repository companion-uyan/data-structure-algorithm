package person.companion.stack;

import org.junit.Test;

/**
 * 功能描述：测试自定义栈
 *
 * author: companion
 * Write by: 2021/7/15 11:09
 */
public class Main {
    @Test
    public void test1() {
        StackArray<Integer> stack = new StackArray<>();
        stack.push(1);
        stack.push(2);
        stack.push(5);
        System.out.println(stack);
        System.out.println(stack.size());
        System.out.println(stack.peek());

        System.out.println();
        System.out.println(stack.pop());
        System.out.println(stack);
        System.out.println(stack.size());
        System.out.println(stack.peek());
    }
}
