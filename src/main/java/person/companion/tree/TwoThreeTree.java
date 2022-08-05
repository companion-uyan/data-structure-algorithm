package person.companion.tree;

import person.companion.array.SortArray;
import person.companion.stack.StackArray;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：自定义2-3树实现
 * 2-3树规定一个节点最多两个元素，大于两个的之后需要进行分裂
 * 分裂之后将值在中间的作为一个新的父节点并与分裂之前的父节点合并
 * 合并之后看新的合并节点是否又大于两个元素，如果大于则按照上述规则不断分裂合并
 *
 * author: companion
 * Write by: 2021/8/7 16:23
 */
@Data
public class TwoThreeTree<E extends Comparable<E>> {
    public static void main(String[] args) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();

        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 30000; i++) {
            // 产生5-35的随机数
            int num = (int) (Math.random() * 30000 + 5);
            arr.add(num);
            tree.add(num);
        }

        /*int arr[] = {78, 255, 173, 189, 140, 220, 77, 191, 8, 23, 162, 188, 33, 225, 51, 168, 220, 65, 284, 235, 23, 42, 122, 13, 192, 65, 212, 57, 131, 219};
        for (int i : arr) {
            tree.add(i);
        }*/

        // TODO 中序递归遍历的bug
        // System.out.println(tree.inorderTraversal());
        System.out.println(arr);
        System.out.println(tree.stackInorderTraversal());
    }

    private Node<E> root;

    /**
     * 定义一个合并节点，也就是2节点、3节点或者4节点
     * 一个2节点包含一个元素（两个子节点或者没有子节点）
     * 注意本例中两个节点的时候这里将大的节点放入max中
     * 一个3节点包含一小一大两个元素（三个子节点或者没有子节点）
     */
    @Data
    @NoArgsConstructor
    private static class Node<E> {
        // 值
        private SortArray<E> values;
        // 当前节点存储了几个元素
        private int size;
        // 标志当前节点是否刚完成分裂，需要与父节点进行合并
        private boolean isMerge;
        /**
         * 用于遍历时有判断有中间节点的父节点是否已经被访问过
         * 如果已经访问过则获取该节点的最大值并且将该节点出栈
         * 否则获取该节点最小值并且遍历中间节点
         */
        private boolean isVisited;
        // 左子节点
        private Node<E> min;
        // 中间子节点
        private Node<E> mid;
        // 右子节点
        private Node<E> max;

        Node(E e) {
            this.values = new SortArray<>();
            values.add(e);
        }
    }

    /**
     * 当前节点与父节点的关系
     */
    private enum NodeType {
        /**
         * 左节点
         */
        MIN(0),
        /**
         * 中间节点
         */
        MID(1),
        /**
         * 右节点
         */
        MAX(2);

        NodeType(int type) {
        }
    }

    /**
     * 获取指定位置元素
     *
     * @param index 索引
     * @return 指定位置元素
     */
    private E get(Node<E> node, int index) {
        return node.size > index ? node.values.get(index) : null;
    }

    /**
     * 获取node节点的最小元素
     *
     * @param node node节点的最小元素
     * @return 最小元素
     */
    private E getMin(Node<E> node) {
        return get(node, 0);
    }

    /**
     * 获取node节点的中间元素
     *
     * @param node node节点的中间元素
     * @return 中间元素
     */
    private E getMid(Node<E> node) {
        return get(node, 1);
    }

    private E getMax(Node<E> node) {
        return node.values.get(node.size - 1);
    }

    /**
     * 添加元素
     *
     * @param e 需要添加的元素
     */
    public void add(E e) {
        root = add(root, e);
    }

    /**
     * 需要添加的节点
     *
     * @param node 需要添加的节点
     * @param e    需要添加的元素
     * @return 添加之后的父节点
     */
    private Node<E> add(Node<E> node, E e) {
        if (node == null) {
            node = new Node<>(e);
            node.size++;
            return node;
        }

        // 如果有左子节点且当前元素小于节点最小值，向左子节点查找
        if (node.min != null && e.compareTo(getMin(node)) <= 0) {
            node.min = add(node.min, e);
            // 合并、拆分并且返回节点
            return merge(node, node.min, NodeType.MIN);
        }

        // 如果是4节点，需要查找中间子节点  一个3节点包含一小一大两个元素（三个子节点或者没有子节点）
        if (node.mid != null && e.compareTo(getMin(node)) > 0 && e.compareTo(getMid(node)) < 0) {
            node.mid = add(node.mid, e);
            return merge(node, node.mid, NodeType.MID);
        }

        // 如果有右子节点并且添加值大于右子节点，向右子节点查找
        if (node.max != null && e.compareTo(getMax(node)) > 0) {
            node.max = add(node.max, e);
            return merge(node, node.max, NodeType.MAX);
        }

        /*
         * 如果没有子节点，则需要在当前节点添加元素
         * 根据规则 可以知道只有没有左子节点就一定没有子节点
         */
        if (node.min == null) {
            // 添加元素
            node.values.add(e);
            node.size++;
            // 分裂
            node = split(node);
        }

        return node;
    }

    /**
     * 合并节点
     *
     * @param node  需要合并的节点
     * @param child 刚完成分裂的待合并节点
     * @param type  当前节点与父节点的关系
     * @return
     */
    private Node<E> merge(Node<E> node, Node<E> child, NodeType type) {
        if (child == null || !child.isMerge) {
            return node;
        }

        // 两节点合并为三节点
        if (node.size == 1) {
            return twoNodeMerge(node, child, type);
        }

        // 合并3节点为4节点
        return threeNodeMerge(node, child, type);
    }

    /**
     * 合并3节点为4节点
     *
     * @param node  需要合并的节点
     * @param child 需要合并的子节点
     * @param type  子节点与父节点的关系
     * @return 合并之后的父节点
     */
    private Node<E> threeNodeMerge(Node<E> node, Node<E> child, NodeType type) {
        // 将子节点的值赋给父节点
        node.values.add(getMin(child));
        node.size++;
        // 创建分裂后的最小节点
        Node<E> minNode = new Node<>(getMin(node));
        minNode.size++;
        // 创建分裂后的最大节点
        Node<E> maxNode = new Node<>(getMax(node));
        maxNode.size++;
        // 分裂父节点，新建一个节点用于存储父节点的中间值并将其作为新的父节点返回
        Node<E> parent = new Node<>(getMid(node));
        parent.size++;
        // 父节点与子节点关联
        parent.min = minNode;
        parent.max = maxNode;
        // 因为4节点需要分裂，因此父节点需要与上级节点进行再合并
        parent.isMerge = true;
        // 首先将分裂之后的四个节点的值保存起来，之后再对父节点进行分裂并返回新的父节点
        switch (type) {
            case MIN:
                // 将最小值分裂为新的最小节点并且将子节点的两个叶子节点赋给新的最小节点
                minNode.min = child.min;
                minNode.max = child.max;
                // 最大节点
                maxNode.min = node.mid;
                maxNode.max = node.max;
                break;
            case MID:
                // 最小节点赋值
                minNode.min = node.min;
                minNode.max = child.min;
                // 最大节点赋值
                maxNode.min = child.max;
                maxNode.max = node.max;
                break;
            case MAX:
                // 最小节点赋值
                minNode.min = node.min;
                minNode.max = node.mid;
                // 最大节点赋值
                maxNode.min = child.min;
                maxNode.max = child.max;
                break;
            default:
                break;
        }

        return parent;
    }

    /**
     * 合并节点，2节点合并为3节点
     * 2节点因为最多两个子节点，因此合并更加简单，不用考虑tmp节点
     *
     * @param node  需要合并的节点
     * @param child 需要合并的子节点
     * @param type  自己的与父节点的关系
     * @return 合并之后的父节点
     */
    private Node<E> twoNodeMerge(Node<E> node, Node<E> child, NodeType type) {
        // 将child的值添加到父节点
        node.values.add(getMin(child));
        node.size++;
        switch (type) {
            case MIN:
                /*
                将子节点的左右节点分别赋给父节点
                这一步之后child节点不再与2-3之间有联系
                因为node的min节点已经不再是child，下面同理
                 */
                node.min = child.min;
                node.mid = child.max;
                break;
            case MAX:
                node.mid = child.min;
                node.max = child.max;
                break;
            default:
                break;
        }
        return node;
    }

    /**
     * 分裂节点
     *
     * @param node 需要进行分裂的节点
     */
    private Node<E> split(Node<E> node) {
        // 如果不是四节点不需要进行分裂
        if (node == null || node.size < 3) {
            return node;
        }

        /*
         * 分裂：将中间值作为父节点的值
         * 最小值作为左子节点的值
         * 最大值作为右子节点的值
         */
        // 创建一个新的最小节点
        Node<E> minNode = new Node<>(getMin(node));
        minNode.size++;
        // 原左子节点赋给最小节点的左子节点
        minNode.min = node.min;
        // 原中间节点赋给最小节点的最大节点
        minNode.max = node.mid;
        // 连接中间值节点与最小值节点
        node.min = minNode;

        // 最大节点
        Node<E> maxNode = new Node<>(getMax(node));
        maxNode.size++;
        maxNode.max = node.max;
        node.max = maxNode;

        // 清除中间节点的最大值与最小值
        SortArray<E> value = new SortArray<>();
        value.add(getMid(node));
        node.values = value;
        node.size = 1;
        // 需要与父节点进行合并
        node.isMerge = true;

        return node;
    }

    // TODO 删除等其他方法补充

    /**
     * 通过栈实现中序遍历
     *
     * @return 数据集合
     */
    public List<E> stackInorderTraversal() {
        return stackInorderTraversal(root, new ArrayList<E>());
    }

    /**
     * 通过栈实现中序遍历，2-3树与AVL不同，有中间节点的时候取值顺序应该是：
     * 左子节点全部--父节点最小值--中间节点值--父节最大值--右子节点
     *
     * @param node 需要遍历的节点
     * @return 数据集合
     */
    private List<E> stackInorderTraversal(Node<E> node, List<E> list) {
        StackArray<Node<E>> stack = new StackArray<>();
        stack.push(node);
        while (stack.isNotEmpty()) {
            // 将当前节点与左子节点入栈
            while (stack.peek() != null) {
                node = node.min;
                stack.push(node);
            }

            // 空元素出栈
            stack.pop();
            if (stack.isEmpty()) {
                break;
            }

            node = stack.pop();
            // 到这里说明已经遍历了当前路径所有左子节点，需要将最左节点的值保存
            list.addAll(node.values.toList());

            // 父节点最小值--中间节点值--父节最大值--右子节点
            if (stack.isNotEmpty()) {
                if (stack.peek().mid != null && !stack.peek().isVisited) {
                    node = stack.peek();
                    // 父节点最小值
                    list.add(getMin(node));
                    node.isVisited = true;
                    // 中间元素入栈
                    node = node.mid;
                    stack.push(node);
                } else {
                    node = stack.pop();
                    // 没有中间元素则直接取父元素的值并且入栈右子节点
                    // 这里两种情况，一种是没有中间节点，则当前节点只有一个值，直接取出
                    // 另一种是有中间节点，那么中间节点的最小值已经再上面的if条件中取出，这里取最大值
                    list.add(getMax(node));
                    node = node.max;
                    stack.push(node);
                }
            }
        }

        return list;
    }

    /**
     * 深度优先遍历-中序遍历
     *
     * @return 值
     */
    private List<E> inorderTraversal() {
        return inorderTraversal(root, new ArrayList<E>());
    }

    /**
     * 深度优先遍历-中序遍历
     *
     * @param node 正在遍历的节点
     * @param list 数据存储的集合
     * @return 数据集合
     */
    private List<E> inorderTraversal(Node<E> node, ArrayList<E> list) {
        if (node == null) {
            return list;
        }

        inorderTraversal(node.min, list);
        /*
         * 2-3树与AVL不同，有中间节点的时候取值顺序应该是：
         * 左子节点全部--父节点最小值--中间节点值--父节最大值--右子节点
         */
        list.addAll(node.values.toList());
        inorderTraversal(node.mid, list);
        inorderTraversal(node.max, list);

        return list;
    }
}
