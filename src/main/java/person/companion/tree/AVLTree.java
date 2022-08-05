package person.companion.tree;

import person.companion.queue.QueueArray;
import person.companion.stack.StackArray;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：自定义平衡二叉搜索树
 * 这里有几点重要结论：
 * 1.不平衡节点一定是插入节点的上级节点
 * 2.对于旋转的四种情况，一定是从第一个平衡因子等于2的节点开始连续两个子节点一共三个节点构成，并且这三个节点一定是插入节点的父节点或插入节点本身
 *
 * author: companion
 * Write by: 2021/7/26 20:37
 */
public class AVLTree<E extends Comparable<E>> {
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 0; i < 30; i++) {
            tree.insert(i);
        }

        tree.delete(15);
        System.out.println(tree.inorderTraversal());

        /*// 深度优先遍历-前序遍历
        System.out.println(tree.preorderTraversal());
        // 栈-前序遍历
        System.out.println(tree.stackPreorderTraversal());
        // 深度优先遍历-中序遍历
        System.out.println(tree.inorderTraversal());
        // 栈-中序遍历
        System.out.println(tree.stackInorderTraversal());
        // 深度优先遍历-后序遍历
        System.out.println(tree.postorderTraversal());
        // 栈-后序遍历
        System.out.println(tree.stackPostorderTraversal());
        // 栈-后序遍历方式2
        System.out.println(tree.stackPostorderTraversal1());
        // 广度优先遍历-队列
        System.out.println(tree.queueTraversal());*/

        // 删除节点
        /*for (int i = 1; i < 16; i++) {
            tree.delete(i);
            System.out.println(tree.inorderTraversal());
        }*/
    }

    /**
     * 根节点
     */
    private Node<E> root;

    /**
     * 定义Node类用于存放元素
     */
    @Data
    private static class Node<E> {
        /**
         * 权
         */
        private E value;

        /**
         * 高度
         */
        private int height;

        /**
         * 左子节点
         */
        private Node<E> left;

        /**
         * 右子节点
         */
        private Node<E> right;
    }

    /**
     * 获取当前节点的高度
     *
     * @param node 需要获取高度的节点
     * @return 节点高度
     */
    private int height(Node node) {
        if (node == null) {
            return 0;
        }

        return node.getHeight();
    }


    /**
     * 计算节点高度
     *
     * @return 节点高度
     */
    private int calcHeight(Node node) {
        if (node == null) {
            return 0;
        }

        return Math.max(height(node.getLeft()), height(node.getRight())) + 1;
    }

    /**
     * 查找当前节点下的最大节点
     *
     * @return 最大节点
     */
    private Node<E> maxNode() {
        return maxNode(root);
    }

    /**
     * 查找当前节点下的最大节点
     *
     * @return 最大节点，该节点一定在当前节点的最右边
     */
    private Node<E> maxNode(Node<E> node) {
        if (node == null) {
            return null;
        }

        // 遍历右节点，因为只返回最后一个节点的值，所以不使用递归
        while (node.right != null) {
            node = node.right;
        }

        return node;
    }

    /**
     * 查询最小节点
     *
     * @return 最小节点
     */
    public Node<E> minNode() {
        return minNode(root);
    }

    /**
     * 根据当前节点查询最小节点
     *
     * @param node 根节点
     * @return 最小节点
     */
    private Node<E> minNode(Node<E> node) {
        if (node == null) {
            return null;
        }

        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    /**
     * 插入元素到二叉树
     *
     * @param e 待插入的元素
     */
    public void insert(E e) {
        root = insert(root, e);
    }

    /**
     * 插入元素
     *
     * @param node 在该节点或该节点的子节点存放元素
     * @param e    元素
     * @return 插入节点
     */
    private Node<E> insert(Node<E> node, E e) {
        // 为空说明已经找到需要插入的地方，直接创建新节点就行
        if (node == null) {
            node = new Node<>();
            node.setValue(e);
            node.setHeight(1);
            return node;
        }

        // 如果不为空，则需要与当前节点判断大小
        if (e.compareTo(node.value) < 0) {
            // 查找左子节点
            node.left = insert(node.left, e);

            return balance(node);
        }

        if (e.compareTo(node.value) >= 0) {
            // 查找右子节点
            node.right = insert(node.right, e);
            return balance(node);
        }

        return node;
    }

    /**
     * 平衡节点
     *
     * @param node 需要进行平衡的节点
     * @return 平衡之后的根节点
     */
    private Node<E> balance(Node<E> node) {
        // 重新计算当前节点的高度，因为是递归查询，这样就能做到将插入节点的路径上的所有父节点的高度重新计算
        node.setHeight(calcHeight(node));
        // 如果该节点平衡因子大于2，则需要进行旋转，旋转与当前节点及其子节点，孙子节点有关
        if (height(node.left) - height(node.right) == 2) {
            // 注意删除时会有两边高度相等的情况，这时候可以选择一次旋转效率更高
            if (height(node.left.left) >= height(node.left.right)) {
                // 只需要一次右旋即可
                return rightRotation(node);
            }

            if (height(node.left.left) <= height(node.left.right)) {
                // 先左旋再右旋
                node.left = leftRotation(node.left);
                return rightRotation(node);
            }
        }

        // 插入到节点右边按的情况
        if (height(node.right) - height(node.left) == 2) {
            if (height(node.right.right) >= height(node.right.left)) {
                // 只进行一次左旋
                return leftRotation(node);
            }

            if (height(node.right.right) < height(node.right.left)) {
                // 先右旋，再左旋
                node.right = rightRotation(node.right);
                return leftRotation(node);
            }
        }

        return node;
    }

    /**
     * 对当前节点及左子节点进行右旋
     *
     * @param parent 待旋转节点
     * @return 旋转之后的父节点
     */
    private Node<E> rightRotation(Node<E> parent) {
        Node<E> left = parent.left;
        parent.left = left.right;
        left.right = parent;

        parent.height = calcHeight(parent);
        left.height = calcHeight(left);

        return left;
    }

    /**
     * 对当前节点及右子节点进行左旋
     *
     * @param parent 待旋转节点
     * @return 旋转之后的父节点
     */
    private Node<E> leftRotation(Node<E> parent) {
        Node<E> right = parent.right;
        parent.right = right.left;
        right.left = parent;

        parent.height = calcHeight(parent);
        right.height = calcHeight(right);

        return right;
    }

    /**
     * 删除元素
     *
     * @return 删除后的根节点
     */
    private Node<E> delete(E e) {
        root = delete(root, e);
        return root;
    }

    /**
     * 删除元素
     *
     * @param node 判断当前元素是否要删除的元素
     * @param e    需要删除的元素
     * @return 删除节点
     */
    private Node<E> delete(Node<E> node, E e) {
        if (node == null) {
            return null;
        }

        // 判断元素在当前节点的左边还是右边，直到找到节点
        if (e.compareTo(node.value) < 0) {
            node.left = delete(node.left, e);
            // 判断高度并且旋转
            return balance(node);
        }

        if (e.compareTo(node.value) > 0) {
            node.right = delete(node.right, e);
            return balance(node);
        }

        // 找到元素
        if (e.compareTo(node.value) == 0) {
            // 判断节点是否有子节点，直接删除
            if (node.left == null && node.right == null) {
                // 因为反参作为上级节点的子节点，所以这里直接返回null即可
                // node = null;
                return null;
            }

            // 如果只有右子节点，用右子节点替换删除节点，这样替换之后node节点不再持有引用，会被垃圾回收
            if (node.left == null && node.right != null) {
                node = node.right;
            }

            if (node.left != null && node.right == null) {
                node = node.left;
            }

            // 这里有两种写法，一种是判断高度之后再将遍历高度更高的那一侧，一种是不管高度，只遍历某一边
            /**
             * 方法1：根据高度进行判断
             * 1.判断子节点高度
             * 2.如果左子节点高度更高，找出左子节点最大值替换当前节点的值
             * 3.删除左子节点的最大节点
             */
            /*if (node.left != null && node.right != null) {
                if (height(node.left) > height(node.right)) {
                    Node<E> maxNode = maxNode(node.left);
                    node.value = maxNode.value;
                    // 注意这里删除的左子节点的最大节点一定是没有右子节点的，因此不会再进入两边都有值的判断条件
                    delete(node.left, maxNode.value);
                } else {
                    Node<E> minNode = minNode(node.right);
                    node.value = minNode.value;
                    delete(node.right, minNode.value);
                }
            }*/

            /**
             * 方法2：不管高度，始终使用右子节点的最小值进行替换
             */
            if (node.left != null && node.right != null) {
                Node<E> minNode = minNode(node.right);
                node.value = minNode.value;
                // 遍历更新高度值
                delete(node.right, minNode.value);
            }
        }

        return node;
    }

    /**
     * 前序遍历
     *
     * @return 通过前序遍历出来的树的所有值
     */
    public List<E> preorderTraversal() {
        return preorderTraversal(root, new ArrayList<>());
    }

    /**
     * 前序遍历
     *
     * @return 通过前序遍历出来的树的所有值
     */
    private List<E> preorderTraversal(Node<E> node, List<E> list) {
        if (node == null) {
            return null;
        }

        list.add(node.value);
        preorderTraversal(node.left, list);
        preorderTraversal(node.right, list);

        return list;
    }

    /**
     * 中序遍历
     *
     * @return 通过中序遍历出来的树的所有值
     */
    public List<E> inorderTraversal() {
        return inorderTraversal(root, new ArrayList<E>());
    }

    /**
     * 中序遍历
     *
     * @return 通过中序遍历出来的树的所有值
     */
    private List<E> inorderTraversal(Node<E> node, List<E> list) {
        if (node == null) {
            return null;
        }

        inorderTraversal(node.left, list);
        list.add(node.value);
        inorderTraversal(node.right, list);

        return list;
    }

    /**
     * 后序遍历
     *
     * @return 通过后序遍历出来的树的所有值
     */
    private List<E> postorderTraversal() {
        return postorderTraversal(root, new ArrayList<E>());
    }

    /**
     * 后序遍历
     *
     * @return 通过后序遍历出来的树的所有值
     */
    private List<E> postorderTraversal(Node<E> node, List<E> list) {
        if (node == null) {
            return null;
        }

        postorderTraversal(node.left, list);
        postorderTraversal(node.right, list);
        list.add(node.value);

        return list;
    }

    /**
     * 深度优先遍历-栈实现
     *
     * @return 数据集合
     */
    public List<E> stackInorderTraversal() {
        return stackInorderTraversal(root);
    }

    /**
     * 深度优先遍历-栈实现，这里使用中序遍历方式
     *
     * @param node 遍历节点
     * @return 数据集合
     */
    private List<E> stackInorderTraversal(Node<E> node) {
        List<E> list = new ArrayList<>();
        StackArray<Node<E>> stack = new StackArray<>();
        stack.push(node);
        // 是否退出整个递归
        while (stack.isNotEmpty()) {
            // 只有第一次递归使用while，其余都是用if条件判断
            while (stack.peek() != null) {
                node = node.left;
                stack.push(node);
            }

            // 将空值退出
            stack.pop();
            if (stack.isNotEmpty()) {
                // 退出父节点
                node = stack.pop();
                list.add(node.value);
                // 就是这一句查找当前节点的右节点解决了死循环
                node = node.right;
                stack.push(node);
            }
        }

        return list;
    }

    /**
     * 通过栈实现前序遍历
     *
     * @return 数据集合
     */
    public List<E> stackPreorderTraversal() {
        return stackPreorderTraversal(root);
    }

    /**
     * 通过栈实现前序遍历
     *
     * @param node 需要遍历的节点
     * @return 数据集合
     */
    private List<E> stackPreorderTraversal(Node<E> node) {
        List<E> list = new ArrayList<>();
        StackArray<Node<E>> stack = new StackArray<>();
        stack.push(root);
        while (stack.isNotEmpty()) {
            while (stack.peek() != null) {
                // 前序遍历先添加值
                node = stack.peek();
                list.add(node.value);

                node = node.left;
                stack.push(node);
            }

            // 去除空指针
            stack.pop();
            if (stack.isNotEmpty()) {
                node = stack.pop();
                node = node.right;
                stack.push(node);
            }
        }

        return list;
    }

    /**
     * 栈-后序遍历
     *
     * @return 数据集合
     */
    public List<E> stackPostorderTraversal() {
        return stackPostorderTraversal(root);
    }

    /**
     * 栈-后续遍历
     * 方式1：通过prev节点保存上次遍历的节点来判断右节点是否已经遍历
     * 如果遍历了则遍历其父节点，否则遍历右节点
     *
     * @param node 遍历节点
     * @return 数据集合
     */
    private List<E> stackPostorderTraversal(Node<E> node) {
        List<E> list = new ArrayList<>();
        StackArray<Node<E>> stack = new StackArray<>();
        Node<E> prev = null; //上一次访问的节点
        stack.push(node);
        while (stack.isNotEmpty()) {
            while (stack.peek() != null) {
                node = node.left;
                stack.push(node);
            }

            // 空指针出栈
            stack.pop();
            if (stack.isNotEmpty()) {
                node = stack.peek();
                // 右节点为空或已经遍历右节点，遍历父节点
                if (node.right == null || node.right == prev) {
                    node = stack.pop();
                    prev = node;
                    list.add(node.value);
                    // push空值，不进上面的while条件
                    stack.push(null);
                } else {
                    // 没有遍历则将右节点入栈
                    node = node.right;
                    stack.push(node);
                }
            }
        }

        return list;
    }

    /**
     * 栈-后续遍历
     * 方式2：因为后序遍历的遍历顺序是左右父，也就是说出栈顺序是左右父
     * 因此可以按照父右左的方式进行入栈，也就是先入栈右节点
     * 这样需要两个栈，对空间开销比较大
     *
     * @return 数据集合
     */
    private List<E> stackPostorderTraversal1() {
        Node<E> node = root;
        ArrayList<E> list = new ArrayList<>();
        StackArray<Node<E>> stack = new StackArray<>();
        // 再新建一个栈用于保存所有节点
        StackArray<Node<E>> stack1 = new StackArray<>();
        stack.push(node);
        stack1.push(node);
        while (stack.isNotEmpty()) {
            while (stack.peek() != null) {
                node = node.right;
                stack.push(node);
                stack1.push(node);
            }

            stack.pop();
            stack1.pop();

            // 遍历左节点
            if (stack.isNotEmpty()) {
                node = stack.pop().left;
                stack.push(node);
                stack1.push(node);
            }
        }

        // 遍历栈取值
        while (stack1.isNotEmpty()) {
            list.add(stack1.pop().value);
        }

        return list;
    }

    /**
     * 使用队列实现层序遍历
     *
     * @return 数据集合
     */
    public List<E> queueTraversal() {
        return queueTraversal(root);
    }

    /**
     * 使用队列实现层序遍历
     *
     * @param node 节点
     * @return 反参
     */
    private List<E> queueTraversal(Node<E> node) {
        List<E> list = new ArrayList<>();
        QueueArray<Node<E>> queue = new QueueArray<>();
        queue.enQueue(node);
        while (queue.isNotEmpty()) {
            node = queue.deQueue();
            list.add(node.value);
            // 先遍历左节点
            if (node.left != null) {
                queue.enQueue(node.left);
            }

            if (node.right != null) {
                queue.enQueue(node.right);
            }
        }

        return list;
    }
}
