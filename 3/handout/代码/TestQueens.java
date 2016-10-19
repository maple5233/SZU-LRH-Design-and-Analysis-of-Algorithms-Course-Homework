/**
 * Created by hongjiyao_2014150120 on 16-10-19.
 */

import java.util.Scanner;

/**
 * 测试棋盘是否合法的类
 */
public class TestQueens {
    private static final int QUEENS_NUM = 8;
    private static int[] checkerboard = new int[QUEENS_NUM]; //棋盘
    private static Scanner in = new Scanner(System.in);

    /***
     * 根据输入生成棋盘 每摆放一次棋子 检查是否合法
     *
     * @return 摆放是否成功
     */
    private static boolean initCheckerboard() {
        // 初始化棋盘
        for (int i = 0; i < QUEENS_NUM; i++) {
            checkerboard[i] = -QUEENS_NUM * i;
        }
        for (int i = 0; i < QUEENS_NUM; i++) {
            int row = in.nextInt(); // 行
            int col = in.nextInt(); // 列
            checkerboard[row - 1] = col - 1; // 摆放棋子
            if (!judge(col - 1)) {
                return false;
            }
        }
        return true;
    }

    /***
     * 判断皇后的位置是否摆放正确的判断模块
     *
     * @param col 摆的位置 哪一列
     * @return 是否合法
     */
    private static boolean judge(int col) {
        for (int i = 0; i < col; i++) {
            if (checkerboard[i] == checkerboard[col]
                    || Math.abs(col - i) == Math.abs(checkerboard[col] - checkerboard[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        boolean success = initCheckerboard();
        if (success) {
            System.out.println("摆放成功！");
        } else {
            System.out.println("摆放失败！");
        }
    }
}
