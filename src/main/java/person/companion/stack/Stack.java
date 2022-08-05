package person.companion.stack;

/**
 * 功能描述：自定义栈结构的接口
 *
 * author: companion
 * Write by: 2021/7/15 10:47
 */
public interface Stack<E> {
    // 入栈
    void push(E e);

    // 出栈
    E pop();

    // 获取栈顶元素
    E peek();

    // 获取数量
    int size();

    // 是否为空
    boolean isEmpty();

    // 是否不为空
    boolean isNotEmpty();

    // 出栈一个元素并返回当前栈顶
    E popPeek();
}
