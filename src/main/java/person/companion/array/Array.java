package person.companion.array;

import java.util.Iterator;

/**
 * 功能描述：自定义的一个ArrayList类，学习算法与数据结构
 *
 * author: companion
 * Write by: 2021/7/14 11:01
 */

public class Array<E> {
    // 数组实际元素个数
    private int size;
    // 数组长度
    private int capacity;

    E[] data;

    Array() {
        // 默认初始化元素为10个
        this(10);
    }

    public Array(int capacity) {
        this.capacity = capacity;
        data = (E[]) new Object[this.capacity];
    }

    /**
     * 定义一个迭代器
     */
    private class ArrayIterator implements Iterator<E> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return Array.this.data != null && (index < size);
        }

        @Override
        public E next() {
            return Array.this.data[index++];
        }
    }

    /**
     * 在指定位置添加元素方法
     * 这里需要将插入的元素索引之后的全部元素向后移动一次
     *
     * @param index   添加的索引值
     * @param element 元素
     * @return 添加的元素
     */
    public void add(int index, E element) {
        // 判断数组是否已满
        if (size == capacity) {
            capacity = capacity << 1;
            resize(capacity);
        }

        if (index < 0 || index >= capacity) {
            throw new RuntimeException("index out of bound");
        }

        // 将插入元素之后的元素后挪一位
        for (int i = index; i < size; i++) {
            data[i + 1] = data[i];
        }

        size++;
        data[index] = element;
    }

    // 添加元素到最后一位
    public void addLast(E element) {
        add(size, element);
    }

    // 修改元素
    public void replace(int index, E e) {
        if (size - 1 < index) {
            throw new RuntimeException("index out of bound");
        }

        data[index] = e;
    }

    /**
     * 数组扩容
     */
    private void resize(int newCapacity) {
        // 扩容为原来的两倍
        E[] newData = (E[]) new Object[newCapacity];
        // 将数据存入新的数组
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    // 删除元素
    public E delete(int index) {
        if (size - 1 < index) {
            throw new RuntimeException("index out of bound");
        }

        E oldValue = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[--size] = null;
        // 如果容量小于三分之一减小数组长度，这里与扩容标准不同是为了防止频繁扩容与缩容
        if (size < capacity / 3) {
            capacity = capacity >> 1;
            resize(capacity);
        }

        return oldValue;
    }

    // 查找元素
    public E get(int index) {
        // 这里数组越界会自动抛出异常，不用我们手动抛出
        // 前面手动抛出是因为后面还有代码需要执行
        return data[index];
    }

    public int indexOf(E e) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(e)) {
                return i;
            }
        }

        return -1;
    }

    public boolean contains(E e) {
        return indexOf(e) > -1;
    }

    // 覆写打印方法
    @Override
    public String toString() {
        if (data == null) {
            return "null";
        }

        StringBuilder builder = new StringBuilder("[");
        ArrayIterator iterator = iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next()).append(",");
        }

        // 去除最后一个逗号并添加']'
        return builder.substring(0, builder.length() - 1) + "]";
    }

    public E deleteLast() {
        return delete(size - 1);
    }

    public int size() {
        return size;
    }

    public E getLast() {
        return data[size - 1];
    }

    public ArrayIterator iterator() {
        return new ArrayIterator();
    }
}
