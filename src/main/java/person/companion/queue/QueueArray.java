package person.companion.queue;

import person.companion.array.Array;

/**
 * 功能描述：使用自定义的Array自定义一个队列
 *
 * author: companion
 * Write by: 2021/7/15 13:09
 */
public class QueueArray<E> implements Queue<E> {
    private Array<E> data;

    public QueueArray() {
        this(10);
    }

    public QueueArray(int capacity) {
        data = new Array<>(capacity);
    }

    /**
     * 插入元素
     *
     * @param e 需要插入的元素
     */
    @Override
    public void enQueue(E e) {
        data.addLast(e);
    }

    /**
     * 删除元素
     *
     * @return 需要删除的元素
     */
    @Override
    public E deQueue() {
        return data.delete(0);
    }

    /**
     * 获取队首的值
     *
     * @return 队首元素
     */
    @Override
    public E getFront() {
        return data.get(0);
    }

    /**
     * 队列长度
     *
     * @return 队列长度
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
        return data.size() == 0;
    }

    /**
     * 是否不为空
     *
     * @return 是否不为空
     */
    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }


    @Override
    public String toString() {
        return "QueueArray{" +
                "font:" + data +
                '}';
    }
}
