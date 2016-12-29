/**
 * Created by hongjiyao_2014150120 on 16-12-21.
 * 求资源分配问题的最大利润
 */

import java.util.Arrays;
import java.util.Date;

/**
 * 最大利润主类
 */
public class MaxProfits {

    private static final int M = 1000;            // 设备数量
    private static final int N = 3000;             // 车间数量
    private static final int MIN_PROFIT = 30;   // 最小利润的最大值
    private static final int PROFIT_STEP = 30;  // 利润随机增加的最大值

    /**
     * 求最大利润
     *
     * @param g 利润表
     *          g[i][j] 表示将 j+1 台设备分配给 i+1 号车间得到的利润（为了方便数组索引从1开始）
     *          f[i][j] 表示将 j 台设备分配给前 i 个车间的最大利润 （即题目所求的是p[m][n]）
     *          则f[i][j]是以下两种情况的较大值：
     *          (1)不分配给当前的i号车间，此时的最大值为将j台设备分配给前i-1个车间的最大值，f[i][j] = f[i-1][j]；
     *          (2)分配k台设备给i号车间，剩下的j-k台设备分配给前i-1个车间，
     *          此时f[i][j] = g[i-1][k-1] + f[i-1][j-k]，其中 1≤ k ≤j。
     *          也就是递推方程：f[i][j] = max{f[i-1][j], g[i-1][k-1] + f[i-1][j-k]}(1<= k <=j)
     *          初始化p[0][j] = 0（将 j 台设备分配给前 0 个车间），p[i][0] = 0（将 0 台设备分配给前 i 个车间）
     *          从i = 0 开始自底向上构造表即可计算出 p[m][n]
     *          l[i][j]代表分配给车间i的设备
     * @return 结果字符串
     */
    private static String getMaxProfitResultString(final int[][] g) {
        int m = N + 1, n = M + 1;       // 为了方便，数组索引从1开始，即m*n = (N+1)*(M+1)
        int[][] f = new int[m][n];      // 要填的表
        int[][] l = new int[m][n];      // 结果记录

        /**
         * 初始化表
         */
        for (int i = 0; i < m; i++) {
            f[i][0] = 0;                // 将0台设备分配给前i个车间 肯定不产生利润
        }
        for (int j = 0; j < n; j++) {
            f[0][j] = 0;                // 将j台设备分配给前0个车间 肯定不产生利润
        }

        /**
         * 填表
         */
        long start = new Date().getTime();
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // 情况1 不分配给i号车间，此时的最大值为将j台设备分配给前i-1个车间的最大值 f[i][j] = f[i-1][j]；
                int max = f[i - 1][j];
                l[i][j] = 0;
                // 情况2 分配k台设备给i号车间得g[i-1][k-1]，剩下的j-k台设备分配给前i-1个车间得f[i-1][j-k]
                // f[i][j] = g[i-1][k-1] + f[i-1][j-k]，其中 1≤ k ≤j。
                for (int k = 1; k <= j; k++) {
                    if (max < g[i - 1][k - 1] + f[i - 1][j - k]) {
                        max = g[i - 1][k - 1] + f[i - 1][j - k];
                        l[i][j] = k;
                    }
                }
                f[i][j] = max;
            }
        }
        long end = new Date().getTime();
        // 查表计算解决方案
        StringBuilder result = new StringBuilder();
        // 从最后一个车间、满设备开始逆推
        for (int i = m - 1, j = n - 1; i > 0 && j > 0; ) {
            if (l[i][j] != 0) { // l[i][j]==k表示这个车间分配到k台设备，此时 i--, j-=k
                result.insert(0, "\n" + "车间" + i + "得到" + l[i][j] + "件设备");
                j -= l[i][j];
                i--;
            } else { // l[i][j]==0表示这个车间没有分配到设备，此时i--
                i--;
            }
        }

        result.insert(0, "最大利润: " + f[m - 1][n - 1]);
        result.append("\n" + "总共花去").append(end - start).append("ms.");
        return result.toString();
    }

    /**
     * 随机生成利润表
     *
     * @return 生成的随机利润表g
     */
    private static int[][] getRandomG() {
        int[][] g = new int[N][M];      // 利润表

        for (int i = 0; i < N; i++) {
            g[i][0] = (int) (Math.random() * MIN_PROFIT); // 单件利润范围0-30
            for (int j = 1; j < M; j++) {
                g[i][j] = g[i][j - 1] + (int) (Math.random() * PROFIT_STEP); // 后面的利润随机增加0-10
            }
        }
        return g;
    }

    public static void main(final String[] args) {
        int[][] g = getRandomG();
//        int[][] g = {
//                {13, 21, 30, 32, 34, 41, 41, 47, 54, 54},
//                {26, 31, 31, 34, 37, 45, 54, 58, 58, 65},
//                {25, 25, 32, 36, 43, 49, 49, 49, 55, 56}
//        };
        System.out.println("利润表如下：");
        for (int i = 0; i < N; i++) {
            System.out.println("车间" + (i + 1) + "：" + Arrays.toString(g[i]));
        }
        System.out.println("最大利润方案如下：\n" + getMaxProfitResultString(g));
    }
}
