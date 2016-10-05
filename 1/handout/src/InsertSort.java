import java.util.Date;
import java.util.Scanner;

/**
 * Created by hongjiyao_2014150120 on 16-9-13.
 */
public class InsertSort {
    private static final int testNum = 50;
    private static final int rank = 1000; // 随机数的大小区间

    private static long Sort(final int size, final int[] S) {
        long now = new Date().getTime();
        int temp, j;
        for (int i = 1; i < size; i++) {
            if (S[i] < S[i - 1]) {
                temp = S[i];
                j = i - 1;
                do {
                    S[j + 1] = S[j];
                    j--;
                } while (j >= 0 && temp < S[j]);
                S[j + 1] = temp;
            }
        }
        long after = new Date().getTime();
        return after - now;
    }

    public static void main(final String[] args) {
        int size;
        System.out.println("输入您的数据规模：");
        Scanner in = new Scanner(System.in);
        size = in.nextInt();
        long result = 0;
        for (int i = 0; i < testNum; i++) {
            /* 生成随机数组 */
            int[] temp = new int[size];
            // System.out.println("原数组如下:");
            for (int j = 0; j < size; j++) {
                temp[j] = (int) (Math.random() * rank);
            }
            System.out.print("");
            System.out.print("");
            /* 交给排序函数 */
            result += Sort(size, temp);
        }
        System.out.println();
        System.out.println("耗时" + result + "ms");
        System.out.println("平均耗时" + (double) result / (double) testNum + "ms");
    }
}
