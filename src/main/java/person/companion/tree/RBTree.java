package person.companion.tree;

import person.companion.stack.StackArray;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 功能描述：自定义红黑树实现
 *
 * author: companion
 * Write by: 2021/8/29 15:41
 */
@Data
@NoArgsConstructor
public class RBTree<E extends Comparable<E>> {
    private static final boolean BLACK = false;
    private static final boolean RED = true;

    public static void main(String[] args) {
        RBTree<Integer> tree = new RBTree<>();

        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            // 产生5-35的随机数
            int num = (int) (Math.random() * 30000 + 5);
            arr.add(num);
            tree.add(num);
        }

        /*int[] arr = {5327, 13337, 18199, 27188, 7417, 14053, 17277, 14159, 20953, 15605, 17954, 22699, 22845, 25687, 24720, 26697, 22273, 29756, 26913};
        for (int i : arr) {
            tree.add(i);
            // System.out.println(tree.stackInorderTraversal());
        }*/

        tree.delete(arr.get(158));
        Collections.sort(arr);
        System.out.println(arr);
        // System.out.println(tree.inorderTraversal());
        System.out.println(tree.inorderTraversal());
    }

    /**
     * 根节点
     */
    private Node<E> root;

    @Data
    @NoArgsConstructor
    // 解决debugger时stackOverFlow问题
    @ToString(exclude = "parent")
    private static class Node<E> {
        /**
         * 值
         */
        private E value;
        /**
         * 左节点
         */
        private Node<E> left;
        /**
         * 右节点
         */
        private Node<E> right;
        /**
         * 父节点
         */
        private Node<E> parent;
        /**
         * 该节点是否是红色节点
         */
        private boolean color;

        public Node(E value) {
            this.value = value;
        }
    }

    /**
     * 添加一个元素
     *
     * @param e 需要添加的元素
     */
    public void add(E e) {
        root = add(root, null, e);
        root.color = BLACK;
    }

    /**
     * 添加一个元素
     * 如果当前节点的父节点为黑节点，直接插入红节点
     * 如果父节点为红节点且叔叔节点为空，添加之后旋转
     * 如果父节点为红节点且叔叔节点不为空(不为空一定是红节点)，颜色翻转之后添加
     *
     * @param node 向当前节点或其子节点添加元素
     * @param e    需要添加的元素
     * @return 添加之后的新的父节点
     */
    private Node<E> add(Node<E> node, Node<E> parent, E e) {
        // 当前节点为空，新增一个节点并添加值
        if (node == null) {
            node = new Node<>();
            node.value = e;
            node.color = RED;
            node.parent = parent;
            return node;
        }

        // 节点不为空，查找子节点，从左子节点开始查找
        if (e.compareTo(node.value) <= 0) {
            node.left = add(node.left, node, e);
        }

        if (e.compareTo(node.value) > 0) {
            node.right = add(node.right, node, e);
        }

        // return rotation1(node);
        return rotation(node);
    }

    /**
     * 第一种旋转方式，总是将右边的节点旋转到左边
     *
     * @param node 需要旋转的节点
     * @return 旋转之后的父节点
     */
    private Node<E> rotation1(Node<E> node) {
        if (isRed(node.left) && isRed(node.right)) {
            // 颜色翻转
            return colorFlip(node);
        }

        //左旋
        if (isRed(node.right) && !isRed(node.left)) {
            node = leftRotation(node);
        }

        //右旋
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rightRotation(node);
        }

        return node;
    }

    /**
     * 当前节点是否是红节点
     *
     * @param node 节点颜色
     * @return 是否是红节点
     */
    private boolean isRed(Node<E> node) {
        return node != null && node.color;
    }

    /**
     * 根据旋转类型对当前节点进行旋转
     * 需要旋转的情况只有四种，满足条件是当前节点的子节点与孙子节点都是红色节点
     *
     * @param node 需要旋转的节点
     * @return 旋转之后的父节点
     */
    private Node<E> rotation(Node<E> node) {
        if (isRed(node.left) && isRed(node.right)) {
            // 颜色翻转
            return colorFlip(node);
        }

        if (isRed(node.left) && isRed(node.left.left)) {
            // 只需要进行一次右旋
            return rightRotation(node);
        }

        if (isRed(node.left) && isRed(node.left.right)) {
            // 先左旋再右旋
            node.left = leftRotation(node.left);
            return rightRotation(node);
        }

        if (isRed(node.right) && isRed(node.right.right)) {
            // 只需要进行一次右旋
            return leftRotation(node);
        }

        if (isRed(node.right) && isRed(node.right.left)) {
            // 先左旋再右旋
            node.right = rightRotation(node.right);
            return leftRotation(node);
        }

        // 其余情况不旋转，直接返回
        return node;
    }

    /**
     * 对当前节点及其子节点进行颜色翻转
     *
     * @param node 需要颜色翻转的节点
     * @return 进行颜色翻转以后的父节点
     */
    private Node<E> colorFlip(Node<E> node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;

        return node;
    }

    /**
     * 对节点进行左旋
     *
     * @param parent 需要旋转的节点
     * @return 旋转之后的父节点
     */
    private Node<E> leftRotation(Node<E> parent) {
        Node<E> right = parent.right;
        if (right.left != null) {
            right.left.parent = parent;
        }

        parent.right = right.left;
        right.left = parent;
        right.parent = parent.parent;
        parent.parent = right;

        boolean parentColor = parent.color;
        parent.color = right.color;
        right.color = parentColor;
        return right;
    }

    /**
     * 对节点进行右旋
     *
     * @param parent 需要旋转的父节点
     * @return 旋转之后的父节点
     */
    private Node<E> rightRotation(Node<E> parent) {
        Node<E> left = parent.left;
        if (left.right != null) {
            left.right.parent = parent;
        }

        parent.left = left.right;
        left.right = parent;
        left.parent = parent.parent;
        parent.parent = left;


        boolean parentColor = parent.color;
        parent.color = left.color;
        left.color = parentColor;

        return left;
    }

    /**
     * 递归实现中序遍历
     *
     * @return 中序遍历的值
     */
    public List<E> inorderTraversal() {
        return inorderTraversal(root, new ArrayList<>());
    }

    /**
     * 递归实现中序遍历
     *
     * @param node 当前正在遍历的几点
     * @param list 遍历之后的数据集合
     * @return 遍历之后的数据集合
     */
    private List<E> inorderTraversal(Node<E> node, ArrayList<E> list) {
        if (node == null) {
            return list;
        }

        inorderTraversal(node.left, list);
        list.add(node.value);
        inorderTraversal(node.right, list);

        return list;
    }

    /**
     * 使用栈实现中序遍历
     *
     * @return 数据集合
     */
    private List<E> stackInorderTraversal() {
        return stackInorderTraversal(root, new ArrayList<>());
    }

    /**
     * 使用栈实现中序遍历
     *
     * @param node 正在遍历的节点
     * @param list 数据集合
     * @return 数据集合
     */
    private List<E> stackInorderTraversal(Node<E> node, ArrayList<E> list) {
        StackArray<Node<E>> stack = new StackArray<>();
        // 根节点入栈
        stack.push(node);
        while (stack.isNotEmpty()) {
            // 左节点入栈
            while (stack.peek() != null) {
                node = node.left;
                stack.push(node);
            }

            // 空元素出栈
            stack.pop();
            // 存值
            if (stack.isNotEmpty()) {
                node = stack.pop();
                list.add(node.value);
                // 遍历右节点
                node = node.right;
                stack.push(node);
            }
        }

        return list;
    }

    /**
     * 从树中删除一个元素
     *
     * @param e 需要删除的元素
     */
    private void delete(E e) {
        delete(root, e);
    }

    /**
     * 从树中删除一个元素
     * 删除元素与普通二叉树相同，都需要寻找删除节点的后继节点进行替换
     * 替换之后的平衡与AVL不同，还有颜色的变化
     *
     * @param node 需要判断当前节点或其子节点是否有需要删除的元素
     * @param e    需要删除的元素
     */
    private void delete(Node<E> node, E e) {
        // 树中没有对应的元素，直接退出
        if (node == null) {
            return;
        }

        // 先递归找到对应的节点
        if (e.compareTo(node.value) < 0) {
            delete(node.left, e);
        }

        if (e.compareTo(node.value) > 0) {
            delete(node.right, e);
        }

        if (e.compareTo(node.value) == 0) {
            Node<E> minNode = node;
            if (node.left != null) {
                minNode = node.left;
            }

            if (node.right != null) {
                minNode = minNode(node.right);
            }

            // 如果有对应的元素，则需要根据情况找到对应的替换节点将值替换之后删除替换节点
            // 替换为后继节点的值
            node.value = minNode.value;
            // 注意第一次查找替换的后继节点一定没有子节点，也只有这时候有可能是红色节点，其他情况的替换节点都是黑色
            // 进行替换节点的平衡操作
            deleteBalance(minNode);
            // 删除替换节点的值
            if (minNode.parent.left == minNode) {
                minNode.parent.left = null;
            } else {
                minNode.parent.right = null;
            }
        }
    }

    /**
     * 查询最小节点
     *
     * @param node 需要查找的节点
     * @return 最小节点
     */
    private Node<E> minNode(Node<E> node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    /**
     * 删除之后对替换节点进行平衡操作
     *
     * @param replaceNode 替换节点
     */
    private void deleteBalance(Node<E> replaceNode) {
        // 如果替换节点为根节点，直接退出不做操作
        if (replaceNode == root) {
            return;
        }

        // 如果替换节点是红节点直接替换
        if (replaceNode.color) {
            return;
        }

        Node<E> parent = replaceNode.parent;
        // 判断当前节点与父节点的关系，如果是左节点
        if (parent.left == replaceNode) {
            // s红
            if (!isRed(parent) && isRed(parent.right)) {
                relateRotationNode(leftRotation(parent));
                // 再次替换节点，做第二次操作
                deleteBalance(replaceNode);
                // 为了简化之后的判断条件，这里直接return
                return;
            }

            // 父节点任意颜色，s黑色，sr红色----right-right
            if (!isRed(parent.right) && isRed(parent.right.right)) {
                parent.right.right.color = BLACK;
                // 通过左旋重新平衡
                relateRotationNode(leftRotation(parent));
                return;
            }

            // 兄弟节点黑色，sl红色，sr黑色----right-left
            if (isRed(parent.right.left)) {
                relateRotationNode(rightRotation(parent.right));
                // 这里需要旋转之后再进行颜色处理，因为颜色在旋转的时候会发生变化
                // 再次对替换节点进行操作
                deleteBalance(replaceNode);
                return;
            }

            // 兄弟节点与兄弟节点的子节点都是黑色
            if (!isRed(parent.right.right) && !isRed(parent.right.left)) {
                parent.right.color = RED;
                // 将父节点作为替换节点
                deleteBalance(parent);
                return;
            }
        }

        // 如果是右节点
        if (parent.right == replaceNode) {
            // 父节点黑色，左节点红色
            if (!isRed(parent) && isRed(parent.left)) {
                relateRotationNode(rightRotation(parent));
                // 再次替换节点，做第二次操作
                deleteBalance(replaceNode);
                return;
            }

            // s黑色，sl红色，sr任意颜色----left-left
            if (!isRed(parent.left) && isRed(parent.left.left)) {
                parent.left.left.color = BLACK;
                relateRotationNode(rightRotation(parent));
                return;
            }

            // s黑色，sl黑色，sr红色----left-right
            if (isRed(parent.left.right)) {
                relateRotationNode(leftRotation(parent.left));
                deleteBalance(replaceNode);
                return;
            }

            // s黑色，sl黑色，sr黑色
            if (!isRed(parent.left.left) && !isRed(parent.left.right)) {
                parent.left.color = RED;
                deleteBalance(parent);
            }
        }
    }

    /**
     * 为旋转之后的节点挂载父子节点关系
     * 因为删除的情况下通过旋转来平衡的话通过递归的方式来挂载新的父节点很麻烦
     *
     * @param parent 旋转之后的父节点
     */
    private void relateRotationNode(Node<E> parent) {
        // 如果是根节点
        if (parent.parent == null) {
            return;
        }

        if (parent.parent.left == parent.left || parent.parent.left == parent.right) {
            parent.parent.left = parent;
            return;
        }

        if (parent.parent.right == parent.left || parent.parent.right == parent.right) {
            parent.parent.right = parent;
        }
    }

}
