/**
 * Created by hongjiyao_2014150120 on 16-9-28.
 */

/**
 * 解决八皇后问题的类
 */
public class ManyQueens {
    private static final int QUEEN_NUM = 8;
    private static int[] queens = new int[QUEEN_NUM]; // 皇后结果保存数组
    private static int numOfAns = 0; // 解的个数

    /***
     * 摆放皇后的函数模块 回溯地去尝试所有的可能解
     * @param n 刚开始要摆的位置
     */
    private static void check(int n) { // 当前是准备摆第n个皇后
        if (n == QUEEN_NUM) { // 摆完了
            numOfAns++;
            display();
            return; // 返回上一个可行情形，以便寻找下一个解
        }
        for (int i = 0; i < QUEEN_NUM; i++) {
            queens[n] = i; // 尝试放皇后
            if (judge(n)) {
                check(n + 1); // 尝试下一个位置的可能 即使尝试成功后还是会退回来 因此可遍历所有可能解
            }
        }
    }

    /***
     * 判断皇后的位置是否摆放正确的判断模块
     * @param n 摆的位置
     * @return 是否合法
     */
    private static boolean judge(int n) {
        for (int i = 0; i < n; i++) {
            if (queens[i] == queens[n] || Math.abs(n - i) == Math.abs(queens[n] - queens[i])) {
                return false; // 只需要检查新放进来的queens[n]
            }
        }
        return true;
    }

    /***
     * 展示皇后的位置
     */
    private static void display() {
        System.out.print("[ ");
        for (int queen : queens) {
            System.out.print(queen + 1 + " ");
        }
        System.out.print(']');
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println(QUEEN_NUM + "皇后问题的所有解如下：");
        check(0);
        System.out.println("共有解" + numOfAns + "个");
    }
}
