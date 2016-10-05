import java.util.Date;
import java.util.Scanner;

/**
 * Created by hongjiyao_2014150120 on 16-9-13.
 */
public class MergeSort {
    private static final int testNum = 20;
    private static final int rank = 1000; // 随机数的大小区间

    private static void Copy(int[] a, int[] b, final int left, final int right) {
        //拷贝数组b至a
        int i;
        for (i = left; i <= right; i++)
            a[i] = b[i];
    }

    private static void merge(int[] R1, int[] R2, int l, int m, int r) {
        //将 R1 [l:m] 和 R1 [m+ 1 :r] 归并到R2 [l:r]
        int i = l, j = m + 1, k = l;
        while ((i <= m) && (j <= r)) {
            if (R1[i] <= R1[j])
                R2[k++] = R1[i++];
            else
                R2[k++] = R1[j++];
        }
        // 接上末尾的部分
        if (i > m) {
            for (int q = j; q <= r; q++)
                R2[k++] = R1[q];
        } else {
            for (int q = i; q <= m; q++)
                R2[k++] = R1[q];
        }

    }

    private static void mergeSort(int[] S, int[] T, int left, int right) {
        if (right > left) {
            int i = (left + right) / 2;
            mergeSort(S, T, left, i);
            mergeSort(S, T, i + 1, right);
            merge(S, T, left, i, right); //合并到数组T
            Copy(S, T, left, right); //复制回数组S
        }

    }

    private static long Sort(int[] S, final int left, final int right) {
        long now = new Date().getTime();

        int[] T = new int[right - left + 1];
        for (int s = 0; s < T.length; s++)
            T[s] = 0;
        mergeSort(S, T, left, right);

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
            /* 交给排序函数 */
            result += Sort(temp, 0, size - 1);
        }
        System.out.println();
        System.out.println("耗时" + result + "ms");
        System.out.println("平均耗时" + (double) result / (double) testNum + "ms");
    }
}
