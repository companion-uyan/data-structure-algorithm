package person.companion.array;

import org.junit.Test;

import java.io.*;

/**
 * 功能描述：使用稀疏数组完成五子棋盘的存盘续盘功能
 *
 * author: companion
 * Write by: 2021/7/14 17:44
 */
public class Chess {
    // 保存五子棋的原二维数组
    int[][] arr = new int[11][11];
    // 保存压缩有五子棋的稀疏数组
    int[][] chess = null;
    String fileName = "E:\\chess.txt";

    /**
     * 五子棋压缩并且存盘功能
     */
    @Test
    public void test1() {
        arr[1][2] = 1;
        arr[2][3] = 2;

        // 遍历打印原棋盘
        // printArr(arr);
        // 压缩为稀疏数组
        compressArr(arr);
        // 打印稀疏数组
        // printArr(chess);
        // 还原稀疏数组并打印还原后的数据
        // int[][] temp = recoverArr(chess);
        // printArr(temp);
        // 保存数据到磁盘
        // saveChess(chess);
        // 读取磁盘数据并打印
        readChess();
    }

    /**
     * 读取磁盘数据并打印
     */
    private void readChess() {
        int[][] temp = null;
        // 记录reader当前的行数，用于第一行获取数组规模初始化二维数组
        int lineNum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                String value = line.substring(0, line.length() - 1);
                String[] split = value.split(",");
                if (lineNum == 1) {
                    temp = new int[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
                }

                if (lineNum != 1) {
                    temp[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = Integer.parseInt(split[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 打印
        printArr(temp);
    }

    /**
     * 数据存盘
     *
     * @param chess 稀疏数组
     */
    private void saveChess(int[][] chess) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int[] ints : chess) {
                for (int anInt : ints) {
                    writer.write(anInt + ",");
                }

                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 还原稀疏数组
     *
     * @param chess 需要还原的稀疏数组
     */
    private int[][] recoverArr(int[][] chess) {
        // 取稀疏数组的前两个数组初始化还原数组
        int[][] temp = new int[chess[0][0]][chess[0][1]];
        for (int i = 0; i < chess.length; i++) {
            if (i != 0) {
                // 非空值所在行号
                int row = chess[i][0];
                // 非空值所在列下标
                int col = chess[i][1];
                // 赋值
                temp[row][col] = chess[i][2];
            }
        }

        return temp;
    }

    /**
     * 将原数组压缩为稀疏数组
     *
     * @param arr 原数组
     */
    private void compressArr(int[][] arr) {
        // 记录非空记录值的个数
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int value = arr[i][j];
                if (value != 0) {
                    num++;
                }
            }
        }

        // 初始化稀疏数组规模
        chess = new int[num + 1][];
        chess[0] = new int[]{arr[0].length, arr[1].length, num};
        // 记录稀疏数组当前所在行数
        int rowNum = 1;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int value = arr[i][j];
                if (value != 0) {
                    chess[rowNum++] = new int[]{i, j, value};
                }
            }
        }
    }

    /**
     * 打印二维数组
     *
     * @param arr 二维数组
     */
    private void printArr(int[][] arr) {
        for (int[] ints : arr) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }

            System.out.println();
        }
    }
}
