package person.companion.queue;

/**
 * 功能描述：自定义队列接口
 *
 * author: companion
 * Write by: 2021/7/15 13:07
 */
public interface Queue<E> {
    // 插入操作
    void enQueue(E e);

    // 删除操作
    E deQueue();

    // 获取第一个元素（队首）
    E getFront();

    // 获取长度
    int size();

    // 是否为空
    boolean isEmpty();

    // 是否不为空
    boolean isNotEmpty();
}
