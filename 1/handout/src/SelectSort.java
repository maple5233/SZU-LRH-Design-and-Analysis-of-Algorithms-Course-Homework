import java.util.Date;
import java.util.Scanner;

/**
 * Created by hongjiyao_2014150120 on 16-9-13.
 */
public class SelectSort {
    private static final int testNum = 20;
    private static final int rank = 1000; // 随机数的大小区间

    private static long Sort(final int size, int[] S) {
        long now = new Date().getTime();
        int index;
        for (int i = 0; i < size; i++) {
            index = i; //记下位置
            for (int j = i + 1; j < size; j++) {
                if (S[j] < S[index]) {
                    index = j; //找出最小者
                }
            }
            if (index != i) {
                S[index] = S[index] ^ S[i];
                S[i] = S[index] ^ S[i];
                S[index] = S[index] ^ S[i];
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
            for (int j = 0; j < size; j++) {
                temp[j] = (int) (Math.random() * rank);
                // System.out.print(temp[j]+" ");
            }
            System.out.print("");
            /* 交给排序函数 */
            result += Sort(size, temp);
        }
        System.out.println();
        System.out.println("耗时" + result + "ms");
        System.out.println("平均耗时" + (double) result / (double) testNum + "ms");
    }
}
