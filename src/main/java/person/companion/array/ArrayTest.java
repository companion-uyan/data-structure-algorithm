package person.companion.array;

import org.junit.Test;

import java.util.TreeSet;

/**
 * 功能描述：测试自定义的Array数组
 *
 * author: companion
 * Write by: 2021/7/14 12:48
 */
public class ArrayTest {
    @Test
    public void test1() {
        try {
            Array<Object> array = new Array<>(2);
            array.addLast("dfa");
            array.addLast(5);
            // 扩容之前的数组
            System.out.println(array);
            array.add(1, "测试插入数据");
            // 扩容之后的数组
            System.out.println(array);
            System.out.println(array.contains("dfa"));
            System.out.println(array.indexOf("3"));
            array.replace(2, 9);
            System.out.println(array);
            System.out.println(array.delete(1));
            System.out.println(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试SortArray
     */
    @Test
    public void test2() {
        SortArray<Integer> integers = new SortArray<>();
        integers.add(5);
        integers.add(3);
        integers.add(7);
        integers.add(4);
        integers.add(0);
        System.out.println(integers);

        // 方式2：通过TreeSet实现
        TreeSet<Integer> integers1 = new TreeSet<>(
                (o1, o2) -> {
                    // 如果为空，将空数据后移
                    int compare = o1 == null ? -1 : o1.compareTo(o2);
                    // 如果相等，元素不动
                    return compare == 0 ? 1 : compare;

                });
        integers1.add(5);
        integers1.add(3);
        integers1.add(7);
        integers1.add(4);
        integers1.add(0);
        System.out.println(integers1);
    }
}
