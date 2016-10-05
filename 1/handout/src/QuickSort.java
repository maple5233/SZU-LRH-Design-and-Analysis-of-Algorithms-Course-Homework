import java.util.Date;
import java.util.Scanner;

/**
 * Created by hongjiyao_2014150120 on 16-9-13.
 */
public class QuickSort {
    private static final int testNum = 20;
    private static final int rank = 1000; // 随机数的大小区间

    public static void quickSort(int[] arr) {
        qsort(arr, 0, arr.length - 1);
    }

    private static void qsort(int[] arr, final int low, final int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);        //将数组分为两部分
            qsort(arr, low, pivot - 1);                   //递归排序左子数组
            qsort(arr, pivot + 1, high);                  //递归排序右子数组
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[low];     //第一个作为枢轴
        while (low < high) {
            while (low < high && arr[high] >= pivot)
                --high;
            arr[low] = arr[high];             //交换比枢轴小的记录到左端
            while (low < high && arr[low] <= pivot)
                ++low;
            arr[high] = arr[low];           //交换比枢轴小的记录到右端
        }
        //扫描完成，枢轴到位
        arr[low] = pivot;
        //返回的是枢轴的位置
        return low;
    }

    private static long Sort(int[] S) {
        long now = new Date().getTime();
        quickSort(S);
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
            System.out.print("");
            /* 交给排序函数 */
            result += Sort(temp);
        }
        System.out.println();
        System.out.println("耗时" + result + "ms");
        System.out.println("平均耗时" + (double) result / (double) testNum + "ms");
    }
}

