package person.companion.stack;

import person.companion.array.Array;

/**
 * 功能描述：通过自定义的Array类实现一个栈结构
 *
 * author: companion
 * Write by: 2021/7/15 10:54
 */
public class StackArray<E> implements Stack<E> {
    private Array<E> data;

    public StackArray() {
        this(10);
    }

    public StackArray(int capacity) {
        data = new Array(capacity);
    }

    /**
     * 入栈
     *
     * @param e 需要入栈的元素
     */
    @Override
    public void push(E e) {
        data.addLast(e);
    }

    /**
     * 出栈
     *
     * @return 出栈的元素
     */
    @Override
    public E pop() {
        return data.deleteLast();
    }

    /**
     * 获取栈顶元素
     *
     * @return 栈顶元素
     */
    @Override
    public E peek() {
        return data.getLast();
    }

    /**
     * 获取栈的长度
     *
     * @return 栈的长度
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * 是否为空
     *
     * @return 是否为空
     */
    @Override
    public boolean isEmpty() {
        return data == null || size() == 0;
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * 出栈一个元素并且返回当前栈顶
     *
     * @return 当前栈顶元素
     */
    @Override
    public E popPeek() {
        if (isEmpty()) return null;
        // 出栈
        pop();

        return isEmpty() ? null : peek();
    }

    @Override
    public String toString() {
        return "StackArray{" +
                "data=" + data +
                '}';
    }
}
